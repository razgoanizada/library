package raz.projects.library.errors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class LibraryExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException exception) {

        var problemDetails = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());

        problemDetails.setProperty("timestamp", LocalDateTime.now());
        problemDetails.setProperty("resourceName", exception.getResourceName());
        problemDetails.setProperty("resourceId", exception.getResourceId());

        return problemDetails;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException exception) {

        var problemDetails = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

        problemDetails.setProperty("timestamp", LocalDateTime.now());
        problemDetails.setProperty("resourceName", exception.getResourceName());
        problemDetails.setProperty("resourceId", exception.getResourceId());

        return problemDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {


        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

            exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
                problemDetail.setProperty("detail", fieldError.getDefaultMessage());
                problemDetail.setProperty("Validation Failed for property", fieldError.getField());
                problemDetail.setProperty("rejectedValue", fieldError.getRejectedValue());
            });


        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.BAD_REQUEST, "Database save Failed");

        if (exception.getCause() instanceof ConstraintViolationException){

            problemDetail.setProperty("cause", "Constraint Violation");
        }

        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException exception){

        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;

    }

    @ExceptionHandler(LibraryException.class)
    public ProblemDetail handleLibraryException(LibraryException exception) {
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

}
