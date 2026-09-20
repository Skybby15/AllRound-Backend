CREATE TABLE Nodes (
    id BIGSERIAL PRIMARY KEY,
    sphere_id BIGINT NOT NULL REFERENCES Spheres(id) ON DELETE CASCADE,
    parent_node_id BIGINT REFERENCES Nodes(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(10) NOT NULL,
    storage_path VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX uq_nodes_sphere_parent_name
ON nodes (sphere_id, parent_node_id, name)
NULLS NOT DISTINCT;