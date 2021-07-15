# Bats-PostgreSQL

基于 PostgreSQL 协议，使用 PostgreSQL 的 SQL 语法访问 Lealone 数据库


## 开发环境

* JDK 1.8+
* Maven 3.3+
 

## 在 IDE 中运行

代码导入 IDE 后，直接运行 [PgServerStart](https://github.com/lealone/Bats/blob/master/bats-postgresql/src/test/java/org/lealone/plugins/postgresql/test/PgServerStart.java) 

以下是启动成功的信息
```
INFO  11:49:33.270 Lealone version: 5.0.0-SNAPSHOT
INFO  11:49:33.270 Loading config file:/E:/lealone/lealone-postgresql/target/test-classes/lealone.yaml
INFO  11:49:33.395 Base dir: ./target/data
INFO  11:49:33.395 Init storage engines: 0 ms
INFO  11:49:33.473 Init transaction engines: 78 ms
INFO  11:49:33.488 Init sql engines: 15 ms
INFO  11:49:33.519 Init protocol server engines: 31 ms
INFO  11:49:33.707 Init lealone database: 188 ms
INFO  11:49:33.707 Starting nio net server
INFO  11:49:34.206 TcpServer started, host: 127.0.0.1, port: 9210
INFO  11:49:34.206 Starting nio net server
INFO  11:49:34.253 PgServer started, host: 127.0.0.1, port: 9510
INFO  11:49:34.253 Total time: 983 ms (Load config: 125 ms, Init: 312 ms, Start: 546 ms)
INFO  11:49:34.253 Exit with Ctrl+C
```

然后在命令行窗口中运行以下命令

`psql -h 127.0.0.1 -p 9510 -U test -W -d test`


```sql
E:\lealone\lealone-postgresql>set LANG=en_US

E:\lealone\lealone-postgresql>psql -h 127.0.0.1 -p 9510 -U test -W -d test
Password for user test:
psql (9.3.2, server 8.1.4)
Type "help" for help.

test=> create table if not exists pet(name varchar(20), age int);
UPDATE 0
test=> insert into pet values('pet1', 2);
CommandInterfaceINSERT 0 1
test=> select count(*) from pet;
 count(*)
----------
        1
(1 row)

test=> select name, age from pet where name='pet1';
 name | age
------+-----
 pet1 |   2
(1 row)

test=>
```


## 打包后运行

#### 打包

先执行以下命令打包:

`mvn package -Dmaven.test.skip=true`

生成的文件是 `target\lealone-postgresql-5.0.0-SNAPSHOT.jar`


#### 运行

确保已经按 [Lealone 快速入门](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E7%94%A8%E6%88%B7%E6%96%87%E6%A1%A3.md) 中的步骤安装了 Lealone 数据库，这里假定安装到 `E:\lealone-database` 目录

把 `target\lealone-postgresql-5.0.0-SNAPSHOT.jar` 复制到 `E:\lealone-database\lib` 目录

修改 `E:\lealone-database\conf\lealone.yaml` 文件，增加以下配置:

```yaml
sql_engines:
  - name: Lealone
    enabled: false
    parameters: {
        # key1: v1,
        # key2: v2,
    }
  - name: PostgreSQL
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
  - name: PostgreSQL
    enabled: true
    parameters: {
        port: 9510,
        trace: false,
        allow_others: true,
        daemon: false,
        ssl: false
    }
```
 

最后打开命令行窗口，切换到 `E:\lealone-database\bin` 目录，输入 `lealone` 启动数据库即可
