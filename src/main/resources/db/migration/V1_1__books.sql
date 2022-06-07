CREATE TABLE IF NOT EXISTS books (
    id int auto_increment not null,
    title varchar,
    writer varchar,
    publisher varchar,
    price int,
    PRIMARY KEY(id)
);
INSERT INTO books(id, title, writer, publisher, price) VALUES (4, 'タイトル', '著者', '出版社', 300);

