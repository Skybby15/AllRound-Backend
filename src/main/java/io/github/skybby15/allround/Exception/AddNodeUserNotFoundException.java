package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;;

public class AddNodeUserNotFoundException extends ApiException {
  private static String code = "USER_NOT_FOUND";

  public AddNodeUserNotFoundException() {
    super(Status.NOT_FOUND, code, "The user was not found");
  }

  public AddNodeUserNotFoundException(String message) {
    super(Status.NOT_FOUND, code, message);
  }
}
