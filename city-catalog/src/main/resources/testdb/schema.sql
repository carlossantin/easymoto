drop table city_distance if exists;
drop table city if exists;

create table city (ID integer identity primary key, name varchar(50) not null);

create table city_distance(from_id integer not null, to_id integer not null, distance integer not null);
alter table city_distance add foreign key (from_id) references city(id) on delete cascade;
alter table city_distance add foreign key (to_id) references city(id) on delete cascade;

