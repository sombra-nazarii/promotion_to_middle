create table jwt_token
(
    id                            bigserial     not null,
    access_token                  varchar(2048) not null,
    access_token_expiration_date  timestamp     not null,
    creation_date                 timestamp     not null,
    deleted                       boolean       not null,
    refresh_token                 varchar(2048) not null,
    refresh_token_expiration_date timestamp     not null,
    valid                         boolean       not null,
    user_credential_id            int8          not null,
    primary key (id)
);

create table role
(
    id   bigserial not null,
    name varchar(255),
    primary key (id)
);

create table user_credential
(
    id          bigserial    not null,
    deleted     boolean      not null,
    email       varchar(255) not null,
    enabled     boolean      not null,
    last_logged timestamp,
    password    varchar(255),
    primary key (id)
);

create table user_credential_roles
(
    user_credential_id int8 not null,
    role_id            int8 not null
);

alter table jwt_token
    add constraint FKjbnbn92u7132lubylx2itrsyo
        foreign key (user_credential_id)
            references user_credential;

alter table user_credential_roles
    add constraint FK1hnmxqtleuju6e3glq371m3bu
        foreign key (role_id)
            references role;

alter table user_credential_roles
    add constraint FKkynpylfhbrgvcrwomwwischj5
        foreign key (user_credential_id)
            references user_credential;
