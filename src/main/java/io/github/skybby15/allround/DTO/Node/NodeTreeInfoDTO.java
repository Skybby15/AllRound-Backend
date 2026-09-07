package io.github.skybby15.allround.DTO.Node;

import io.github.skybby15.allround.Model.NodeType;

public record NodeTreeInfoDTO(
    Long id,
    Long parentId,
    String name,
    NodeType type
) {
    
}
