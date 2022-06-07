CREATE TABLE IF NOT EXISTS users (
    username varchar not null,
    password varchar,
    PRIMARY KEY(username)
);
INSERT INTO users(username, password) VALUES ('testuser', '78b1eb3dcac8fd461fa9042d9a9425e1d7caad8ef6c7d6f3d0a8816a2c197c1b6d585a7f33e36d37');

