CREATE DATABASE IF NOT EXISTS tingeso;
CREATE DATABASE IF NOT EXISTS keycloakdb;

create table if not exists tingeso.client
(
    avaliable    bit          null,
    client_id    bigint auto_increment
        primary key,
    last_name    varchar(255) null,
    mail         varchar(255) null,
    name         varchar(255) null,
    password     varchar(255) null,
    phone_number varchar(255) null,
    rut          varchar(255) null,
    state        varchar(255) null,
    role         varchar(255) null
);

create table if not exists tingeso.client_behind
(
    avaliable        bit          null,
    client_id_behind bigint auto_increment
        primary key,
    report_id        bigint       null,
    last_name        varchar(255) null,
    mail             varchar(255) null,
    name             varchar(255) null,
    phone_number     varchar(255) null,
    rut              varchar(255) null,
    state            varchar(255) null
);

create table if not exists tingeso.client_behind_loans
(
    client_behind_loans_id bigint auto_increment
        primary key,
    client_id_behind       bigint null,
    loan_report_id         bigint null
);

create table if not exists tingeso.client_loans
(
    client_id       bigint null,
    id_client_loans bigint auto_increment
        primary key,
    loan_id         bigint null
);

create table if not exists tingeso.fine
(
    date      date         null,
    amount    bigint       null,
    client_id bigint       null,
    fine_id   bigint auto_increment
        primary key,
    loan_id   bigint       null,
    state     varchar(255) null,
    type      varchar(255) null
);

create table if not exists tingeso.fine_report
(
    date           date         null,
    amount         bigint       null,
    client_id      bigint       null,
    fine_report_id bigint auto_increment
        primary key,
    loan_id        bigint       null,
    report_id      bigint       null,
    state          varchar(255) null,
    type           varchar(255) null
);

create table if not exists tingeso.loans
(
    active        bit          null,
    date          date         null,
    delivery_date date         null,
    return_date   date         null,
    amount        bigint       null,
    client_id     bigint       null,
    extra_charges bigint       null,
    loan_id       bigint auto_increment
        primary key,
    staff_id      bigint       null,
    loan_type     varchar(255) null
);

create table if not exists tingeso.loans_report
(
    date             date         null,
    delivery_date    date         null,
    return_date      date         null,
    amount           bigint       null,
    client_id        bigint       null,
    client_id_behind bigint       null,
    extra_charges    bigint       null,
    loan_report_id   bigint auto_increment
        primary key,
    report_id        bigint       null,
    staff_id         bigint       null,
    loan_type        varchar(255) null
);

create table if not exists tingeso.records
(
    client_id     bigint       null,
    loan_id       bigint       null,
    record_amount bigint       null,
    record_date   datetime(6)  null,
    record_id     bigint auto_increment
        primary key,
    tool_id       bigint       null,
    record_type   varchar(255) null
);

create table if not exists tingeso.reports
(
    client_id_behind bit    null,
    fine_id_reports  bit    null,
    loan_id_report   bit    null,
    report_date      date   null,
    tools_id_ranking bit    null,
    client_id_report bigint null,
    report_id        bigint auto_increment
        primary key
);

create table if not exists tingeso.staff
(
    staff_id   bigint auto_increment
        primary key,
    password   varchar(255) null,
    staff_mail varchar(255) null,
    staff_name varchar(255) null,
    staff_rut  varchar(255) null
);

create table if not exists tingeso.tools
(
    diary_fine_fee bigint       null,
    loan_count     bigint       null,
    loan_fee       bigint       null,
    reposition_fee bigint       null,
    stock          bigint       null,
    tool_id        bigint auto_increment
        primary key,
    category       varchar(255) null,
    disponibility  varchar(255) null,
    initial_state  varchar(255) null,
    tool_name      varchar(255) null,
    low_dmg_fee    bigint       null
);

create table if not exists tingeso.tools_loan_report
(
    loan_id             bigint null,
    tool_id             bigint null,
    tool_loan_report_id bigint auto_increment
        primary key
);

create table if not exists tingeso.tools_loans
(
    id      bigint auto_increment
        primary key,
    loan_id bigint null,
    tool_id bigint null
);

create table if not exists tingeso.tools_ranking
(
    report_id       bigint null,
    tool_id         bigint null,
    tool_ranking_id bigint auto_increment
        primary key
);

create table if not exists tingeso.tools_report
(
    loan_count     bigint       null,
    tool_id_report bigint auto_increment
        primary key,
    category       varchar(255) null,
    tool_name      varchar(255) null
);


INSERT INTO tingeso.client (client_id, last_name, mail, name, password, phone_number, rut, state, role) VALUES
(1,'martinez','gm@gmail.com','gonzalo','admin','954653212','21656544-9','activo','ADMIN');

-- REPOSITION VALUES ARE IN CLP
INSERT INTO tingeso.tools(tool_id, category, tool_name, initial_state, disponibility, reposition_fee, loan_fee, diary_fine_fee, stock, low_dmg_fee, loan_count) VALUES
(1, 'Herramientas manuales', 'Martillo', 'Disponible', 'Disponible', 8000, 1500, 300, 25, 200, 0),
(2, 'Herramientas manuales', 'Destornillador Plano', 'Disponible', 'Disponible', 6000, 1200, 200, 30, 150, 0),
(3, 'Herramientas manuales', 'Destornillador Phillips', 'Disponible', 'Disponible', 6000, 1200, 200, 30, 150, 0),
(4, 'Herramientas manuales', 'Llave inglesa', 'Disponible', 'Disponible', 9000, 1800, 300, 20, 250, 0),
(5, 'Herramientas manuales', 'Juego de llaves Allen', 'Disponible', 'Disponible', 7000, 1500, 250, 18, 200, 0),
(6, 'Herramientas manuales', 'Alicate universal', 'Disponible', 'Disponible', 8000, 1600, 300, 22, 220, 0),
(7, 'Herramientas manuales', 'Sierra manual', 'Disponible', 'Disponible', 10000, 2000, 350, 12, 300, 0),
(8, 'Herramientas manuales', 'Cinta métrica', 'Disponible', 'Disponible', 5000, 800, 150, 40, 100, 0),
(9, 'Electroportátiles', 'Taladro percutor', 'Disponible', 'Disponible', 45000, 8000, 1500, 8, 1200, 0),
(10, 'Electroportátiles', 'Atornillador eléctrico', 'Disponible', 'Disponible', 35000, 6000, 1200, 10, 1000, 0),
(11, 'Electroportátiles', 'Amoladora angular', 'Disponible', 'Disponible', 35000, 7000, 1200, 9, 1100, 0),
(12, 'Electroportátiles', 'Sierra circular', 'Disponible', 'Disponible', 50000, 9000, 1800, 6, 1400, 0),
(13, 'Electroportátiles', 'Sierra de calar (jigsaw)', 'Disponible', 'Disponible', 32000, 6000, 1000, 10, 900, 0),
(14, 'Electroportátiles', 'Lijadora orbital', 'Disponible', 'Disponible', 30000, 6000, 900, 11, 800, 0),
(15, 'Electroportátiles', 'Rotomartillo', 'Disponible', 'Disponible', 70000, 10000, 2000, 5, 1800, 0),
(16, 'Jardinería', 'Cortacésped', 'Disponible', 'Disponible', 120000, 15000, 2500, 3, 3000, 0),
(17, 'Jardinería', 'Desbrozadora', 'Disponible', 'Disponible', 80000, 10000, 1800, 4, 2200, 0),
(18, 'Jardinería', 'Motosierra', 'Disponible', 'Disponible', 90000, 12000, 2000, 5, 2400, 0),
(19, 'Jardinería', 'Podadora de tijera', 'Disponible', 'Disponible', 25000, 4000, 600, 15, 500, 0),
(20, 'Jardinería', 'Soplador / Aspirador de hojas', 'Disponible', 'Disponible', 30000, 5000, 700, 7, 700, 0),
(21, 'Medición y nivelación', 'Nivel láser', 'Disponible', 'Disponible', 40000, 7000, 1000, 6, 1200, 0),
(22, 'Medición y nivelación', 'Multímetro digital', 'Disponible', 'Disponible', 15000, 3000, 500, 20, 400, 0),
(23, 'Medición y nivelación', 'Cinta métrica profesional', 'Disponible', 'Disponible', 8000, 1200, 200, 35, 150, 0),
(24, 'Medición y nivelación', 'Calibrador vernier', 'Disponible', 'Disponible', 12000, 2000, 300, 14, 250, 0),
(25, 'Construcción y elevación', 'Compactadora / Gimpy', 'Disponible', 'Disponible', 100000, 20000, 3000, 2, 3500, 0),
(26, 'Construcción y elevación', 'Generador portátil', 'Disponible', 'Disponible', 200000, 25000, 4000, 2, 5000, 0),
(27, 'Construcción y elevación', 'Andamio modular (unidad)', 'Disponible', 'Disponible', 50000, 8000, 1200, 10, 1500, 0),
(28, 'Construcción y elevación', 'Carretilla', 'Disponible', 'Disponible', 15000, 3000, 400, 25, 300, 0),
(29, 'Construcción y elevación', 'Plataforma elevadora pequeña', 'Disponible', 'Disponible', 250000, 30000, 5000, 1, 6000, 0),
(30, 'Especializadas', 'Soldadora MIG/MMA', 'Disponible', 'Disponible', 180000, 22000, 3500, 3, 4000, 0),
(31, 'Especializadas', 'Compresor de aire portátil', 'Disponible', 'Disponible', 140000, 18000, 3000, 4, 3200, 0);
