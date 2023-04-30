package superapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserBadRequestException extends RuntimeException{

    private static final long serialVersionUID = -2184262340187181321L;
    public UserBadRequestException() {
    }
    public UserBadRequestException(String message) {
        super(message);
    }

    public UserBadRequestException(Throwable cause) {
        super(cause);
    }

    public UserBadRequestException(String message, Throwable cause) {
        super(message,cause);
    }

}
