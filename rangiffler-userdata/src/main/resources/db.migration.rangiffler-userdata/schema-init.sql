create extension if not exists "uuid-ossp";

create table if not exists country
(
    id   UUID unique  not null default uuid_generate_v1(),
    code varchar(50)  not null,
    name varchar(255) not null,
    flag bytea        not null,
    primary key (id)
);

insert into "country" (code, "name", "flag") values  ('33f3', '33b3', 'g3g3g3g');


alter table "country"
    owner to postgres;

create table if not exists "user"
(
    id         UUID unique        not null default uuid_generate_v1(),
    username   varchar(50) unique not null,
    firstname  varchar(255),
    lastName   varchar(255),
    avatar     bytea,
    country_id UUID                not null,
    primary key (id),
    constraint us_country_id foreign key (country_id) references country (id)
);

alter table "user"
    owner to postgres;

create table if not exists friendship
(
    requester_id UUID        not null,
    addressee_id UUID        not null,
    created_date date        not null,
    status       varchar(50) not null,
    primary key (requester_id, addressee_id),
    constraint friend_are_distinct_ck check (requester_id <> addressee_id),
    constraint fk_requester_id foreign key (requester_id) references "user" (id),
    constraint fk_addressee_id foreign key (addressee_id) references "user" (id)
);

alter table friendship
    owner to postgres;