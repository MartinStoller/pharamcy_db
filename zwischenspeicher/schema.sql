DROP TABLE IF EXISTS TBL_MEDIKAMENTE;

CREATE TABLE TBL_MEDIKAMENTE (
    id INT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    wirkstoff VARCHAR(250) NOT NULL,
    hersteller VARCHAR(250) NOT NULL,
    vorrat INT NOT NULL
    );