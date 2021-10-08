CREATE DATABASE IF NOT EXISTS contas;

create table conta (
	id bigint not null auto_increment,
	id_cliente varchar(255) not null,
	numero varchar(255) not null unique,
	saldo decimal(19,2) not null,
	primary key (id)
);

create table transacao (
	id bigint not null auto_increment,
	criado_em datetime(6) not null,
	tipo_transacao varchar(255) not null,
	uuid binary(255) not null unique,
	valor decimal(19,2) not null,
	conta_id bigint not null,
	primary key (id),
	foreign key (conta_id) references conta (id)
);