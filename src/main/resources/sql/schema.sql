CREATE TABLE IF NOT EXISTS monkeyschema

--https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/appendix-schema.html
--create table if not exists users(
--	username varchar_ignorecase(50) not null primary key,
--	password varchar_ignorecase(50) not null,
--	enabled boolean not null
--);
--
--create table if not exists authorities (
--	username varchar_ignorecase(50) not null,
--	authority varchar_ignorecase(50) not null,
--	constraint fk_authorities_users foreign key(username) references users(username)
--);
--create unique index ix_auth_username on authorities (username,authority);



---- Users
--create table if not exists users
--(
--	username varchar(200) not null primary key,
--	password varchar(500) not null,
--	enabled boolean not null
--);
--
---- Authorities
--create table if not exists authorities
--(
--	username varchar(200) not null,
--	authority varchar(50) not null,
--	constraint fk_authorities_users foreign key (username) references users (username),
--	constraint username_authority UNIQUE (username, authority)
--);