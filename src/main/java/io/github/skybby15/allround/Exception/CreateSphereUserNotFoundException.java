package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;

public class CreateSphereUserNotFoundException extends ApiException {
  private static String code = "USER_NOT_EXISTING";

  public CreateSphereUserNotFoundException() {
    super(
      Status.NOT_FOUND,
      code,
      "User does not exist."
    );
  }
}
