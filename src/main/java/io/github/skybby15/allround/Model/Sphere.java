package io.github.skybby15.allround.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(
    name = "spheres",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_spheres_owner_name",
            columnNames = {"owner_id", "name"}
        )
    }
)
public class Sphere extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)  
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @NotBlank
  @Column(nullable = false)
  private String name;

}
