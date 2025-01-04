import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents a node in a quartly linked list.
 *
 * @param <E> The type of element stored in the node.
 */
public class QuartNode<E extends Cloneable> implements Cloneable {
    protected E value;
    protected QuartNode<E> NORTH;
    protected QuartNode<E> EAST;
    protected QuartNode<E> SOUTH;
    protected QuartNode<E> WEST;
    /**
     * Constructs a new QuartNode with the specified value and initializes directions to null.
     *
     * @param value The value to be stored in the node.
     */
    public QuartNode(E value) {
        this.value = value;
        this.NORTH = null;
        this.EAST = null;
        this.SOUTH = null;
        this.WEST = null;
    }
    /**
     * Constructs a new QuartNode with the specified value and connects it to another node in the specified direction.
     *
     * @param value      The value to be stored in the new node.
     * @param direction  The direction in which the new node is connected to the other node.
     * @param other      The node to which the new node is connected.
     */
    public QuartNode(E value, Direction direction, QuartNode<E> other) {
        QuartNode<E> newNode = new QuartNode<>(value);
        switch (direction) {
            case NORTH:
                newNode.SOUTH = other;
                other.NORTH = newNode;
                break;
            case EAST:
                newNode.WEST = other;
                other.EAST = newNode;
                break;
            case SOUTH:
                newNode.NORTH = other;
                other.SOUTH = newNode;
                break;
            case WEST:
                newNode.EAST = other;
                other.WEST = newNode;
                break;
        }
    }
    /**
     * Returns the value stored in the node.
     *
     * @return The value stored in the node.
     */
    public E getValue() {
        return value;
    }
    /**
     * Returns the node in the NORTH direction.
     *
     * @return The node in the NORTH direction.
     */
    public QuartNode<E> getNORTH() {
        return NORTH;
    }
    /**
     * Returns the node in the East direction.
     *
     * @return The node in the East direction.
     */
    public QuartNode<E> getEAST() {
        return EAST;
    }
    /**
     * Returns the node in the South direction.
     *
     * @return The node in the South direction.
     */
    public QuartNode<E> getSOUTH() {
        return SOUTH;
    }
    /**
     * Returns the node in the West direction.
     *
     * @return The node in the West direction.
     */
    public QuartNode<E> getWEST() {
        return WEST;
    }

    /**
     * Returns the neighbor node in the specified direction.
     *
     * @param direction The direction of the neighbor node to returns.
     * @return The neighbor node in the specified direction.
     */
    public QuartNode<E> getRoomInDirection(Direction direction) {
        switch (direction) {
            case NORTH:
                return this.NORTH;
            case SOUTH:
                return this.SOUTH;
            case EAST:
                return this.EAST;
            default:
                return this.WEST;
        }
    }
    /**
     * Sets the value stored in the node.
     *
     * @param value The value to be stored in the node.
     */
        public void setValue (E value){
            this.value = value;
        }
    /**
     * Returns the neighbor node in the specified direction.
     *
     * @param direction The direction of the neighbor node to retrieve.
     * @return The neighbor node in the specified direction.
     */
        public QuartNode<E> getNeighbor (Direction direction){
            switch (direction) {
                case NORTH:
                    return this.NORTH;
                case EAST:
                    return this.EAST;
                case SOUTH:
                    return this.SOUTH;
                default:
                    return this.WEST;
            }
        }
    /**
     * Creates and returns a deep copy of this QuartNode.
     *
     * @return A deep copy of this QuartNode.
     */
        @Override
        public QuartNode<E> clone () {
            try {
                QuartNode<E> clone = (QuartNode<E>) super.clone();
                Method cloneMethod = value.getClass().getMethod("clone");
                clone.value = (E) cloneMethod.invoke(value);
                clone.NORTH = null;
                clone.EAST = null;
                clone.SOUTH = null;
                clone.WEST = null;
                return clone;
            } catch (CloneNotSupportedException e) {
                return null;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


