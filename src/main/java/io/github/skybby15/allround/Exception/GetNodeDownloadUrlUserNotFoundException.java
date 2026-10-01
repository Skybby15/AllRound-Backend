package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response;

public class GetNodeDownloadUrlUserNotFoundException extends ApiException {
    private static String code = "USER_NOT_FOUND";

    public GetNodeDownloadUrlUserNotFoundException() {
        super(Response.Status.NOT_FOUND, code, "The user for this request was not found.");
    }
    
}
