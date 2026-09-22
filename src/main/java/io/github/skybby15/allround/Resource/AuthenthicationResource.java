package io.github.skybby15.allround.Resource;

import io.github.skybby15.allround.DTO.Authentication.AuthenticationResult;
import io.github.skybby15.allround.DTO.Authentication.LoginRequest;
import io.github.skybby15.allround.DTO.Authentication.LoginResponse;
import io.github.skybby15.allround.DTO.Authentication.RefreshTokenResponse;
import io.github.skybby15.allround.DTO.Authentication.SignupRequest;
import io.github.skybby15.allround.DTO.Authentication.SignupResponse;
import io.github.skybby15.allround.DTO.ErrorResponse;
import io.github.skybby15.allround.Service.AuthenticationService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenthicationResource {
  @Inject AuthenticationService authService;

  @POST
  @PermitAll
  @Path("/login")
  @APIResponse(
      responseCode = "200",
      description = "Login successful",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = LoginResponse.class)))
  @APIResponse(
      responseCode = "401",
      description = "Invalid credentials",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponse.class)))
  public Response loginUser(@Valid LoginRequest credentials) {
    AuthenticationResult result = authService.loginUser(credentials);

    LoginResponse response = new LoginResponse(result.accessToken());
    NewCookie cookie =
        new NewCookie.Builder("refreshToken")
            .value(result.refreshToken())
            .httpOnly(true)
            .secure(false) // true in production (HTTPS)
            .sameSite(NewCookie.SameSite.LAX)
            .path("/auth") // will be seen only from /auth/refresh path on browser
            .maxAge(60 * 60 * 24 * 10)
            .build();

    return Response.ok(response).cookie(cookie).build();
  }

  @POST
  @PermitAll
  @Path("/logout")
  public Response logoutUser(
      @CookieParam("refreshToken")
          String refreshToken // in case i ever want to do something with it at the end , here it is
      // the cookie
      ) {
    NewCookie expiredRefreshCookie =
        new NewCookie.Builder("refreshToken")
            .value("")
            .httpOnly(true)
            .secure(false)
            .sameSite(NewCookie.SameSite.LAX)
            .path("/auth")
            .maxAge(0)
            .build();

    return Response.noContent().cookie(expiredRefreshCookie).build();
  }

  @POST
  @PermitAll
  @Path("/signup")
  @APIResponse(
      responseCode = "200",
      description = "Signup successful",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = SignupResponse.class)))
  @APIResponse(
      responseCode = "409",
      description = "Email or username already exists",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponse.class)))
  public Response signupUser(@Valid SignupRequest credentials) {
    SignupResponse response = authService.signupUser(credentials);
    return Response.ok(response).build();
  }

  @POST
  @Path("/refresh")
  @PermitAll
  public Response refresh(@CookieParam("refreshToken") String refreshToken) {
    RefreshTokenResponse response = authService.refreshAccessToken(refreshToken);
    return Response.ok(response).build();
  }
}
