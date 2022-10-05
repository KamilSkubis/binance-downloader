# Status
Working.

# Project goal
Console application to download data from Binance and save it in database. 

# Installation

Application is using MySQL so please install MySQL on your computer.
## Setting up schema, tables
Log in to mysql using
`mysql -u {your user, default root} -p`
provide password for user. Then create new schema
`create schema {type schema name ex. stock_data};` then
`use {schema name that you typed earlier};`
Copy and paste queries to create necessary tables

```
create table {name your table ex. binance_1d}(
        id bigint AUTO_INCREMENT,
        symbol_id int,
        open_time datetime(6),
        open double,
        high double,
        low double,
        close double,
        volume double,
        key(id)
        );
```

```
create table symbols(
        id bigint AUTO_INCREMENT,
        symbol char(15),
        key(id)
        );
```

This is all for setting up database. Now download compiled jars from build *page*. 

# How you can use this application
By default, this app is using following settings to connect to MySQL database 
 




