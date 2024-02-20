BEGIN;

DROP TABLE IF EXISTS items CASCADE;
CREATE TABLE items (id serial PRIMARY KEY, val int);
INSERT INTO items (val) SELECT 0 FROM generate_series(1,40);

COMMIT;