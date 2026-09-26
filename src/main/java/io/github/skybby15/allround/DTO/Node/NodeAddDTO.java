package io.github.skybby15.allround.DTO.Node;

import java.util.List;
import java.util.Optional;

import io.github.skybby15.allround.Model.NodeType;

public record NodeAddDTO(
    String name,
    NodeType type,
    
    Optional<String> fileClientId,
    Optional<Long> fileSize,
    Optional<String> fileContentType,

    Optional<List<NodeAddDTO>> folderChildren
) {
    
}
