package raz.projects.library.errors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
@ToString
public class ResourceNotFoundException extends LibraryException{

    private String resourceName;
    private String resourceId;
    private String message;

    public ResourceNotFoundException(String resourceName, String resourceId, String message) {
        super("%s with id = %s %s".formatted(resourceName, resourceId, message));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.message = message;
    }


    public ResourceNotFoundException(String resourceName, long resourceId, String message) {
        this(resourceName, String.valueOf(resourceId), message);
    }


    public ResourceNotFoundException(String resourceName, long resourceId) {
        this(resourceName, String.valueOf(resourceId), "Not Found");
    }


    public ResourceNotFoundException(long resourceId) {
        this("Resource", String.valueOf(resourceId), "Not Found");
    }
}

