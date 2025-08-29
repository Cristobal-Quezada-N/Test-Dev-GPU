package usach.hackaton.gpu.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  // alredy Registered Email: 409
  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  public ResponseEntity<Map<String,Object>> handleEmailTaken(EmailAlreadyRegisteredException ex) {
    Map<String,Object> body = new HashMap<>();
    body.put("message", ex.getMessage());
    body.put("code", "EMAIL_TAKEN");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  // Generic Handler: 400
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String,Object>> handleRuntime(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(Map.of("message", ex.getMessage()));
  }
}
