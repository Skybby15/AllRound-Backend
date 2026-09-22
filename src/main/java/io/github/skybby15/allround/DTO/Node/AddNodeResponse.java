package io.github.skybby15.allround.DTO.Node;

import java.util.List;

public record AddNodeResponse(
    List<AddedNodeDTO> nodes
) {
}
