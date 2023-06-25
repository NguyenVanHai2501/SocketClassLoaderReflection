package exception;

public class InputNotSupportException extends Exception {
    public InputNotSupportException(String message) throws Exception {
        throw new Exception(message);
    }
}
