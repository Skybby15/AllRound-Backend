package io.github.skybby15.allround.DTO.Node.Add;

import io.github.skybby15.allround.Model.NodeType;
import java.util.List;
import java.util.Optional;

public record NodeAddDTO(
    String name,
    NodeType type,
    Optional<String> fileClientId,
    Optional<Long> fileSize,
    Optional<String> fileContentType,
    Optional<List<NodeAddDTO>> folderChildren) {}
