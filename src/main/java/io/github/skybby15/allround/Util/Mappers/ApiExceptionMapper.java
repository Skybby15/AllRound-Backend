package io.github.skybby15.allround.Util.Mappers;

import java.time.Instant;

import io.github.skybby15.allround.DTO.ErrorResponse;
import io.github.skybby15.allround.Exception.ApiException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider 
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException e) {
        ErrorResponse error = new ErrorResponse(
            e.getCode(),
            e.getMessage(),
            Instant.now()
        );

        return Response.status(e.getStatus())
            .entity(error)
            .build();
    }
    
}
