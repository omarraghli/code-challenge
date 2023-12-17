package tanger.med.codechallenge.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The file is empty")
public class FileIsEmptyException extends RuntimeException{
    public FileIsEmptyException(String message) {
        super(message);
    }
}
