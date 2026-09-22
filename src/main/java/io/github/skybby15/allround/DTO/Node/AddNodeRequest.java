package io.github.skybby15.allround.DTO.Node;

import java.util.List;

public record AddNodeRequest(
    Long sphereId,
    Long parentNodeId,
    List<NodeAddDTO> nodes
) {
    
}
