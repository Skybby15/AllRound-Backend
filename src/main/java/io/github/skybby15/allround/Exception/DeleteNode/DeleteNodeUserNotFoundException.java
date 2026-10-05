package io.github.skybby15.allround.Exception.DeleteNode;

import io.github.skybby15.allround.Exception.ApiException;
import jakarta.ws.rs.core.Response;

public class DeleteNodeUserNotFoundException extends ApiException {
    private static String code = "USER_NOT_FOUND";

    public DeleteNodeUserNotFoundException() {
        super(Response.Status.NOT_FOUND, code, "The user for this request was not found.");
    }
}
