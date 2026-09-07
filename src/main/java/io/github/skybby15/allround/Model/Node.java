package io.github.skybby15.allround.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(
    name = "nodes",
    indexes = {
        @Index(
            name = "idx_nodes_sphere_parent",
            columnList = "sphere_id, parent_node_id"
        )
    }
)
public class Node extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sphere_id", nullable = false)
    private Sphere sphere;

  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_node_id")
    private Node parent;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private NodeType type;

  @NotBlank
  @Column(name="storage_path", length = 255)
  private String storagePath;

}
