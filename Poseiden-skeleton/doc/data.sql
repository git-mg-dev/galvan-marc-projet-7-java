DROP TABLE IF EXISTS bid_list;
DROP TABLE IF EXISTS trade;
DROP TABLE IF EXISTS curve_point;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS rule;
DROP TABLE IF EXISTS users;

CREATE TABLE bid_list (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bid_quantity DOUBLE,
  ask_quantity DOUBLE,
  bid DOUBLE ,
  ask DOUBLE,
  benchmark VARCHAR(125),
  bid_list_date TIMESTAMP,
  commentary VARCHAR(125),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (id)
);

CREATE TABLE trade (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  buy_quantity DOUBLE,
  sell_quantity DOUBLE,
  buy_price DOUBLE ,
  sell_price DOUBLE,
  trade_date TIMESTAMP,
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  benchmark VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (id)
);

CREATE TABLE curve_point (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  curve_id tinyint,
  as_of_date TIMESTAMP,
  term DOUBLE,
  value DOUBLE,
  creation_date TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE TABLE rating (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  moodys_rating VARCHAR(125),
  sand_p_rating VARCHAR(125),
  fitch_rating VARCHAR(125),
  order_number tinyint,

  PRIMARY KEY (id)
);

CREATE TABLE rule (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sql_str VARCHAR(125),
  sql_part VARCHAR(125),

  PRIMARY KEY (id)
);

CREATE TABLE users (
  id tinyint(4) NOT NULL AUTO_INCREMENT,
  username VARCHAR(125) UNIQUE,
  password VARCHAR(125),
  fullname VARCHAR(125),
  role VARCHAR(125),

  PRIMARY KEY (id)
);

insert into users(fullname, username, password, role) values("Administrator", "admin", "$2a$10$hlSnwA0RZQYkYSTbZdQFFeBADQG26ht6.CRE.zaLlT6z3QlL.fzhu", "ADMIN");
insert into users(fullname, username, password, role) values("User", "user", "$2a$10$hlSnwA0RZQYkYSTbZdQFFeBADQG26ht6.CRE.zaLlT6z3QlL.fzhu", "USER");