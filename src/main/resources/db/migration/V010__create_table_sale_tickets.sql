CREATE TABLE sale_tickets
(
    id_ticket SERIAL NOT NULL
        CONSTRAINT K9 PRIMARY KEY,
    id_spec   INT    NOT NULL,
    _date     DATE   NOT NULL,
    price     INT    NOT NULL,
    _row      INT    NOT NULL,
    _col      INT    NOT NULL,
    CONSTRAINT k10 UNIQUE (id_spec, _date, _row, _col),
    CONSTRAINT c11 FOREIGN KEY (_row, _col) REFERENCES hall (_row, _column) ON DELETE RESTRICT,
    CONSTRAINT c10 FOREIGN KEY (id_spec, _date) REFERENCES repertoires (id_spec, _date) ON DELETE RESTRICT
);