package io.github.skybby15.allround.Service;

import io.github.skybby15.allround.DTO.Node.NodeTreeInfoDTO;
import io.github.skybby15.allround.DTO.Node.NodeTreeResponse;
import io.github.skybby15.allround.Repository.NodeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped 
public class NodeService {
    @Inject NodeRepository nodeRepository;

    public NodeTreeResponse getNodeTree(Long sphereId) {
        nodeRepository.findBySphereId(sphereId);

        NodeTreeResponse response = NodeTreeResponse.builder()
            .nodes(nodeRepository.findBySphereId(sphereId).stream().map(node -> new NodeTreeInfoDTO(
                node.getId(),
                node.getParent() != null ? node.getParent().getId() : null,
                node.getName(),
                node.getType()
            )).toList())
            .build();

        return response;
    }
    
}
