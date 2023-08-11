drop table if exists book_mark;
drop table if exists review_recommend_keyword;
drop table if exists recommend_keyword;
drop table if exists review;
drop table if exists menu;
drop table if exists bakery_category;
drop table if exists bakery;
drop table if exists member;
drop table if exists bread_type;
drop table if exists category;
drop table if exists nutrient_type;

create table bakery
(
    bakery_id                bigint not null auto_increment,
    bread_type_id            bigint not null,
    nutrient_type_id         bigint not null,
    address_rest             varchar(255)          not null,
    bakery_name              varchar(255)          not null,
    bakery_picture           varchar(255)          not null,
    book_mark_count          bigint  default 0,
    city                     varchar(255)          not null,
    closed_day               varchar(255),
    first_near_station       varchar(255),
    map_url                  varchar(255)          not null,
    homepage                 varchar(255),
    is_haccp                 boolean default false not null,
    is_nongmo                boolean default false not null,
    is_vegan                 boolean default false not null,
    review_count             bigint  default 0,
    keyword_delicious_count  bigint  default 0,
    keyword_kind_count       bigint  default 0,
    keyword_special_count    bigint  default 0,
    keyword_zero_waste_count bigint  default 0,
    opening_hours            varchar(255),
    phone_number             varchar(255),
    second_near_station      varchar(255),
    state                    varchar(255)          not null,
    town                     varchar(255)          not null,
    primary key (bakery_id)
);


create table category (
                          category_id bigint not null auto_increment,
                          category_name varchar(255),
                          primary key (category_id)
);


create table bakery_category (
                                 bakery_category_id bigint not null auto_increment,
                                 bakery_id bigint not null,
                                 category_id bigint not null,
                                 primary key (bakery_category_id)
);

create table bread_type (
                            bread_type_id bigint not null auto_increment,
                            bread_type_name varchar(255) not null,
                            is_gluten_free boolean not null,
                            is_nut_free boolean not null,
                            is_sugar_free boolean not null,
                            is_vegan boolean not null,
                            primary key (bread_type_id)
);

create table book_mark
(
    book_mark_id bigint not null auto_increment,
    bakery_id   bigint not null,
    member_id   bigint not null,
    primary key (book_mark_id)
);

create table member
(
    member_id     bigint not null auto_increment,
    bread_type_id            bigint not null,
    nutrient_type_id         bigint not null,
    email         varchar(255) not null,
    main_purpose  varchar(255) not null,
    nickname      varchar(255) not null,
    password      varchar(255) not null,
    platform_type varchar(255) not null,
    role          varchar(255),
    refresh_token  varchar(255),
    created_at timestamp,
    updated_at timestamp,
    primary key (member_id)
);

create table menu
(
    menu_id    bigint not null auto_increment,
    menu_name  varchar(255) not null,
    menu_price integer      not null,
    bakery_id  bigint       not null,
    primary key (menu_id)
);


create table nutrient_type (
                               nutrient_type_id bigint not null auto_increment,
                               is_ingredient_open boolean not null,
                               is_not_open boolean not null,
                               is_nutrient_open boolean not null,
                               nutrient_type_name varchar(255) not null,
                               primary key (nutrient_type_id)
);

create table recommend_keyword
(
    recommend_keyword_id bigint not null auto_increment,
    keyword_name varchar(255) not null,
    primary key (recommend_keyword_id)
);

create table review
(
    review_id   bigint not null auto_increment,
    is_like     boolean not null,
    review_text varchar(255),
    bakery_id   bigint,
    member_id   bigint,
    created_at timestamp,
    updated_at timestamp,
    primary key (review_id)
);

create table review_recommend_keyword
(
    review_recommend_keyword_id bigint not null auto_increment,
    recommend_keyword_id         bigint not null,
    review_id                    bigint not null,
    primary key (review_recommend_keyword_id)
);

alter table bakery
    add constraint fk1
        foreign key (bread_type_id)
            references bread_type (bread_type_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table bakery
    add constraint fk2
        foreign key (nutrient_type_id)
            references nutrient_type (nutrient_type_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table member
    add constraint fk3
        foreign key (bread_type_id)
            references  bread_type (bread_type_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table member
    add constraint fk4
        foreign key (nutrient_type_id)
            references  nutrient_type (nutrient_type_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table book_mark
    add constraint fk5
        foreign key (bakery_id)
            references bakery (bakery_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table book_mark
    add constraint fk6
        foreign key (member_id)
            references member (member_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table menu
    add constraint fk7
        foreign key (bakery_id)
            references bakery (bakery_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table review
    add constraint fk8
        foreign key (bakery_id)
            references bakery (bakery_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table review
    add constraint fk9
        foreign key (member_id)
            references member (member_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table review_recommend_keyword
    add constraint fk10
        foreign key (recommend_keyword_id)
            references recommend_keyword (recommend_keyword_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table review_recommend_keyword
    add constraint fk11
        foreign key (review_id)
            references review (review_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table bakery_category
    add constraint fk12
        foreign key (category_id)
            references category (category_id) ON DELETE CASCADE ON UPDATE CASCADE;

alter table bakery_category
    add constraint fk13
        foreign key (bakery_id)
            references bakery (bakery_id) ON DELETE CASCADE ON UPDATE CASCADE;