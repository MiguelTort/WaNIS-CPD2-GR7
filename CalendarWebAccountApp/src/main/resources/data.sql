DROP TABLE IF EXISTS account;

CREATE TABLE account (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  events VARCHAR(250) DEFAULT NULL
);

INSERT INTO account (name, password, events) VALUES
  ('account1', 'password1', 'test1'),
  ('account2', 'password2', 'test2'),