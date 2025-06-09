package kristar.projects.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    private final LocalDateTime timestamp;

    @Getter
    @Setter
    private HttpStatus httpStatus;

    @Getter
    @Setter
    private String error;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private List<String> listErrorsValidation;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus httpStatus, String error, String message) {
        this();
        this.httpStatus = httpStatus;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(HttpStatus httpStatus, String error, String message,
                         List<String> listErrorsValidation) {
        this(httpStatus, error, message);
        this.listErrorsValidation = listErrorsValidation;
    }
}
