package io.github.skybby15.allround.Util.Mappers;

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

    public static AddedNodeDTO toAddedFileNode(Node entity,String uploadUrl,String clientId)
    {
        Long parentId;

        if(entity.getParent() != null)
            parentId = entity.getParent().getId();
        else
            parentId = null;

        AddedNodeDTO mapped = AddedNodeDTO.builder()
            .id(entity.getId())
            .parentId(parentId)
            .name(entity.getName())
            .type(entity.getType())
            .clientId(clientId)
            .uploadURL(uploadUrl)
            .build();

        return mapped;
    }
}
