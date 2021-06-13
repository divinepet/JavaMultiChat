create table if not exists chat
(
    id       bigint generated always as identity,
    username varchar(30) unique not null,
    password varchar(200) not null
);

alter table chat
    owner to elaronda;