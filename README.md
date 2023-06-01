# QinSQL

使用 MySQL 或 PostgreSQL 的协议和 SQL 语法访问 Lealone 数据库

MySQL 协议版本支持 5.x 到 8.x 系列

PostgreSQL JDBC Driver 支持 9.x 和 42.x 两个系列版本


## 编译需要

* JDK 17+ (运行只需要 JDK 1.8+)
* Maven 3.8+


## 打包

执行以下命令打包:

`mvn package -Dmaven.test.skip=true`

生成的文件放在 `qinsql\target` 目录


## 运行 Lealone 数据库

进入 `qinsql\target\qinsql-5.2.0\bin` 目录，运行: `qinsql`

```java
E:\qinsql\target\qinsql-5.2.0\bin>qinsql
Lealone version: 5.2.0
Loading config from Loading config from file:/E:/qinsql/target/qinsql-5.2.0/conf/lealone.yaml
Base dir: E:/qinsql/target/qinsql-5.2.0/data
Init storage engines: 4 ms
Init transaction engines: 27 ms
Init sql engines: 1 ms
Init protocol server engines: 184 ms
Init lealone database: 0 ms
Starting TcpServer accepter
TcpServer started, host: 127.0.0.1, port: 9210
Starting MySQLServer accepter
MySQLServer started, host: 127.0.0.1, port: 9310
Starting PgServer accepter
PgServer started, host: 127.0.0.1, port: 9510
Total time: 293 ms (Load config: 69 ms, Init: 220 ms, Start: 4 ms)
Exit with Ctrl+C
```

## 用 MySQL 客户端访问 Lealone 数据库

执行以下命令启动 MySQL 客户端:

`mysql --no-beep -h 127.0.0.1 -P 9310 -u root`

```sql
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 0
Server version: 5.1.48-lealone-5.2.0

Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> create table if not exists pet(name varchar(20), age int);
Query OK, 0 rows affected (0.00 sec)

mysql> insert into pet values('pet1', 2);
Query OK, 1 row affected (0.01 sec)

mysql> select count(*) from pet;
+----------+
| COUNT(*) |
+----------+
|        1 |
+----------+
1 row in set (0.01 sec)

mysql>
```


## 用 PostgreSQL 客户端访问 Lealone 数据库

执行以下命令启动 PostgreSQL 客户端:

`psql -h 127.0.0.1 -p 9510 -U postgres -W`

提示口令时输入: postgres

```sql
口令:
psql (14.0, 服务器 8.2.23)
输入 "help" 来获取帮助信息.

postgres=> create table if not exists pet(name varchar(20), age int);
UPDATE 0
postgres=> insert into pet values('pet1', 2);
CommandInterfaceINSERT 0 1
postgres=> select count(*) from pet;
 count(*)
----------
        1
(1 行记录)

postgres=>
```


## 在 IDE 中运行

代码导入 IDE 后，直接运行 [QinServerStart](https://github.com/qinsql/QinSQL/blob/master/qinsql-test/src/test/java/org/qinsql/test/QinServerStart.java)

