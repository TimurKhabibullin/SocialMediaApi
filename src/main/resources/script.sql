create table Person(
                       id int generated by default as identity primary key,
                       username varchar(100) unique not null,
                       email varchar(150) unique not null,
                       password varchar not null,
                       role varchar(100) not null
);


create table Post(
                     id int generated by default as identity primary key,
                     header varchar(500) not null,
                     text varchar not null,
                     link_for_image varchar not null,
                     date_of_create BIGINT not null,
                     person_id int not null
);

create table friend_requests (
                                 id int generated by default as identity PRIMARY KEY,
                                 sender_id INT,
                                 recipient_id INT,
                                 status VARCHAR(20)
);

CREATE TABLE friendships (
                             id int generated by default as identity PRIMARY KEY,
                             user1_id INT,
                             user2_id INT
);

CREATE TABLE messages (
                          id int generated by default as identity PRIMARY KEY,
                          sender_id INT,
                          recipient_id INT,
                          text TEXT
);