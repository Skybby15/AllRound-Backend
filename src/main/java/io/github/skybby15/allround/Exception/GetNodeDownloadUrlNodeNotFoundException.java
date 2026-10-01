package io.github.skybby15.allround.Exception;

import jakarta.ws.rs.core.Response;

public class GetNodeDownloadUrlNodeNotFoundException extends ApiException {
    private static String code = "NODE_NOT_FOUND";

    public GetNodeDownloadUrlNodeNotFoundException() {
        super(Response.Status.NOT_FOUND, code, "The node requested to download was not found.");
    }
    
}
