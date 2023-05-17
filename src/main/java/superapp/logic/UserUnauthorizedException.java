package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UserUnauthorizedException extends RuntimeException{

    private static final long serialVersionUID = 372583476553713325L;
    public UserUnauthorizedException() {}
    public UserUnauthorizedException(String message) {
        super(message);
    }

    public UserUnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UserUnauthorizedException(String message, Throwable cause) {
        super(message,cause);
    }

}
