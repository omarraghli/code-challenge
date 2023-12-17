package tanger.med.codechallenge.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The current user is not an admin")
public class UserNotAdminException extends RuntimeException{
    public UserNotAdminException(String message) {
        super(message);
    }
}
