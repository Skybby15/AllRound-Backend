package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response;

public class DeleteNodeUserNotFoundException extends ApiException {
    private static String code = "USER_NOT_FOUND";

    public DeleteNodeUserNotFoundException() {
        super(Response.Status.NOT_FOUND, code, "The user for this request was not found.");
    }
}
