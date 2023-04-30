package superapp.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MiniappCommandNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 647764193533676544L;

	public MiniappCommandNotFoundException() {	
	}
	public MiniappCommandNotFoundException(String message) {
		super(message);
	}
	
	public MiniappCommandNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public MiniappCommandNotFoundException(String message, Throwable cause) {
		super(message,cause);
	}
	
}
