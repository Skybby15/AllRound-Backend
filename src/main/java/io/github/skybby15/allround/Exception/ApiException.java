package io.github.skybby15.allround.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends Exception {
  private String code;

  public ApiException(String message) {
    super(message);
  }

  public ApiException(String code, String message) {
    super(message);
    this.code = code;
  }
}
