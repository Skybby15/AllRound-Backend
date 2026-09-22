package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response.Status;

public class RefreshInvalidTokenException extends ApiException {
    public RefreshInvalidTokenException() {
    super(
      Status.CONFLICT,
      "REFRESH_TOKEN_INVALID",
      "The refresh token is invalid"
    );
  }
}
