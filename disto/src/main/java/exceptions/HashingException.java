package exceptions;

public class HashingException extends Exception{
    public HashingException(String message, Throwable cause) {
        super(message, cause);
    }
    public HashingException(String message) {
        super(message);
    }
}
