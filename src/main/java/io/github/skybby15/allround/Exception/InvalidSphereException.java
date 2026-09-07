package io.github.skybby15.allround.Exception;

public class InvalidSphereException extends ApiException {
  private static String code = "INVALID_SPHERE";

  public InvalidSphereException() {
    super(code, "Invalid sphere.");
  }

  public InvalidSphereException(String message) {
    super(code, message);
  }
    
}
