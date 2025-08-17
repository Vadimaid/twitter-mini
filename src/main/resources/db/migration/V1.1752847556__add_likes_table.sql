create table if not exists m2m_user_likes (
    user_id bigint not null,
    post_id bigint not null,
    primary key (user_id, post_id)
);