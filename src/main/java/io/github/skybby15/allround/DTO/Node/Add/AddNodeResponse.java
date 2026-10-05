package io.github.skybby15.allround.DTO.Node.Add;

import java.util.List;

public record AddNodeResponse(
    List<AddedNodeDTO> nodes
) {
}
