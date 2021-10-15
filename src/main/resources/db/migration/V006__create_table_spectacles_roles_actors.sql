CREATE TABLE spectacles_roles_actors
(
    id_spec  INT NOT NULL,
    id_role  INT NOT NULL,
    id_actor INT NOT NULL,
    CONSTRAINT k6 PRIMARY KEY (id_spec, id_role, id_actor),
    CONSTRAINT c6 FOREIGN KEY (id_spec, id_actor) REFERENCES spectacles_actors (id_spec, id_actor) ON DELETE CASCADE,
    CONSTRAINT c7 FOREIGN KEY (id_actor) REFERENCES actors (id) ON DELETE CASCADE,
    CONSTRAINT c8 FOREIGN KEY (id_role) REFERENCES roless (id) ON DELETE CASCADE,
    CONSTRAINT c9 FOREIGN KEY (id_spec, id_role) REFERENCES spectacles_roles (id_spec, id_role) ON DELETE CASCADE
);