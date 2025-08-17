alter table if exists person_info
    drop constraint if exists person_info_pkey;

alter table if exists person_info
    add primary key (twitter_user_id);

alter table if exists person_info
    drop column if exists id;

alter table if exists person_info
    rename column twitter_user_id to id;

alter table if exists organization_info
    drop constraint if exists organization_info_pkey;

alter table if exists organization_info
    add primary key (twitter_user_id);

alter table if exists organization_info
    drop column if exists id;

alter table if exists organization_info
    rename column twitter_user_id to id;