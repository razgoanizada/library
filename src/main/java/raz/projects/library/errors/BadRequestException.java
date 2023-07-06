package raz.projects.library.errors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
@ToString
public class BadRequestException extends LibraryException{

    private String resourceName;
    private String resourceId;
    private String message;

    public BadRequestException(String resourceName, String resourceId, String message) {
        super("%s with id = %s %s".formatted(resourceName, resourceId, message));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.message = message;
    }


    public BadRequestException(String resourceName, long resourceId, String message) {
        this(resourceName, String.valueOf(resourceId), message);
    }


    public BadRequestException(String resourceName, long resourceId) {
        this(resourceName, String.valueOf(resourceId), "Not Found");
    }

    public BadRequestException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
        this.message = message;
    }

    public BadRequestException(long resourceId) {
        this("Resource", String.valueOf(resourceId), "Not Found");
    }
}
