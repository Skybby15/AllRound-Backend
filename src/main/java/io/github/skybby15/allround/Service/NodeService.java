package io.github.skybby15.allround.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import io.github.skybby15.allround.DTO.Node.AddNodeRequest;
import io.github.skybby15.allround.DTO.Node.AddNodeResponse;
import io.github.skybby15.allround.DTO.Node.AddedNodeDTO;
import io.github.skybby15.allround.DTO.Node.NodeAddDTO;
import io.github.skybby15.allround.DTO.Node.NodeInfoResponse;
import io.github.skybby15.allround.DTO.Node.NodeTreeDTO;
import io.github.skybby15.allround.DTO.Node.NodeTreeResponse;
import io.github.skybby15.allround.Exception.AddNodeParentIdNotInSphereException;
import io.github.skybby15.allround.Exception.AddNodeSphereNotFoundException;
import io.github.skybby15.allround.Exception.AddNodeUserMissingSphereAccess;
import io.github.skybby15.allround.Exception.AddNodeUserNotFoundException;
import io.github.skybby15.allround.Exception.ApiException;
import io.github.skybby15.allround.Model.Node;
import io.github.skybby15.allround.Model.NodeType;
import io.github.skybby15.allround.Model.Sphere;
import io.github.skybby15.allround.Model.User;
import io.github.skybby15.allround.Repository.NodeRepository;
import io.github.skybby15.allround.Repository.SphereRepository;
import io.github.skybby15.allround.Repository.UserRepository;
import io.github.skybby15.allround.Util.FirebaseStorageUtils;
import io.github.skybby15.allround.Util.Mappers.NodeMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class NodeService {
  @Inject NodeRepository nodeRepository;
  @Inject SphereRepository sphereRepository;
  @Inject UserRepository userRepository;
  @Inject FirebaseStorageUtils firebaseUtils;

  private record NodeToProcess(
      NodeAddDTO dto,
      Node parent
  ) {}

  @Transactional 
  public AddNodeResponse addNodes(AddNodeRequest request, Long userId) throws ApiException{
    User user = userRepository.findByIdOptional(userId).orElseThrow(() -> new AddNodeUserNotFoundException());
    Sphere sphere = sphereRepository.findByIdOptional(request.sphereId()).orElseThrow(() -> new AddNodeSphereNotFoundException());

    if(!user.getId().equals(sphere.getOwner().getId()))
        throw new AddNodeUserMissingSphereAccess();

    List<NodeAddDTO> heads = request.nodes();
    Node parentNode;

    if(request.parentNodeId() != null)
        parentNode = nodeRepository.findById(request.parentNodeId());
    else
        parentNode = null;

    if(parentNode != null && !parentNode.getSphere().getId().equals(request.sphereId()))
        throw new AddNodeParentIdNotInSphereException();

    List<AddedNodeDTO> addedFileNodes = new ArrayList<>();
    Queue<NodeToProcess> queue = new ArrayDeque<>();
    queue.addAll(
        heads.stream()
        .map((node) -> new NodeToProcess(node,parentNode))
        .toList()
    );

    while (!queue.isEmpty()) {

        NodeToProcess current = queue.poll();

        Node entity = NodeMapper.toEntity(current.dto());
        entity.setParent(current.parent());
        entity.setSphere(sphere);

        String uploadUrl;
        if(current.dto().type() == NodeType.FILE)
        {
            String clientId = current.dto().fileClientId().get();
            String fileName = current.dto().name();

            entity.setStoragePath(
                "users/"+ userId.toString() + 
                "/spheres/"+ request.sphereId().toString() + 
                "/nodes/" + clientId  + "/" + fileName
            );

            uploadUrl = 
                firebaseUtils.generateUploadUrl(
                    entity.getStoragePath(),
                    current.dto().fileContentType().get()
                )
                .toString();
            

            AddedNodeDTO addedFileNode = NodeMapper.toAddedFileNode(entity,uploadUrl,current.dto.fileClientId().get()); 
            addedFileNodes.add(addedFileNode);
        }
        else
        {
            entity.setStoragePath("");
        }

        nodeRepository.persist(entity);

        queue.addAll(
            current.dto().folderChildren()
                .orElse(List.of())
                .stream()
                .map(child -> new NodeToProcess(child, entity))
                .toList()
        );
    }

    return new AddNodeResponse(addedFileNodes);
  }

  public NodeTreeResponse getNodeTree(Long sphereId) {
    NodeTreeResponse response =
        NodeTreeResponse.builder()
            .nodes(
                nodeRepository.findBySphereId(sphereId).stream()
                    .map(
                        node ->
                            new NodeTreeDTO(
                                node.getId(),
                                node.getParent() != null ? node.getParent().getId() : null,
                                node.getName(),
                                node.getType()))
                    .toList())
            .build();

    return response;
  }

  public NodeInfoResponse getNodeInfo(Long nodeId)
  {
    Node node = nodeRepository.findByIdOptional(nodeId).get();

    NodeInfoResponse response = 
        NodeInfoResponse.builder()
        .nodeId(node.getId())
        .name(node.getName())
        .build();

    return response;
  }
}
