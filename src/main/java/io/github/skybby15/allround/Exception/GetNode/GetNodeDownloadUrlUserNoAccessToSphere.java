package io.github.skybby15.allround.Exception.GetNode;

import io.github.skybby15.allround.Exception.ApiException;
import jakarta.ws.rs.core.Response;

public class GetNodeDownloadUrlUserNoAccessToSphere extends ApiException {
  private static String code = "USER_FORBIDDEN_FROM_SPHERE";

  public GetNodeDownloadUrlUserNoAccessToSphere() {
    super(Response.Status.FORBIDDEN, code, "User does not own the sphere this node is in.");
  }
}
