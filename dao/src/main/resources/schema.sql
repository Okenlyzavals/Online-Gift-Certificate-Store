DROP TABLE IF EXISTS certificate_has_tag;
DROP TABLE IF EXISTS orders_has_certificates;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS gift_certificate;

CREATE TABLE roles(
    id_role bigint NOT NULL auto_increment,
    role_name varchar(45) not null UNIQUE,
    PRIMARY KEY (id_role)
);

CREATE TABLE gift_certificate (
  certificate_id bigint NOT NULL AUTO_INCREMENT,
  create_date datetime(6) DEFAULT NULL,
  certificate_description varchar(255) DEFAULT NULL,
  duration int DEFAULT NULL,
  last_update_date datetime(6) DEFAULT NULL,
  certificate_name varchar(255) DEFAULT NULL,
  price decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (certificate_id)
);

CREATE TABLE users (
                       id_users bigint NOT NULL AUTO_INCREMENT,
                       email varchar(255) DEFAULT NULL UNIQUE,
                       user_password varchar(255) DEFAULT NULL,
                       username varchar(255) DEFAULT NULL,
                       id_role bigint not null,
                       PRIMARY KEY (id_users),
                       FOREIGN KEY (id_role) REFERENCES roles (id_role)
);

CREATE TABLE orders (
  id_orders bigint NOT NULL AUTO_INCREMENT,
  purchase_date datetime(6) DEFAULT NULL,
  order_price decimal(19,2) DEFAULT NULL,
  id_user bigint DEFAULT NULL,
  PRIMARY KEY (id_orders),
  FOREIGN KEY (id_user) REFERENCES users (id_users) ON DELETE CASCADE
);

CREATE TABLE tag (
  tag_id bigint NOT NULL AUTO_INCREMENT,
  tag_name varchar(255) DEFAULT NULL UNIQUE,
  PRIMARY KEY (tag_id)
);

CREATE TABLE orders_has_certificates (
  order_id bigint NOT NULL,
  cert_id bigint NOT NULL,
  PRIMARY KEY (order_id,cert_id),
  FOREIGN KEY (cert_id) REFERENCES gift_certificate (certificate_id) ON DELETE CASCADE ,
  FOREIGN KEY (order_id) REFERENCES orders (id_orders) ON DELETE CASCADE
);

CREATE TABLE certificate_has_tag (
  id_certificate bigint NOT NULL,
  id_tag bigint NOT NULL,
  PRIMARY KEY (id_certificate,id_tag),
  FOREIGN KEY (id_certificate) REFERENCES gift_certificate (certificate_id) ON DELETE CASCADE ,
  FOREIGN KEY (id_tag) REFERENCES tag (tag_id) ON DELETE CASCADE
);
