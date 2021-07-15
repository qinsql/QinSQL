# Bats-MySQL

基于 MySQL 协议，使用 MySQL 的 SQL 语法访问 Lealone 数据库



## 开发环境

* JDK 1.8+
* Maven 3.3+
 


## 在 IDE 中运行

代码导入 IDE 后，直接运行 [MySQLServerStart](https://github.com/lealone/Bats/blob/master/bats-mysql/src/test/java/org/lealone/plugins/mysql/test/MySQLServerStart.java) 

以下是启动成功的信息
```
INFO 22:19:05.244 Lealone version: 5.0.0-SNAPSHOT
INFO 22:19:05.244 Loading config from file:/E:/lealone/lealone-mysql/target/test-classes/lealone.yaml
INFO 22:19:05.369 Base dir: ./target/data
INFO 22:19:05.384 Init storage engines: 15 ms
INFO 22:19:05.463 Init transaction engines: 79 ms
INFO 22:19:05.463 Init sql engines: 0 ms
INFO 22:19:05.510 Init protocol server engines: 47 ms
INFO 22:19:05.682 Init lealone database: 172 ms
INFO 22:19:05.682 Starting nio net server
INFO 22:19:06.197 TcpServer started, host: 127.0.0.1, port: 9210
INFO 22:19:06.197 Starting nio net server
INFO 22:19:06.197 MySQLServer started, host: 127.0.0.1, port: 9310
INFO 22:19:06.197 Total time: 953 ms (Load config: 125 ms, Init: 313 ms, Start: 515 ms)
INFO 22:19:06.197 Exit with Ctrl+C
```

然后在命令行窗口中运行以下命令

`mysql --no-beep -h localhost -P 9310 -u root -pzhh`


```sql
E:\lealone\lealone-mysql>mysql --no-beep -h localhost -P 9310 -u root -pzhh
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 0
Server version: 5.1.48-lealone-5.0.0-SNAPSHOT

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


## 打包后运行

#### 打包

先执行以下命令打包:

`mvn package -Dmaven.test.skip=true`

生成的文件是 `target\lealone-mysql-5.0.0-SNAPSHOT.jar`


#### 运行

确保已经按 [Lealone 快速入门](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E7%94%A8%E6%88%B7%E6%96%87%E6%A1%A3.md) 中的步骤安装了 Lealone 数据库，这里假定安装到 `E:\lealone-database` 目录

把 `target\lealone-mysql-5.0.0-SNAPSHOT.jar` 复制到 `E:\lealone-database\lib` 目录

修改 `E:\lealone-database\conf\lealone.yaml` 文件，增加以下配置:

```yaml
sql_engines:
  - name: Lealone
    enabled: true
    parameters: {
        # key1: v1,
        # key2: v2,
    }
  - name: MySQL
    enabled: true
    parameters: {
        # key1: v1,
        # key2: v2,
    }

protocol_server_engines:
  - name: TCP
    enabled: true
    parameters: {
        port: 9210,
        trace: false,
        allow_others: true,
        daemon: false,
        ssl: false
    }
  - name: MySQL
    enabled: true
    parameters: {
        port: 9310,
        trace: false,
        allow_others: true,
        daemon: false,
        ssl: false
    }
```
 

最后打开命令行窗口，切换到 `E:\lealone-database\bin` 目录，输入 `lealone` 启动数据库即可
