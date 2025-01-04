/**
 *
 * This exception is thrown when an attempt is made to add room
 * to another room that does not exist.
 */
public class RoomDoesNotExist extends RuntimeException {
    public RoomDoesNotExist(){}
    public RoomDoesNotExist(String message){
        super(message);
    }
    public RoomDoesNotExist(String message, Throwable cause){
        super(message, cause);
    }
}
