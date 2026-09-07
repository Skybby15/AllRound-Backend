package io.github.skybby15.allround.Repository;

import java.util.List;

import io.github.skybby15.allround.Model.Node;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class NodeRepository implements PanacheRepository<Node> {
    public List<Node> findBySphereId(Long sphereId) {
        return list("sphere.id", sphereId);
    }
}
