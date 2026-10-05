package io.github.skybby15.allround.DTO.Node.Delete;

import io.github.skybby15.allround.Model.NodeType;

public record NodeDeleteInfo(
    Long id,
    NodeType type,
    String storagePath
) {}