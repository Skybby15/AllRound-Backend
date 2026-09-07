package io.github.skybby15.allround.Resource;

import io.github.skybby15.allround.Service.FirebaseStorageService;
import io.github.skybby15.allround.Service.NodeService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path ("/node")
@Consumes (MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class NodeResource {
    @Inject FirebaseStorageService firebaseStorageService;
    @Inject NodeService nodeService;

    @GET 
    @Path ("/download/{id}")
    @PermitAll 
    public Response generateDownloadUrl(@PathParam("id") String storagePath) {
        return Response.ok().entity(firebaseStorageService.generateDownloadUrl(storagePath).toString()).build();
    }
}
