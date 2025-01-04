/**
 *
 * This exception is thrown when an attempt is made to add node
 * to another node that does not exist.
 */
public class NoSuchElement extends RuntimeException{
    public NoSuchElement(){}
    public NoSuchElement(String message){
        super(message);
    }
    public NoSuchElement(String message, Throwable cause){
        super(message, cause);
    }
}
