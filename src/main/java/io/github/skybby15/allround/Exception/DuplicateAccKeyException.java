package io.github.skybby15.allround.Exception;

public class DuplicateAccKeyException extends ApiException {
  public DuplicateAccKeyException() {
    super("ACCOUNT_ALREADY_EXISTS", "An account with these credentials already exists.");
  }

  public DuplicateAccKeyException(String key) {
    super(
        key.toUpperCase() + "_ALREADY_EXISTS", "An account with this " + key + " already exists.");
  }
}
