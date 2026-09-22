package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;

public class CreateSphereInvalidSphereException extends ApiException {
  private static String code = "INVALID_SPHERE";

  public CreateSphereInvalidSphereException() {
    super(Status.BAD_REQUEST, code, "Invalid sphere.");
  }

  public CreateSphereInvalidSphereException(String message) {
    super(Status.BAD_REQUEST, code, message);
  }
}
