package app.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private class JsonResponse {
        String message;

        public JsonResponse() {
        }

        public JsonResponse(String message) {
            super();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    //@ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<JsonResponse> handleException(RuntimeException e) {
        String message = e.getMessage();
        if (message == null) {
            message = e.toString();
        }
        return new ResponseEntity<JsonResponse>(new JsonResponse(message), HttpStatus.BAD_REQUEST);
    }

}