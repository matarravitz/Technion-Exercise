/**
 *
 * This exception is thrown when an attempt is made to add node
 * to another node direction that is already occupied by another node.
 */
public class DirectionIsOccupied extends RuntimeException{
    public DirectionIsOccupied(){}
    public DirectionIsOccupied(String message){
        super(message);
    }
    public DirectionIsOccupied(String message, Throwable cause){
        super(message, cause);
    }
}