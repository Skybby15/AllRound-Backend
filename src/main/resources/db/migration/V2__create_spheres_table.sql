CREATE TABLE Spheres (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES Users(id),
    name VARCHAR(100) NOT NULL,

    CONSTRAINT uq_spheres_owner_name UNIQUE (owner_id, name)
);