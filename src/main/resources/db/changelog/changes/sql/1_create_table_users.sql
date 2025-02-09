CREATE TABLE IF NOT EXISTS users
(
    id              UUID PRIMARY KEY ,
    name               varchar(100) NOT NULL,
    surname               varchar(100) NOT NULL,
    patronymic               varchar(100) ,
    date_of_birth           DATE NOT NULL,
    phone_number               varchar(12) NOT NULL,
    email               varchar(100) NOT NULL,
    image   varchar(255),
    password varchar(255),
    role varchar(255)
);
CREATE INDEX IF NOT EXISTS ix_users_email on users(email);
