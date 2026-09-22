package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;

public class AddNodeSphereNotFoundException extends ApiException {
  private static String code = "SPHERE_NOT_FOUND";

  public AddNodeSphereNotFoundException() {
    super(Status.NOT_FOUND, code, "The sphere was not found");
  }

  public AddNodeSphereNotFoundException(String message) {
    super(Status.NOT_FOUND, code, message);
  }
}
