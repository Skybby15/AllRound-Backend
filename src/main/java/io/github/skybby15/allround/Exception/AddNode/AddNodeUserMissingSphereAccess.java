package io.github.skybby15.allround.Exception.AddNode;

import io.github.skybby15.allround.Exception.ApiException;
import jakarta.ws.rs.core.Response;

public class AddNodeUserMissingSphereAccess extends ApiException {
    private static String code = "USER_NO_SPHERE_ACCESS";

    public AddNodeUserMissingSphereAccess(){
        super(
            Response.Status.FORBIDDEN,
            code,
            "User doesn't have access to this sphere"
        );
    }

    public AddNodeUserMissingSphereAccess(String message) {
    super(Response.Status.FORBIDDEN, code, message);
  }
}
