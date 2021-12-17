CREATE USER 'vertx2'@'localhost' IDENTIFIED BY 'password2';
CREATE USER 'vertx2'@'%' IDENTIFIED BY 'password2';
GRANT ALL ON *.* TO 'vertx2'@'localhost';
GRANT ALL ON *.* TO 'vertx2'@'%'; FLUSH PRIVILEGES;
