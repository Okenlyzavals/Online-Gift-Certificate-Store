DROP TABLE IF EXISTS certificate_has_tag;
DROP TABLE IF EXISTS gift_certificate;
DROP TABLE IF EXISTS tag;

CREATE TABLE gift_certificate (
  id IDENTITY NOT NULL PRIMARY KEY,
  name varchar(45) NOT NULL,
  description varchar(512) DEFAULT NULL,
  price decimal(15,5) NOT NULL,
  duration int NOT NULL,
  create_date timestamp NOT NULL,
  last_update_date timestamp NOT NULL
);

CREATE TABLE tag (
  tag_id IDENTITY NOT NULL PRIMARY KEY,
  tag_name varchar(45) UNIQUE NOT NULL
);

CREATE TABLE certificate_has_tag (
  id_certificate int NOT NULL,
  id_tag int NOT NULL,
  PRIMARY KEY (id_certificate,id_tag),
  CONSTRAINT certificate_has_tag_ibfk_1 FOREIGN KEY (id_certificate) REFERENCES gift_certificate (id) ON DELETE CASCADE,
  CONSTRAINT fk_tag FOREIGN KEY (id_tag) REFERENCES tag (tag_id) ON DELETE CASCADE ON UPDATE RESTRICT
);