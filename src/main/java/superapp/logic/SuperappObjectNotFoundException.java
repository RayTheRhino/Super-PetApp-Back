package superapp.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SuperappObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3158841021323247630L;

	public SuperappObjectNotFoundException() {	
	}
	public SuperappObjectNotFoundException(String message) {
		super(message);
	}
	
	public SuperappObjectNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public SuperappObjectNotFoundException(String message, Throwable cause) {
		super(message,cause);
	}
	
}


