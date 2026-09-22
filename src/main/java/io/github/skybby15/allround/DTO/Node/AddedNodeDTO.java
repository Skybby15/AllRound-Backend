package io.github.skybby15.allround.DTO.Node;

import java.util.Optional;

import io.github.skybby15.allround.Model.NodeType;
import lombok.Builder;

@Builder 
public record AddedNodeDTO(
    Long id,
    Long parentId,
    String name,
    NodeType type,

    Optional<String> uploadURL,
    Optional<Long> fileSize
) {
    
}
