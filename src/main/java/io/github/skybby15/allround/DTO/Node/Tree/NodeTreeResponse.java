package io.github.skybby15.allround.DTO.Node.Tree;

import java.util.List;
import lombok.Builder;

@Builder
public record NodeTreeResponse(List<NodeTreeDTO> nodes) {}
