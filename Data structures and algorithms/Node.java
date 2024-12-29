public class Node<T> {
    private int numOfLeaves; //number of leaves in the subtree of this node
    private T maxKey; //maximum of the keys of all the leaves in the subtree of this node
    private Node<T> left;
    private Node<T> middle;
    private Node<T> right;
    private Node<T> parent;


    public Node(Node<T> parent, T value) {
        this.parent = parent;
        this.maxKey = value;
        this.numOfLeaves = 1;
    }

    public void setLeavesCount(int numberOfLeaves) {
        numOfLeaves = numberOfLeaves;
    }
    public int getLeavesCount() {
        return numOfLeaves;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getMiddle() {
        return middle;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setMiddle(Node<T> middle) {
        this.middle = middle;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> node) {
        parent = node;
    }

    public T getMaxKey() {
        return maxKey;
    }

    public void setMaxKey(T maxKey) {
        this.maxKey = maxKey;
    }

    public boolean isLeaf() {
        return left == null;
    }

    public void updateKey() {
        maxKey = left.maxKey;
        numOfLeaves = left.numOfLeaves;
        if (middle != null) {
            maxKey = middle.maxKey;
            numOfLeaves += middle.numOfLeaves;
        }
        if (right != null) {
            maxKey = right.maxKey;
            numOfLeaves += right.numOfLeaves;
        }
    }

    public void setChildren(Node<T> newLeft, Node<T> newMiddle, Node<T> newRight) {
        left = newLeft;
        middle = newMiddle;
        right = newRight;
        newLeft.parent = this;
        if (newMiddle != null) {
            newMiddle.parent = this;
        }
        if (newRight != null) {
            newRight.parent = this;
        }
        updateKey();
    }

    public Node<T> search(T value, Comparator<T> comparator) {
        if(isLeaf()) {
            if (comparator.compare(maxKey, value) == 0) {
                return this;
            }
            else { //if not fount in the tree
                return null;
            }
        }
        if(comparator.compare(value, left.maxKey) <= 0) {
            return left.search(value, comparator);
        }
        if(comparator.compare(value, middle.maxKey) <= 0) {
            return middle.search(value, comparator);
        }
        return right.search(value, comparator);
    }

    public Node<T> insertAndSplit(Node<T> nodeToInsert, Comparator<T> comparator) {
        Node<T> l = left;
        Node<T> m = middle;
        Node<T> r = right;


        if (r == null) {
            if (comparator.compare(nodeToInsert.maxKey,l.maxKey) < 0) {
                setChildren(nodeToInsert, l, m);
            } else if (comparator.compare(nodeToInsert.maxKey,m.maxKey) < 0) {
                setChildren(l, nodeToInsert, m);
            } else {
                setChildren(l, m, nodeToInsert);
            }
            return null;
        }

        Node<T> newInternal = new Node<T>(null, null);
        if (comparator.compare(nodeToInsert.maxKey, l.maxKey) < 0) {
            setChildren(nodeToInsert, l, null);
            newInternal.setChildren(m, r, null);
        } else if (comparator.compare(nodeToInsert.maxKey, m.maxKey) < 0) {
            setChildren(l, nodeToInsert, null);
            newInternal.setChildren(m, r, null);
        } else if (comparator.compare(nodeToInsert.maxKey, r.maxKey) < 0) {
            setChildren(l, m, null);
            newInternal.setChildren(nodeToInsert, r, null);
        } else {
            setChildren(l, m, null);
            newInternal.setChildren(r, nodeToInsert, null);
        }
        return newInternal;
    }


    public Node<T> borrowOrMerge() {
        Node<T> parent = getParent();
        Node<T> sibling;

        //if the node that wants to borrow is the left child
        if (this == parent.getLeft()) {
            //initialize sibling of the node to be the middle child of the mutual parent
            sibling = parent.middle;
            //if the sibling has right child, move left child of the sibling to the node
            //as a new middle child. update the sibling's children accordingly
            if (sibling.getRight() != null) {
                setChildren(getLeft(), sibling.getLeft(), null);
                sibling.setChildren(sibling.getMiddle(), sibling.getRight(), null);
            } else { //the sibling has 2 children and the node therefor cannot borrow from the sibling
                //move the only child of the node to its sibling
                sibling.setChildren(getLeft(), sibling.getLeft(), sibling.getMiddle());
                //update the parent's children
                parent.setChildren(sibling, parent.getRight(), null);
            }
            return parent;
        }
        //if the node that wants to borrow is the middle child
        if (this == parent.middle) {
            //initialize sibling of the node to be the left child of the mutual parent
            sibling = parent.getLeft();
            //if the sibling has right child, move right child of the sibling to be the node's
            //new left child, and the node's left child to be now it's middle child.
            //update the sibling's children accordingly
            if (sibling.getRight() != null) {
                setChildren(sibling.getRight(), getLeft(), null);
                sibling.setChildren(sibling.getLeft(), sibling.getMiddle(), null);
            } else { //the sibling has 2 children and the node therefore cannot borrow from the sibling
                //move the only child of the node to its sibling
                sibling.setChildren(sibling.getLeft(), sibling.getMiddle(), getLeft());
                parent.setChildren(sibling, parent.getRight(), null);
            }
            return parent;
        }
        //if the node that wants to borrow is the right child
        //initialize sibling of the node to be the middle child of the mutual parent
        sibling = parent.middle;
        //if the sibling has right child, move right child of the sibling to be the node's
        //new left child, and the node's left child to be now it's middle child.
        //update the sibling's children accordingly
        if (sibling.getRight() != null) {
            setChildren(sibling.getRight(), getLeft(), null);
            sibling.setChildren(sibling.getLeft(), sibling.getMiddle(), null);
        } else { //the sibling has 2 children and the node therefor cannot borrow from the sibling
            //move the only child of the node to its sibling
            sibling.setChildren(sibling.getLeft(), sibling.getMiddle(), getLeft());
            parent.setChildren(parent.getLeft(), sibling, null);
        }
        return parent;
    }

    public int Rank() {
        int rank = 1;
        Node<T> current = this;
        Node<T> parent = current.getParent();
        while (parent != null) {
            if (current == parent.getMiddle()) {
                rank += parent.getLeft().getLeavesCount();
            } else if (current == parent.getRight()) {
                rank += parent.getLeft().getLeavesCount() + parent.getMiddle().getLeavesCount();
            }
            current = parent;
            parent = current.getParent();
        }
        return rank;
    }

    public void setLeaf(T value) {
        this.maxKey = value;
        this.numOfLeaves = 1;
    }

}