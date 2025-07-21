create table if not exists twitter_user
(
    id            bigserial primary key,
    login         varchar(15)                 not null unique,
    password      varchar(15)                 not null,
    registered_at timestamp without time zone not null default now(),
    type          varchar(15)                 not null
);

create table if not exists person_info
(
    id              bigserial primary key,
    twitter_user_id bigint references twitter_user (id) not null unique,
    first_name      varchar(15)                         not null,
    last_name       varchar(15),
    birth_date      date                                not null
);

create table if not exists organization_info
(
    id                 bigserial primary key,
    twitter_user_id    bigint references twitter_user (id) not null unique,
    title              varchar                             not null,
    occupation         varchar,
    date_of_foundation date                                not null
);