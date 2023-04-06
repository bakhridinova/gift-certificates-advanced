drop table if exists
    certificate_tag cascade;
drop table if exists
    certificates cascade;
drop table if exists
    orders cascade;
drop table if exists
    tags cascade;
drop table if exists
    users cascade;

create table certificates
(
    certificate_id      integer primary key generated always as identity,
    name                varchar not null,
    description         varchar not null,
    duration            integer not null,
    price               float not null,
    create_date         timestamp,
    last_update_date    timestamp
);

create table orders
(
    order_id          integer primary key generated always as identity,
    price             float,
    created_at        timestamp,
    certificate_id    integer,
    user_id           integer
);

create table tags
(
    tag_id            bigserial primary key,
    name              varchar not null
);

create table users
(
    user_id           integer primary key generated always as identity,
    username          varchar,
    first_name        varchar,
    last_name         varchar,
    email_address     varchar,
    password          varchar,
    date_of_birth     date
);

create table certificate_tag
(
    certificate_id    bigint not null,
    tag_id            bigint not null
);

alter table if exists tags
    add constraint unique_constrain_tags_name
        unique (name);

alter table if exists certificate_tag
    add constraint foreign_key_certificate_tag_certificate_id
        foreign key (certificate_id)
            references certificates(certificate_id);

alter table if exists certificate_tag
    add constraint foreign_key_certificate_tag_tag_id
        foreign key (tag_id)
            references tags(tag_id);

alter table if exists orders
    add constraint foreign_key_orders_certificate_id
        foreign key (certificate_id)
            references certificates(certificate_id);

alter table if exists orders
    add constraint foreign_key_orders_user_id
        foreign key (user_id)
            references users(user_id);