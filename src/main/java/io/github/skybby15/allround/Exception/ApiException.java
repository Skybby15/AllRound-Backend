package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
  private Status status;
  private String code;

  protected ApiException(
      Status status,
      String code,
      String message
    ) {
      super(message);
      this.code = code;
      this.status = status;
    }
}
