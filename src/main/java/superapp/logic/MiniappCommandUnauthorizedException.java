package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class MiniappCommandUnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 632582410553012321L;
    public MiniappCommandUnauthorizedException() {}
    public MiniappCommandUnauthorizedException(String message) {
        super(message);
    }

    public MiniappCommandUnauthorizedException(Throwable cause) {
        super(cause);
    }

    public MiniappCommandUnauthorizedException(String message, Throwable cause) {
        super(message,cause);
    }

}
