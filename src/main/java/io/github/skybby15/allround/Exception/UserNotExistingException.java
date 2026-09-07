package io.github.skybby15.allround.Exception;

public class UserNotExistingException extends ApiException {
  private static String code = "USER_NOT_EXISTING";

  public UserNotExistingException() {
    super(code, "User does not exist.");
  }

  public UserNotExistingException(String message) {
    super(code, message);
  }
}
