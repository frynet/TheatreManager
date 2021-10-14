CREATE TABLE spectacles_roles
(
    id_spec INT     NOT NULL,
    id_role INT     NOT NULL,
    main    BOOLEAN NOT NULL,
    CONSTRAINT k4 PRIMARY KEY (id_spec, id_role),
    CONSTRAINT c1 FOREIGN KEY (id_role) REFERENCES roless (id) ON DELETE CASCADE,
    CONSTRAINT c2 FOREIGN KEY (id_spec) REFERENCES spectacles (id) ON DELETE CASCADE
);