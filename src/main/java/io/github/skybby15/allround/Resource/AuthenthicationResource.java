package io.github.skybby15.allround.Resource;

import io.github.skybby15.allround.DTO.Authentication.LoginRequest;
import io.github.skybby15.allround.DTO.Authentication.LoginResponse;
import io.github.skybby15.allround.DTO.Authentication.SignupRequest;
import io.github.skybby15.allround.DTO.Authentication.SignupResponse;
import io.github.skybby15.allround.Service.AuthenticationService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenthicationResource {
  @Inject AuthenticationService authService;

  @POST
  @PermitAll
  @Path("/login")
  public Response loginUser(@Valid LoginRequest credentials) {
    LoginResponse response = authService.loginUser(credentials);

    return Response.ok(response).build();
  }

  @POST
  @PermitAll
  @Path("/signup")
  public Response signupUser(@Valid SignupRequest credentials) {
    SignupResponse response = authService.signupUser(credentials);

    return Response.ok(response).build();
  }
}
