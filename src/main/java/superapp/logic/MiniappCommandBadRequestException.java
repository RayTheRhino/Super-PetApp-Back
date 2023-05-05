package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MiniappCommandBadRequestException extends RuntimeException{

    private static final long serialVersionUID = -1924485300405749371L;
    public MiniappCommandBadRequestException() {
    }
    public MiniappCommandBadRequestException(String message) {
        super(message);
    }

    public MiniappCommandBadRequestException(Throwable cause) {
        super(cause);
    }

    public MiniappCommandBadRequestException(String message, Throwable cause) {
        super(message,cause);
    }

}
