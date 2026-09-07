package io.github.skybby15.allround.DTO.Node;

import java.util.List;

import lombok.Builder;


@Builder  
public record NodeTreeResponse(
    List<NodeTreeInfoDTO> nodes
) {
    
}
