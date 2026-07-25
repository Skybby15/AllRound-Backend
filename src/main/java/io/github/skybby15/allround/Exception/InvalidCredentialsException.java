package io.github.skybby15.allround.Exception;

public class InvalidCredentialsException extends ApiException {
  private static String code = "INVALID_CREDENTIALS";

  public InvalidCredentialsException() {
    super(code, "Invalid email or password.");
  }

  public InvalidCredentialsException(String message) {
    super(code, message);
  }
}
