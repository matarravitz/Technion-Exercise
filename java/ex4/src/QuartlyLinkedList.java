import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a linked list data structure with directional nodes.
 * @param <E> The type of elements stored in the linked list.
 */
public class QuartlyLinkedList<E extends Cloneable> implements Iterable<QuartNode<E>>, Cloneable {
    protected QuartNode<E> root;
    /**
     * Constructs an empty QuartlyLinkedList.
     */
    public QuartlyLinkedList() {
        this.root = null;
    }
    /**
     * Returns the root node of the linked list.
     * @return The root node of the linked list.
     */
    public QuartNode<E> getRoot() {
        return root;
    }
    /**
     * Adds a new node with the specified element to the linked list.
     * The new node is connected to the specified target node in the specified direction.
     * If the target node doesn't exist or the direction is already occupied, exceptions are thrown.
     * @param toInsert The element to insert into the new node.
     * @param target The element of the target node to which the new node is connected.
     * @param direction The direction in which the new node is connected to the target node.
     * @throws NoSuchElement If the target node doesn't exist.
     * @throws DirectionIsOccupied If the direction is already occupied.
     */
    public void add(E toInsert, E target, Direction direction) {
        if (root == null) {
            root = new QuartNode<>(toInsert);
        } else {
            QuartNode<E> nodeTarget = search(target);
            if (nodeTarget == null) {
                throw new NoSuchElement();
            }
            if (!checkIfDirectionNull(direction, nodeTarget)) {
                throw new DirectionIsOccupied();
            }
            new QuartNode<>(toInsert, direction, nodeTarget);
        }
    }
    /**
     * Searches for a node containing the specified element in the linked list.
     * @param toSearch The element to search for.
     * @return The node containing the specified element, or null if not found.
     */
    public QuartNode<E> search(E toSearch) {
        for (QuartNode<E> node : this) {
            if (toSearch.equals(node.getValue())) {
                return node;
            }
        }
        return null;
    }
    /**
     * Removes the node containing the specified element from the linked list.
     * @param toRemove The element to remove from the linked list.
     * @throws NoSuchElement If the specified element is not found in the linked list.
     */
    public void remove(E toRemove) {
        if (root == null) {
            throw new NoSuchElement();
        }
        QuartNode<E> nodeToInsert = search(toRemove);
        if (nodeToInsert == null) {
            throw new NoSuchElement();
        } else {
            nodeToInsert.WEST = null;
            nodeToInsert.EAST = null;
            nodeToInsert.SOUTH = null;
            nodeToInsert.NORTH = null;
        }
    }
    /**
     * Provides an iterator for iterating the nodes in the linked list.
     * @return An iterator for the nodes in the linked list.
     */
    @Override
    public Iterator<QuartNode<E>> iterator() {
        return new QuartlyLinkedListIterator<>(root);
    }
    /**
     * Checks if the linked list is empty.
     * @return true if the linked list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return root==null;
    }
    /**
     * Checks if the specified direction from the given node is null.
     * @param direction The direction to check.
     * @param node The node from which the direction is checked.
     * @return true if the specified direction from the given node is null, false otherwise.
     */
    public boolean checkIfDirectionNull(Direction direction, QuartNode<E> node) {
        switch (direction) {
            case NORTH:
                return node.NORTH == null;
            case SOUTH:
                return node.SOUTH == null;
            case EAST:
                return node.EAST == null;
            default:
                return node.WEST == null;
        }
    }
    /**
     * Creates and returns a deep copy of this QuartlyLinkedList object.
     * @return A deep copy of this QuartlyLinkedList object.
     */
    @Override
    public QuartlyLinkedList<E> clone() {
        try {
            QuartlyLinkedList<E> clonedList = (QuartlyLinkedList<E>) super.clone();
            clonedList.root = cloneNode(root, new HashSet<>()); // Clone the root node recursively
            return clonedList;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    /**
     * helps clone function.
     * @return A deep copy of this Node.
     */
    private QuartNode<E> cloneNode(QuartNode<E> node, Set<QuartNode<E>> visited) {
        if (node == null || visited.contains(node)) {
            return null;
        }
        visited.add(node);

        QuartNode<E> clonedNode = node.clone();


        clonedNode.NORTH = cloneNode(node.NORTH, visited);
        clonedNode.EAST = cloneNode(node.EAST, visited);
        clonedNode.SOUTH = cloneNode(node.SOUTH, visited);
        clonedNode.WEST = cloneNode(node.WEST, visited);

        return clonedNode;
    }
}
