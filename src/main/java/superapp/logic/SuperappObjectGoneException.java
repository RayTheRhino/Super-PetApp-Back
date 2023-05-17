package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class SuperappObjectGoneException extends RuntimeException{

    private static final long serialVersionUID = 372583476553713325L;
    public SuperappObjectGoneException() {}
    public SuperappObjectGoneException(String message) {
        super(message);
    }

    public SuperappObjectGoneException(Throwable cause) {
        super(cause);
    }

    public SuperappObjectGoneException(String message, Throwable cause) {
        super(message,cause);
    }

}
