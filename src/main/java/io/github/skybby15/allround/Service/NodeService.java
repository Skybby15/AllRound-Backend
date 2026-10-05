package io.github.skybby15.allround.Service;

import io.github.skybby15.allround.DTO.Node.Add.AddNodeRequest;
import io.github.skybby15.allround.DTO.Node.Add.AddNodeResponse;
import io.github.skybby15.allround.DTO.Node.Add.AddedNodeDTO;
import io.github.skybby15.allround.DTO.Node.Add.NodeAddDTO;
import io.github.skybby15.allround.DTO.Node.Delete.DeleteNodeResponse;
import io.github.skybby15.allround.DTO.Node.NodeDownloadUrlResponse;
import io.github.skybby15.allround.DTO.Node.NodeInfoResponse;
import io.github.skybby15.allround.DTO.Node.Tree.NodeTreeDTO;
import io.github.skybby15.allround.DTO.Node.Tree.NodeTreeResponse;
import io.github.skybby15.allround.Exception.AddNode.AddNodeParentIdNotInSphereException;
import io.github.skybby15.allround.Exception.AddNode.AddNodeSphereNotFoundException;
import io.github.skybby15.allround.Exception.AddNode.AddNodeUserMissingSphereAccess;
import io.github.skybby15.allround.Exception.AddNode.AddNodeUserNotFoundException;
import io.github.skybby15.allround.Exception.ApiException;
import io.github.skybby15.allround.Exception.DeleteNode.DeleteNodeNodeNotFoundException;
import io.github.skybby15.allround.Exception.DeleteNode.DeleteNodeUserHasNoAccessToSphere;
import io.github.skybby15.allround.Exception.DeleteNode.DeleteNodeUserNotFoundException;
import io.github.skybby15.allround.Exception.GetNode.GetNodeDownloadUrlNodeNotFoundException;
import io.github.skybby15.allround.Exception.GetNode.GetNodeDownloadUrlUserNoAccessToSphere;
import io.github.skybby15.allround.Exception.GetNode.GetNodeDownloadUrlUserNotFoundException;
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
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@ApplicationScoped
public class NodeService {
  @Inject NodeRepository nodeRepository;
  @Inject SphereRepository sphereRepository;
  @Inject UserRepository userRepository;
  @Inject FirebaseStorageUtils firebaseUtils;

  private record NodeToProcess(NodeAddDTO dto, Node parent) {}

  @Transactional
  public AddNodeResponse addNodes(AddNodeRequest request, Long userId) throws ApiException {
    User user =
        userRepository
            .findByIdOptional(userId)
            .orElseThrow(() -> new AddNodeUserNotFoundException());
    Sphere sphere =
        sphereRepository
            .findByIdOptional(request.sphereId())
            .orElseThrow(() -> new AddNodeSphereNotFoundException());

    if (!user.getId().equals(sphere.getOwner().getId())) throw new AddNodeUserMissingSphereAccess();

    List<NodeAddDTO> heads = request.nodes();
    Node parentNode;

    if (request.parentNodeId() != null)
      parentNode = nodeRepository.findById(request.parentNodeId());
    else parentNode = null;

    if (parentNode != null && !parentNode.getSphere().getId().equals(request.sphereId()))
      throw new AddNodeParentIdNotInSphereException();

    List<AddedNodeDTO> addedFileNodes = new ArrayList<>();
    Queue<NodeToProcess> queue = new ArrayDeque<>();
    queue.addAll(heads.stream().map((node) -> new NodeToProcess(node, parentNode)).toList());

    while (!queue.isEmpty()) {

      NodeToProcess current = queue.poll();

      Node entity = NodeMapper.toEntity(current.dto());
      entity.setParent(current.parent());
      entity.setSphere(sphere);

      String uploadUrl;
      if (current.dto().type() == NodeType.FILE) {
        String clientId = current.dto().fileClientId().get();
        String fileName = current.dto().name();

        entity.setStoragePath(
            "users/"
                + userId.toString()
                + "/spheres/"
                + request.sphereId().toString()
                + "/nodes/"
                + clientId
                + "/"
                + fileName);

        uploadUrl =
            firebaseUtils
                .generateUploadUrl(entity.getStoragePath(), current.dto().fileContentType().get())
                .toString();

        AddedNodeDTO addedFileNode =
            NodeMapper.toAddedFileNode(entity, uploadUrl, current.dto.fileClientId().get());
        addedFileNodes.add(addedFileNode);
      } else {
        entity.setStoragePath("");
      }

      nodeRepository.persist(entity);

      queue.addAll(
          current.dto().folderChildren().orElse(List.of()).stream()
              .map(child -> new NodeToProcess(child, entity))
              .toList());
    }

    return new AddNodeResponse(addedFileNodes);
  }

  @Transactional
  public NodeDownloadUrlResponse getNodeDownloadUrl(long nodeId, long userId) throws ApiException {
    Node node =
        nodeRepository
            .findByIdOptional(nodeId)
            .orElseThrow(() -> new GetNodeDownloadUrlNodeNotFoundException());
    User user =
        userRepository
            .findByIdOptional(userId)
            .orElseThrow(() -> new GetNodeDownloadUrlUserNotFoundException());

    if (!node.getSphere().getOwner().getId().equals(user.getId()))
      throw new GetNodeDownloadUrlUserNoAccessToSphere();

    URL url = firebaseUtils.generateDownloadUrl(node.getStoragePath());

    return new NodeDownloadUrlResponse(url.toString());
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

  public NodeInfoResponse getNodeInfo(Long nodeId) {
    Node node = nodeRepository.findByIdOptional(nodeId).get();

    NodeInfoResponse response =
        NodeInfoResponse.builder().nodeId(node.getId()).name(node.getName()).build();

    return response;
  }

  @Transactional
  public DeleteNodeResponse deleteNode(Long nodeId, Long userId) throws ApiException {
    Node node =
        nodeRepository
            .findByIdOptional(nodeId)
            .orElseThrow(() -> new DeleteNodeNodeNotFoundException()); //
    User user =
        userRepository
            .findByIdOptional(userId)
            .orElseThrow(() -> new DeleteNodeUserNotFoundException()); //

    if (!node.getSphere().getOwner().getId().equals(user.getId()))
      throw new DeleteNodeUserHasNoAccessToSphere(); //

    List<String> subtree = nodeRepository.findSubtreePathsForDeletion(nodeId);
    firebaseUtils.deleteFiles(subtree);

    nodeRepository.delete(node);

    return new DeleteNodeResponse();
  }
}
