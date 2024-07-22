create table engagement
(
    video_id   bigint  not null primary key,
    impressions bigint not null,
    views bigint not null
);

create table genre
(
    id   bigint   auto_increment    not null
        primary key,
    name varchar(255) null
);

create table person
(
    id   bigint    auto_increment   not null
        primary key,
    name varchar(255) null
);

create table video
(
    id              bigint  auto_increment     not null
        primary key,
    description     varchar(255) null,
    is_deleted      bit          not null,
    running_time    int          not null,
    title           varchar(255) null,
    year_of_release int          not null,
    director_id     bigint       null,
    genre_id        bigint       null,
    constraint FKby69dcb5gt1coe0o6ucovnj9t
        foreign key (genre_id) references genre (id),
    constraint FKdriyps0deyrqi3khci055fctt
        foreign key (director_id) references person (id)
);

create table video_actor
(
    video_id bigint not null,
    actor_id bigint not null,
    constraint FKd8a3s0m93gp3j3rxf7x8ql50i
        foreign key (video_id) references video (id),
    constraint FKs3jaapfnj4q6oenjnn66034ao
        foreign key (actor_id) references person (id)
);

COMMIT;
