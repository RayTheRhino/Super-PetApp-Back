package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class UserGoneException extends RuntimeException{
    private static final long serialVersionUID = 502202435553242525L;
    public UserGoneException() {}
    public UserGoneException(String message) {
        super(message);
    }

    public UserGoneException(Throwable cause) {
        super(cause);
    }

    public UserGoneException(String message, Throwable cause) {
        super(message,cause);
    }

}
