package io.github.skybby15.allround.Exception.DeleteNode;

import io.github.skybby15.allround.Exception.ApiException;
import jakarta.ws.rs.core.Response;

public class DeleteNodeNodeNotFoundException extends ApiException {
    static final String code = "NODE_NOT_FOUND";

    public DeleteNodeNodeNotFoundException() {
        super(Response.Status.NOT_FOUND, code, "The node to delete was not found");
    }
    
}
