/**
 *
 * This exception is thrown when an attempt is made to add room
 * to another room direction that is already occupied by another room.
 */
public class ExitIsOccupied extends RuntimeException{
    public ExitIsOccupied(){}
    public ExitIsOccupied(String message){
        super(message);
    }
    public ExitIsOccupied(String message, Throwable cause){
        super(message, cause);
    }
}
