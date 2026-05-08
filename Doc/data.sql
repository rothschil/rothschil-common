DROP TABLE IF EXISTS INTF;

CREATE TABLE INTF (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

INSERT INTO INTF ( name, password) VALUES
  ('Dangote', 'Billionaire Industrialist'),
  ('Gates', 'Billionaire Tech Entrepreneur'),
  ('Alakija', 'Billionaire Oil Magnate');
