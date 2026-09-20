package io.github.skybby15.allround.Resource;

import io.github.skybby15.allround.Service.FirebaseStorageService;
import io.github.skybby15.allround.Service.NodeService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/node")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NodeResource {
  @Inject FirebaseStorageService firebaseStorageService;
  @Inject NodeService nodeService;

  @GET
  @Path("/{id}/downloadUrl")
  @PermitAll
  public Response generateDownloadUrl(@PathParam("id") String storagePath) {
    String response = firebaseStorageService.generateDownloadUrl(storagePath).toString();

    return Response.ok().entity(response).build();
  }

  @POST
  @Authenticated
  public Response createNode() {
    return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  @Authenticated
  public Response getNodeData(@PathParam("id") Long nodeId) {
    return Response.ok().build();
  }
}
