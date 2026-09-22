package io.github.skybby15.allround.Util.Mappers;

import java.util.Optional;

import io.github.skybby15.allround.DTO.Node.AddedNodeDTO;
import io.github.skybby15.allround.DTO.Node.NodeAddDTO;
import io.github.skybby15.allround.Model.Node;

public final class NodeMapper {
    
    public static Node toEntity(NodeAddDTO addDto){
        Node mapped = Node.builder()
            .name(addDto.name())
            .type(addDto.type())
            .build();

        return mapped;
    }

    public static AddedNodeDTO toAddedNode(Node entity,Optional<String> uploadUrl)
    {
        AddedNodeDTO mapped = AddedNodeDTO.builder()
            .id(entity.getId())
            .parentId(entity.getParent().getId())
            .name(entity.getName())
            .type(entity.getType())
            .uploadURL(uploadUrl)
            .build();

        return mapped;
    }
}
