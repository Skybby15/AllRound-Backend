package io.github.skybby15.allround.Util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApplicationScoped
public class PasswordHasher {
  private static int hashIterations = 1;
  private static int hashMemory = 65536;
  private static int hashParallelism = 1;

  public String hashPassword(String password) {
    Argon2 hasher = Argon2Factory.create();
    String hashedPassword =
        hasher.hash(hashIterations, hashMemory, hashParallelism, password.toCharArray());

    return hashedPassword;
  }

  public boolean verifyHashedPassword(String hashedString, String normalPassword) {
    Argon2 argon = Argon2Factory.create();
    boolean valid = argon.verify(hashedString, normalPassword.toCharArray());
    return valid;
  }
}
