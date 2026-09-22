package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;

public class SignupExistingKeyException extends ApiException {
  public SignupExistingKeyException() {
    super(
      Status.CONFLICT,
      "ACCOUNT_ALREADY_EXISTS",
      "An account with these credentials already exists."
    );
  }

  public SignupExistingKeyException(String key) {
    super(
      Status.CONFLICT,
      key.toUpperCase() + "_ALREADY_EXISTS",
      "An account with this " + key + " already exists."
    );
  }
}
