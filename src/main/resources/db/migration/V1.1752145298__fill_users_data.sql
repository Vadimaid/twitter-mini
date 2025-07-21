insert into twitter_user (login, password, type)
values ('vadim', '12345', 'PERSON');
insert into twitter_user (login, password, type)
values ('aziret', '11111', 'PERSON');
insert into twitter_user (login, password, type)
values ('aidar', '00000', 'PERSON');
insert into twitter_user (login, password, type)
values ('aisanat', '12345', 'PERSON');
insert into twitter_user (login, password, type)
values ('tilek', '12345', 'PERSON');
insert into twitter_user (login, password, type)
values ('Daniel', '123456', 'PERSON');
insert into twitter_user (login, password, type)
values ('danil', '12345', 'PERSON');

insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Vadim', 'Maidanov', '1995-04-01', (select id from twitter_user where login = 'vadim'));
insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Aziret', 'Bekmuratov', '2005-07-17', (select id from twitter_user where login = 'aziret'));
insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Aidar', 'Alymbaev', '1999-11-16', (select id from twitter_user where login = 'aidar'));
insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Aisanat', 'Ishembekova', '2007-05-24', (select id from twitter_user where login = 'aisanat'));
insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Tilek', 'Talaibekov', '1993-11-26', (select id from twitter_user where login = 'tilek'));
insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Daniel', 'Saparbaev', '2005-08-18', (select id from twitter_user where login = 'Daniel'));
insert into person_info (first_name, last_name, birth_date, twitter_user_id)
values ('Danil', 'Perevozkin', '1983-02-21', (select id from twitter_user where login = 'danil'));


insert into twitter_user (login, password, type)
values ('google', 'ggl123', 'ORGANIZATION');
insert into twitter_user (login, password, type)
values ('microsoft', 'msft456', 'ORGANIZATION');
insert into twitter_user (login, password, type)
values ('apple', 'appl789', 'ORGANIZATION');
insert into twitter_user (login, password, type)
values ('tesla', 'tsla321', 'ORGANIZATION');
insert into twitter_user (login, password, type)
values ('amazon', 'amzn654', 'ORGANIZATION');
insert into twitter_user (login, password, type)
values ('meta', 'meta987', 'ORGANIZATION');
insert into twitter_user (login, password, type)
values ('netflix', 'nflx246', 'ORGANIZATION');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'google'), 'Google LLC', 'Internet Services & Technology',
        '1998-09-04');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'microsoft'), 'Microsoft Corporation', 'Software & Computing',
        '1975-04-04');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'apple'), 'Apple Inc.', 'Consumer Electronics', '1976-04-01');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'tesla'), 'Tesla, Inc.', 'Electric Vehicles & Clean Energy',
        '2003-07-01');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'amazon'), 'Amazon.com, Inc.', 'E-commerce & Cloud Computing',
        '1994-07-05');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'meta'), 'Meta Platforms, Inc.', 'Social Media & Technology',
        '2004-02-04');

insert into organization_info (twitter_user_id, title, occupation, date_of_foundation)
values ((select id from twitter_user where login = 'netflix'), 'Netflix, Inc.', 'Streaming Services & Entertainment',
        '1997-08-29');

