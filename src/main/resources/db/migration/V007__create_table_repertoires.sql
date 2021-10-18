CREATE TABLE repertoires
(
    id_spec INT  NOT NULL,
    _date   DATE NOT NULL,
    CONSTRAINT k7 PRIMARY KEY (id_spec, _date),
    CONSTRAINT c5 FOREIGN KEY (id_spec) REFERENCES spectacles (id) ON DELETE CASCADE
);