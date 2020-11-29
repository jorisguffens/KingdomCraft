-- apply changes
create table kingdoms (
  id                            integer auto_increment not null,
  name                          varchar(255),
  display                       varchar(255),
  prefix                        varchar(255),
  suffix                        varchar(255),
  spawn                         varchar(255),
  invite_only                   tinyint(1) default 0 not null,
  max_members                   integer not null,
  default_rank_id               integer,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint uq_kingdoms_name unique (name),
  constraint uq_kingdoms_default_rank_id unique (default_rank_id),
  constraint pk_kingdoms primary key (id)
);

create table kingdom_attributes (
  id                            integer auto_increment not null,
  kingdom_id                    integer,
  name                          varchar(255),
  value                         varchar(255),
  constraint uq_kingdom_attributes_kingdom_id_name unique (kingdom_id,name),
  constraint pk_kingdom_attributes primary key (id)
);

create table user_invites (
  id                            integer auto_increment not null,
  user_id                       varchar(255),
  kingdom_id                    integer,
  sender_id                     varchar(255),
  created_at                    datetime(6) not null,
  constraint pk_user_invites primary key (id)
);

create table ranks (
  id                            integer auto_increment not null,
  name                          varchar(255),
  kingdom_id                    integer,
  display                       varchar(255),
  prefix                        varchar(255),
  suffix                        varchar(255),
  max_members                   integer not null,
  level                         integer not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint uq_ranks_name_kingdom_id unique (name,kingdom_id),
  constraint pk_ranks primary key (id)
);

create table rank_attributes (
  id                            integer auto_increment not null,
  rank_id                       integer,
  name                          varchar(255),
  value                         varchar(255),
  constraint uq_rank_attributes_rank_id_name unique (rank_id,name),
  constraint pk_rank_attributes primary key (id)
);

create table rank_permission_groups (
  id                            integer auto_increment not null,
  rank_id                       integer,
  name                          varchar(255),
  constraint uq_rank_permission_groups_rank_id_name unique (rank_id,name),
  constraint pk_rank_permission_groups primary key (id)
);

create table kingdom_relations (
  id                            integer auto_increment not null,
  kingdom_id                    integer,
  other_kingdom_id              integer,
  relation                      integer not null,
  is_request                    tinyint(1) default 0 not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_kingdom_relations primary key (id)
);

create table users (
  id                            varchar(255) not null,
  name                          varchar(255),
  rank_id                       integer,
  kingdom_id                    integer,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint uq_users_name unique (name),
  constraint pk_users primary key (id)
);

create table user_chatchannels (
  id                            integer auto_increment not null,
  user_id                       varchar(255),
  channel                       varchar(255),
  enabled                       tinyint(1) default 0 not null,
  constraint uq_user_chatchannels_user_id_channel unique (user_id,channel),
  constraint pk_user_chatchannels primary key (id)
);

alter table kingdoms add constraint fk_kingdoms_default_rank_id foreign key (default_rank_id) references ranks (id) on delete set null on update set null;

create index ix_kingdom_attributes_kingdom_id on kingdom_attributes (kingdom_id);
alter table kingdom_attributes add constraint fk_kingdom_attributes_kingdom_id foreign key (kingdom_id) references kingdoms (id) on delete cascade on update cascade;

create index ix_user_invites_user_id on user_invites (user_id);
alter table user_invites add constraint fk_user_invites_user_id foreign key (user_id) references users (id) on delete cascade on update cascade;

create index ix_user_invites_kingdom_id on user_invites (kingdom_id);
alter table user_invites add constraint fk_user_invites_kingdom_id foreign key (kingdom_id) references kingdoms (id) on delete cascade on update cascade;

create index ix_user_invites_sender_id on user_invites (sender_id);
alter table user_invites add constraint fk_user_invites_sender_id foreign key (sender_id) references users (id) on delete cascade on update cascade;

create index ix_ranks_kingdom_id on ranks (kingdom_id);
alter table ranks add constraint fk_ranks_kingdom_id foreign key (kingdom_id) references kingdoms (id) on delete cascade on update cascade;

create index ix_rank_attributes_rank_id on rank_attributes (rank_id);
alter table rank_attributes add constraint fk_rank_attributes_rank_id foreign key (rank_id) references ranks (id) on delete cascade on update cascade;

create index ix_rank_permission_groups_rank_id on rank_permission_groups (rank_id);
alter table rank_permission_groups add constraint fk_rank_permission_groups_rank_id foreign key (rank_id) references ranks (id) on delete cascade on update cascade;

create index ix_kingdom_relations_kingdom_id on kingdom_relations (kingdom_id);
alter table kingdom_relations add constraint fk_kingdom_relations_kingdom_id foreign key (kingdom_id) references kingdoms (id) on delete cascade on update cascade;

create index ix_kingdom_relations_other_kingdom_id on kingdom_relations (other_kingdom_id);
alter table kingdom_relations add constraint fk_kingdom_relations_other_kingdom_id foreign key (other_kingdom_id) references kingdoms (id) on delete cascade on update cascade;

create index ix_users_rank_id on users (rank_id);
alter table users add constraint fk_users_rank_id foreign key (rank_id) references ranks (id) on delete set null on update set null;

create index ix_users_kingdom_id on users (kingdom_id);
alter table users add constraint fk_users_kingdom_id foreign key (kingdom_id) references kingdoms (id) on delete set null on update set null;

create index ix_user_chatchannels_user_id on user_chatchannels (user_id);
alter table user_chatchannels add constraint fk_user_chatchannels_user_id foreign key (user_id) references users (id) on delete cascade on update cascade;

