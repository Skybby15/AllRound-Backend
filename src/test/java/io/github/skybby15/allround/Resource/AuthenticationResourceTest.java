package io.github.skybby15.allround.Resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.skybby15.allround.DTO.Authentication.LoginRequest;
import io.github.skybby15.allround.DTO.Authentication.SignupRequest;
import io.github.skybby15.allround.Model.User;
import io.github.skybby15.allround.Repository.UserRepository;
import io.github.skybby15.allround.Util.PasswordHasher;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class AuthenticationResourceTest {

  @Inject UserRepository userRepository;
  @Inject PasswordHasher hasher;

  @Transactional
  User createTestUser() {
    String hashedPassword = hasher.hashPassword("password1");

    User user =
        User.builder()
            .username("user1")
            .email("user1@email.com")
            .passwordHash(hashedPassword)
            .build();

    userRepository.persist(user);
    return user;
  }

  @Transactional
  @BeforeEach
  void clearUsers() {
    userRepository.deleteAll();
  }

  @Nested
  class Login {
    @Test
    void shouldLoginSuccesfully() {
      User user = createTestUser();
      LoginRequest request = new LoginRequest(user.getEmail(), "password1");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/login")
          .then()
          .statusCode(200)
          .body("accessToken", notNullValue())
          .header("Set-Cookie", containsString("refreshToken="));
      ;
    }

    @Test
    void badEmailLogin() {
      createTestUser();
      LoginRequest request = new LoginRequest("not@goodemail.com", "password1");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/login")
          .then()
          .statusCode(401)
          .body("code", equalTo("INVALID_CREDENTIALS"));
    }

    @Test
    void invalidEmailLogin() {
      createTestUser();
      LoginRequest request = new LoginRequest("notagoodemailformat", "password1");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/login")
          .then()
          .statusCode(400);
    }

    @Test
    void badPasswordLogin() {
      User user = createTestUser();
      LoginRequest request = new LoginRequest(user.getEmail(), "wrongpassword");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/login")
          .then()
          .statusCode(401)
          .body("code", equalTo("INVALID_CREDENTIALS"));
    }

    @Test
    void invalidPasswordLogin() {
      User user = createTestUser();
      LoginRequest request = new LoginRequest(user.getEmail(), "");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/login")
          .then()
          .statusCode(400);
    }
  }

  @Nested
  class Signup {

    @Test
    void shouldSignupSuccesfully() {
      long initialCount = userRepository.count();
      SignupRequest request = new SignupRequest("johnny", "johnny@test.com", "Password123");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/signup")
          .then()
          .statusCode(200);

      assertNotEquals(initialCount, userRepository.count());

      User user = userRepository.findByEmail("johnny@test.com").get();

      assertNotNull(user);
      assertEquals("johnny", user.getUsername());
      assertNotEquals("Password123", user.getPasswordHash());
    }

    @Test
    void duplicateEmailKeySignup() {
      User created = createTestUser();
      long initialCount = userRepository.count();
      SignupRequest request = new SignupRequest("user2", created.getEmail(), "password2");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/signup")
          .then()
          .statusCode(409);

      assertEquals(initialCount, userRepository.count());
    }

    @Test
    void duplicateUsernameKeySignup() {
      User created = createTestUser();
      long initialCount = userRepository.count();
      SignupRequest request =
          new SignupRequest(created.getUsername(), "user2@email.com", "password2");

      given()
          .contentType(ContentType.JSON)
          .body(request)
          .when()
          .post("/auth/signup")
          .then()
          .statusCode(409);

      assertEquals(initialCount, userRepository.count());
    }
  }

  @Nested
  class RefreshToken {

    @Test
    void getNewAccessTokenSuccess() {
      User user = createTestUser();
      LoginRequest request = new LoginRequest(user.getEmail(), "password1");

      Response loginResponse =
          given().contentType(ContentType.JSON).body(request).when().post("/auth/login");

      String refreshToken = loginResponse.getCookie("refreshToken");

      assertNotNull(refreshToken);

      given()
          .contentType(ContentType.JSON)
          .cookie("refreshToken", refreshToken)
          .when()
          .post("/auth/refresh")
          .then()
          .statusCode(200);
    }

    @Test
    void noTokenRefreshRequest() {
      given().contentType(ContentType.JSON).when().post("/auth/refresh").then().statusCode(401);
    }

    @Test
    void invalidTokenRefreshRequest() {

      given()
          .contentType(ContentType.JSON)
          .cookie("refreshToken", "not-a-valid-refresh-token")
          .when()
          .post("/auth/refresh")
          .then()
          .statusCode(401);
    }

    @Test
    void expiredTokenRefreshRequest() {
      User user = createTestUser();

      String refreshToken =
          Jwt.issuer("allround-auth")
              .subject(user.getId().toString())
              .upn(user.getEmail())
              .claim("username", user.getUsername())
              .claim("type", "access")
              .expiresIn(Instant.now().minusSeconds(60L).getEpochSecond())
              .sign();
      ;

      given()
          .contentType(ContentType.JSON)
          .cookie("refreshToken", refreshToken)
          .when()
          .post("/auth/refresh")
          .then()
          .statusCode(401);
    }
  }
}
