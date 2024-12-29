public class Tree_2_3<T> {
    private Node<T> root;
    private Comparator<T> comparator;


    public void init(Comparator<T> comparator, T minValue, T maxValue) {
        root = new Node(null, maxValue);
        Node<T> leftSentinel = new Node(root,minValue);
        Node<T> middleSentinel = new Node(root,maxValue);
        leftSentinel.setLeavesCount(0);
        middleSentinel.setLeavesCount(0);
        root.setChildren(leftSentinel, middleSentinel, null);
        this.comparator = comparator;
    }

    public int rank(T value) {
        Node<T> leaf = root.search(value,comparator);
        if (leaf == null) { //if there is no such runner
            throw new IllegalArgumentException();
        }
        return leaf.Rank();
    }

    public void delete(T value) {
        Node<T> nodeToBeDeleted = root.search(value, comparator);
        if (nodeToBeDeleted == null) {
            throw new IllegalArgumentException();
        }
        Node<T> parent = nodeToBeDeleted.getParent();

        //if nodeToBeDeleted is a left child
        if (nodeToBeDeleted == parent.getLeft()) {
            //delete the child and update the parent's children fields
            parent.setChildren(parent.getMiddle(), parent.getRight(), null);
        }
        //if nodeToBeDeleted is a middle child
        else if (nodeToBeDeleted == parent.getMiddle()) {
            //delete the child and update the parent's children fields
            parent.setChildren(parent.getLeft(), parent.getRight(), null);
        }
        //if nodeToBeDeleted is a left child
        else {
            //delete the child and update the parent's children fields
            parent.setChildren(parent.getLeft(), parent.getMiddle(), null);
        }


        //check if the parent of the deleted node is legal - has 2 children after deletion
        while (parent != null) {
            //case 1: the parent is legal
            if (parent.getMiddle() != null) {
                //update the parent key to be the maximum value of all it's children keys
                parent.updateKey();
                parent = parent.getParent();
            }
            //case 2: the parent has 1 child after deletion
            else {
                //if not the root, then check if the parent can borrow from it's sibling
                if (parent != root) {
                    parent = parent.borrowOrMerge();
                }
                //if the parent is the root, delete root and initialize it's left child to be the new root
                else {
                    root = parent.getLeft();
                    parent.getLeft().setParent(null);
                    return;
                }
            }
        }
    }

    public T search(T value) {
        Node<T> node = root.search(value,comparator);
        if (node == null) {
            return null;
        }
        return node.getMaxKey();
    }

    /**
     * Returns the minimal key in the tree. If the tree is empty, the maximum value is returned
     */
    public T minimum() {
        Node<T> node = root;
        while (!node.isLeaf()) {
            node = node.getLeft();
        }
        node = node.getParent().getMiddle();
        return node.getMaxKey();
    }


    public void insert(T value) {
        if (root.search(value,comparator) != null) {
            throw new IllegalArgumentException();
        }
        Node<T> nodeToBeInserted = new Node(null,value);
        Node<T> current = root;
        while (!current.isLeaf()) {
            if (comparator.compare(nodeToBeInserted.getMaxKey(), current.getLeft().getMaxKey()) < 0) {
                current = current.getLeft();
            }
            else if (comparator.compare(nodeToBeInserted.getMaxKey(), current.getMiddle().getMaxKey()) < 0) {
                current = current.getMiddle();
            }
            else {
                current = current.getRight();
            }
        }
        Node<T> parent = current.getParent();

        nodeToBeInserted = parent.insertAndSplit(nodeToBeInserted,comparator);
        while (parent!=root) {
            parent = parent.getParent();
            if (nodeToBeInserted!=null) {
                nodeToBeInserted = parent.insertAndSplit(nodeToBeInserted,comparator);
            }
            else {
                parent.updateKey();
            }
        }
        if (nodeToBeInserted!=null) {
            Node<T> newRoot = new Node(null,null);
            newRoot.setChildren(parent,nodeToBeInserted,null);
            root = newRoot;
        }
    }
    public Node<T> getRoot() {
        return root;
    }
}