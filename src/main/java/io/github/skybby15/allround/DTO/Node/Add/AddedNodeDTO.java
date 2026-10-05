package io.github.skybby15.allround.DTO.Node.Add;

import io.github.skybby15.allround.Model.NodeType;
import lombok.Builder;

@Builder 
public record AddedNodeDTO(
    Long id,

    Long parentId,
    String name,
    NodeType type,

    String clientId,
    String uploadURL
) {
    
}
