package io.github.skybby15.allround.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.skybby15.allround.DTO.Filters.SphereFilter;
import io.github.skybby15.allround.Model.Sphere;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class SphereRepository implements PanacheRepository<Sphere> {

    public List<Sphere> findByFilter(SphereFilter filter) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (filter.ownerId() != null) {
            query.append("owner.id = :ownerId");
            params.put("ownerId", filter.ownerId());
        }

        return find(query.toString(), params).list();
    }
}
