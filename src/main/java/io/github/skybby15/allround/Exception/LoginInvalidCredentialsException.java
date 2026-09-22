package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;

public class LoginInvalidCredentialsException extends ApiException {
  private static String code = "INVALID_CREDENTIALS";

  public LoginInvalidCredentialsException() {
    super(
      Status.UNAUTHORIZED,
      code, 
      "Invalid email or password."
    );
  }
}
