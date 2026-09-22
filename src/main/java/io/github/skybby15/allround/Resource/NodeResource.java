package io.github.skybby15.allround.Resource;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.github.skybby15.allround.DTO.Node.AddNodeRequest;
import io.github.skybby15.allround.DTO.Node.AddNodeResponse;
import io.github.skybby15.allround.DTO.Node.NodeInfoResponse;
import io.github.skybby15.allround.Service.NodeService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/node")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NodeResource {
  @Inject NodeService nodeService;
  @Inject JsonWebToken jwt;

  @GET
  @Path("/{id}")
  @Authenticated
  public Response getNodeData(@PathParam("id") Long nodeId) {
    NodeInfoResponse response = nodeService.getNodeInfo(nodeId);

    return Response.ok().entity(response).build();
  }

  @POST
  @Authenticated
  public Response createNode(AddNodeRequest request) {
    Long userId = Long.valueOf(jwt.getSubject());
    AddNodeResponse response = nodeService.addNodes(request,userId);

    return Response.ok().status(Status.CREATED).entity(response).build();
  } 

  @GET 
  @Path("/{id}/downloadUrl")
  @Authenticated 
  public Response getNodeDownloadUrl(@PathParam("id") long nodeId)
  {
    return Response.ok().build();
  }
}
