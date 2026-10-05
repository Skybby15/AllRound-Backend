package io.github.skybby15.allround.Exception.AddNode;

import io.github.skybby15.allround.Exception.ApiException;
import jakarta.ws.rs.core.Response.Status;

public class AddNodeParentIdNotInSphereException extends ApiException {
  private static String code = "PARENT_NOT_IN_SPHERE";

  public AddNodeParentIdNotInSphereException() {
    super(Status.FORBIDDEN, code, "The parent id was not found in sphere");
  }

  public AddNodeParentIdNotInSphereException(String message) {
    super(Status.FORBIDDEN, code, message);
  }
}
