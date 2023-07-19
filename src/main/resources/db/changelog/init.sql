create table account
(
    id          uuid not null
        constraint account_pk
            primary key,
    customer_id uuid not null
);

create table balance
(
    id serial primary key,
    amount     money   not null,
    currency   varchar not null,
    account_id uuid    not null
        constraint balance_account_id_fk
            references account
);

create table transaction
(
    id          uuid    not null
        constraint transaction_pk primary key,
    account_id  uuid    not null
        constraint transaction_account_id_fk
            references account,
    currency    varchar not null,
    amount      money   not null,
    direction   varchar not null,
    description text    not null
)