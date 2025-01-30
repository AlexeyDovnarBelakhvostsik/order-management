create table orders
(
    id               integer generated always as identity
        constraint orders_pk
            primary key,
    ordernumber      varchar(100),
    totalorderamount integer     not null,
    orderdate        varchar(45) not null,
    address          varchar(100),
    deliveryaddress  varchar(100),
    paymenttype      varchar(100),
    deliverytype     varchar(100)
);