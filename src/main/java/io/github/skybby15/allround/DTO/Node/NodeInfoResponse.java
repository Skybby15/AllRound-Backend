package io.github.skybby15.allround.DTO.Node;

import lombok.Builder;

@Builder 
public record NodeInfoResponse(
    long nodeId,
    String name
) {
    
}
