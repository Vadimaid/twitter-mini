create table if not exists post
(
    id              bigserial primary key,
    twitter_user_id bigint references twitter_user (id) not null,
    topic           varchar                             not null,
    text            varchar                             not null,
    tags            varchar,
    created_at      timestamp without time zone         not null default now()
);

INSERT INTO post (twitter_user_id, topic, text, tags)
SELECT id,
       'First Day at Work',
       'Started my new job today! So excited about the opportunities ahead.',
       'work,career,newbeginnings'
FROM twitter_user
WHERE login = 'vadim'
UNION ALL
SELECT id, 'Weekend Plans', 'Going hiking this weekend. Anyone want to join?', 'hiking,outdoor,weekend'
FROM twitter_user
WHERE login = 'vadim1'
UNION ALL
SELECT id, 'Tech Review', 'Just got the latest smartphone. Will share my thoughts soon.', 'tech,review,gadgets'
FROM twitter_user
WHERE login = 'vadim1';

INSERT INTO post (twitter_user_id, topic, text, tags)
SELECT id, 'Study Goals', 'Preparing for my final exams. Wish me luck!', 'study,education,exams'
FROM twitter_user
WHERE login = 'aziret'
UNION ALL
SELECT id, 'Gaming Night', 'Hosting a gaming tournament this Friday!', 'gaming,esports,fun'
FROM twitter_user
WHERE login = 'aziret'
UNION ALL
SELECT id, 'Coding Progress', 'Finally solved that difficult algorithm problem!', 'coding,programming,achievement'
FROM twitter_user
WHERE login = 'aziret'
UNION ALL
SELECT id, 'Book Review', 'Just finished reading Clean Code. Highly recommend!', 'books,programming,learning'
FROM twitter_user
WHERE login = 'aziret';

-- Posts for organizations
INSERT INTO post (twitter_user_id, topic, text, tags)
SELECT id, 'Product Launch', 'Introducing our latest innovation - coming next week!', 'technology,innovation,launch'
FROM twitter_user
WHERE login = 'google'
UNION ALL
SELECT id, 'Tech Conference', 'Join us at GoogleIO 2025 for exciting announcements!', 'conference,tech,google'
FROM twitter_user
WHERE login = 'google'
UNION ALL
SELECT id, 'Career Opportunity', 'We are hiring software engineers worldwide!', 'careers,jobs,technology'
FROM twitter_user
WHERE login = 'google';

INSERT INTO post (twitter_user_id, topic, text, tags)
SELECT id, 'New Model Release', 'Model Y gets an upgrade! Now with 500 mile range.', 'EV,sustainability,innovation'
FROM twitter_user
WHERE login = 'tesla'
UNION ALL
SELECT id,
       'Sustainability Report',
       'Tesla helped reduce 5M tons of CO2 emissions this year!',
       'climate,sustainability,impact'
FROM twitter_user
WHERE login = 'tesla'
UNION ALL
SELECT id, 'Factory Update', 'Giga Texas reaches new production milestone!', 'manufacturing,growth,texas'
FROM twitter_user
WHERE login = 'tesla'
UNION ALL
SELECT id, 'Tech Innovation', 'New Autopilot features coming in next update!', 'AI,autonomous,technology'
FROM twitter_user
WHERE login = 'tesla';

-- Continue with more organizations
INSERT INTO post (twitter_user_id, topic, text, tags)
SELECT id, 'Platform Update', 'New features rolling out to improve user experience!', 'update,social,technology'
FROM twitter_user
WHERE login = 'meta'
UNION ALL
SELECT id, 'Privacy Enhancement', 'Introducing new privacy controls for users', 'privacy,security,social'
FROM twitter_user
WHERE login = 'meta'
UNION ALL
SELECT id, 'VR Development', 'Metaverse reaches 1 million daily active users!', 'VR,metaverse,future'
FROM twitter_user
WHERE login = 'meta';

INSERT INTO post (twitter_user_id, topic, text, tags)
SELECT id, 'New Series Alert', 'Your favorite show returns this fall!', 'streaming,entertainment,series'
FROM twitter_user
WHERE login = 'netflix'
UNION ALL
SELECT id, 'Award Season', 'Our original content received 15 Emmy nominations!', 'awards,entertainment,streaming'
FROM twitter_user
WHERE login = 'netflix'
UNION ALL
SELECT id, 'Game Division', 'Netflix Games adds 10 new titles this month', 'gaming,mobile,entertainment'
FROM twitter_user
WHERE login = 'netflix'
UNION ALL
SELECT id, 'Documentary Premier', 'New nature documentary series coming next month!', 'documentary,nature,streaming'
FROM twitter_user
WHERE login = 'netflix';