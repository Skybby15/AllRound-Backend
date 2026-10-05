package io.github.skybby15.allround.Repository;

import io.github.skybby15.allround.Model.Node;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NodeRepository implements PanacheRepository<Node> {
  public List<Node> findBySphereId(Long sphereId) {
    return list("sphere.id", sphereId);
  }

  public List<String> findSubtreePathsForDeletion(Long nodeId) {
    @SuppressWarnings("unchecked")
    List<String> results =
        getEntityManager()
            .createNativeQuery(
                """
            WITH RECURSIVE subtree AS (
                SELECT id, storage_path
                FROM nodes
                WHERE id = :nodeId

                UNION ALL

                SELECT n.id, n.storage_path
                FROM nodes n
                JOIN subtree s ON n.parent_node_id = s.id
            )
            SELECT storage_path
            FROM subtree
            WHERE storage_path IS NOT NULL
              AND storage_path <> ''
            """)
            .setParameter("nodeId", nodeId)
            .getResultList();

    return results;
  }
}
