package core.exception;

public class ApplicationException extends RuntimeException {    
    public ApplicationException() {}
    public ApplicationException(String msg) {super(msg);}
    public ApplicationException(Exception e) {super(e);}    
}
