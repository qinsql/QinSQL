# QinSQL

QinSQL 是一个基于 [Lealone](https://github.com/lealone/Lealone) 的可暂停的渐进式 OLAP 引擎

可以使用 MySQL 或 PostgreSQL 的协议和 SQL 语法访问 Lealone 数据库

MySQL 协议版本支持 5.x 到 8.x 系列

PostgreSQL JDBC Driver 支持 9.x 和 42.x 两个系列版本


## 编译需要

* Git 2.32+
* JDK 17+
* Maven 3.8+


## 下载项目源代码

`git clone https://github.com/qinsql/QinSQL.git qinsql`

假设源代码放在 `E:\qinsql`


## 从源代码构建

进入 E:\qinsql 目录，运行: `mvn clean package assembly:assembly -Dmaven.test.skip=true`

生成的文件放在 E:\qinsql\target 目录中，
默认生成 qinsql-x.y.z.tar.gz 和 qinsql-x.y.z.zip 两个压缩文件，
其中 x.y.z 代表实际的版本号

如果运行: `mvn package -Dmaven.test.skip=true -P database`

只生成 qinsql-x.y.z.jar


## 运行 QinSQL

进入 `qinsql\target\qinsql-5.2.0\bin` 目录，运行: `qinsql`

或者进入 `qinsql\target` 目录，运行: `java -jar qinsql-5.2.0.jar`

```java
INFO 09:12:13.384 Lealone version: 5.2.0
INFO 09:12:13.392 Loading config from file:/E:/qinsql/target/qinsql-5.2.0/conf/qinsql.yaml
INFO 09:12:13.444 Base dir: E:/qinsql/target/qinsql-5.2.0/data
INFO 09:12:13.450 Init storage engines: 3 ms
INFO 09:12:13.474 Init transaction engines: 23 ms
INFO 09:12:13.477 Init sql engines: 2 ms
INFO 09:12:13.650 Init protocol server engines: 171 ms
INFO 09:12:13.651 Init lealone database: 0 ms
INFO 09:12:13.655 Starting TcpServer accepter
INFO 09:12:13.657 TcpServer started, host: 127.0.0.1, port: 9210
INFO 09:12:13.657 Starting MySQLServer accepter
INFO 09:12:13.658 MySQLServer started, host: 127.0.0.1, port: 9310
INFO 09:12:13.659 Starting PgServer accepter
INFO 09:12:13.659 PgServer started, host: 127.0.0.1, port: 9510
INFO 09:12:13.659 Total time: 272 ms (Load config: 57 ms, Init: 208 ms, Start: 7 ms)
INFO 09:12:13.660 Exit with Ctrl+C
```

## 用 MySQL 客户端访问 QinSQL

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


## 用 PostgreSQL 客户端访问 QinSQL

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


## 启用 OLAP 引擎

`set olap_threshold 1000;`

当执行 select 语句时，如果遍历了1000条记录还没有结束就会自动启用 OLAP 引擎
