package io.github.skybby15.allround.Resource;

import java.time.Instant;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import io.github.skybby15.allround.DTO.ErrorResponse;
import io.github.skybby15.allround.DTO.Filters.SphereFilter;
import io.github.skybby15.allround.DTO.Node.NodeTreeResponse;
import io.github.skybby15.allround.DTO.Sphere.CreateSphereRequest;
import io.github.skybby15.allround.DTO.Sphere.CreateSphereResponse;
import io.github.skybby15.allround.DTO.Sphere.ListSphereResponse;
import io.github.skybby15.allround.Exception.ApiException;
import io.github.skybby15.allround.Exception.InvalidSphereException;
import io.github.skybby15.allround.Exception.UserNotExistingException;
import io.github.skybby15.allround.Service.NodeService;
import io.github.skybby15.allround.Service.SphereService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sphere")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SphereResource {
    @Inject SphereService sphereService;
    @Inject NodeService nodeService;
    @Inject JsonWebToken jwt;
    

    @POST
    @Authenticated 
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
      responseCode = "201",
      description = "Sphere created successfully",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = CreateSphereResponse.class)))
    public Response createSphere(CreateSphereRequest request) {
        try{
            Long userId = Long.valueOf(jwt.getSubject());

            CreateSphereResponse response = sphereService.createSphere(request, userId);
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (UserNotExistingException e) {
            ErrorResponse  errorResponse = new ErrorResponse(e.getCode(), e.getMessage(), Instant.now());
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        } catch (InvalidSphereException e) {
            ErrorResponse  errorResponse = new ErrorResponse(e.getCode(), e.getMessage(), Instant.now());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        } catch (ApiException e) {
            ErrorResponse  errorResponse = new ErrorResponse(e.getCode(), e.getMessage(), Instant.now());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @GET 
    @Authenticated
    @APIResponse(
      responseCode = "200",
      description = "Spheres retrieved successfully",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ListSphereResponse.class)))
    public Response getSpheres(@QueryParam("ownerId") Long ownerId) {
        SphereFilter filter = SphereFilter.builder()
            .ownerId(ownerId)
            .build();
        
        ListSphereResponse response = sphereService.getSpheresFiltered(filter);

        return Response.ok().entity(response).build();
    }

    @GET 
    @Path ("/{id}/node-tree")
    @Authenticated 
    @APIResponse(
      responseCode = "200",
      description = "Node tree retrieved successfully",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = NodeTreeResponse.class)))
    public Response getNodeTreeBySphereId(@PathParam("id") Long sphereId) {
        NodeTreeResponse response = nodeService.getNodeTree(sphereId);
        return Response.ok().entity(response).build();
    }
}
