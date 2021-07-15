# Bats-Sphere

使用 MySQL 或 PostgreSQL 的协议和 SQL 语法访问 Lealone 数据库



## 开发环境

* JDK 1.8+
* Maven 3.3+



## 在 IDE 中运行

代码导入 IDE 后，直接运行 [LealoneSphereStart](https://github.com/lealone/Bats/blob/master/bats-sphere/src/test/java/org/lealone/plugins/sphere/test/LealoneSphereStart.java) 

控制台出现的信息中包含 `Total time` 就说明启动成功了



## 用 MySQL Shell 访问

在命令行窗口中运行以下命令

`mysql --no-beep -h localhost -P 9310 -u root -pzhh`


```sql
E:\lealone\lealone-sphere>mysql --no-beep -h localhost -P 9310 -u root -pzhh
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 4
Server version: 5.0.0 (2019-11-15)-ShardingSphere-Proxy 5.0.0-RC1

Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> use test;
Database changed
mysql> drop table if exists pet;
Query OK, 0 rows affected (0.13 sec)

mysql> create table pet(pet_id int primary key, name varchar(20), age int);
Query OK, 0 rows affected (0.03 sec)

mysql> insert into pet(pet_id,name,age) values(1, 'pet1', 2);
Query OK, 1 row affected (0.01 sec)

mysql> insert into pet(pet_id,name,age) values(2, 'pet1', 2);
Query OK, 1 row affected (0.02 sec)

mysql> select count(*) from pet;
+----------+
| COUNT(*) |
+----------+
|        2 |
+----------+
1 row in set (0.12 sec)

mysql>
```


## 用 PostgreSQL Shell 访问

在命令行窗口中运行以下命令

`psql -h 127.0.0.1 -p 9510 -U root -W -d test`


```sql
E:\lealone\lealone-sphere>set LANG=en_US

E:\lealone\lealone-sphere>psql -h 127.0.0.1 -p 9510 -U root -W -d test
Password for user root:
psql (9.3.2, server 12.3)
WARNING: psql major version 9.3, server major version 12.3.
         Some psql features might not work.
Type "help" for help.

test=> drop table if exists pet;
null 0
test=> create table pet(pet_id int primary key, name varchar(20),age int);
null 0
test=> insert into pet(pet_id,name,age) values(1, 'pet1', 2);
INSERT 0 1
test=> insert into pet(pet_id,name,age) values(2, 'pet1', 2);
INSERT 0 1
test=> select count(*) from pet;
 COUNT(*)
----------
        2
(1 row)

test=>
```