CREATE TABLE spectacles_actors
(
    id_spec  INT NOT NULL,
    id_actor INT NOT NULL,
    CONSTRAINT k5 PRIMARY KEY (id_actor, id_spec),
    CONSTRAINT c3 FOREIGN KEY (id_spec) REFERENCES spectacles (id) ON DELETE CASCADE,
    CONSTRAINT c4 FOREIGN KEY (id_actor) REFERENCES actors (id) ON DELETE CASCADE
);