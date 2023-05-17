package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class SuperappObjectUnauthorizedException extends RuntimeException{

    private static final long serialVersionUID = 172523470583615371L;
    public SuperappObjectUnauthorizedException() {}
    public SuperappObjectUnauthorizedException(String message) {
        super(message);
    }

    public SuperappObjectUnauthorizedException(Throwable cause) {
        super(cause);
    }

    public SuperappObjectUnauthorizedException(String message, Throwable cause) {
        super(message,cause);
    }

}
