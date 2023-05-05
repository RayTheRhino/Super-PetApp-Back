package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SuperappObjectBadRequestException extends RuntimeException{

    private static final long serialVersionUID = 982423240587685371L;
    public SuperappObjectBadRequestException() {
    }
    public SuperappObjectBadRequestException(String message) {
        super(message);
    }

    public SuperappObjectBadRequestException(Throwable cause) {
        super(cause);
    }

    public SuperappObjectBadRequestException(String message, Throwable cause) {
        super(message,cause);
    }

}
