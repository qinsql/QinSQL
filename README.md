# QinSQL

微服务 数据湖仓 OLTP 数据库 一体化平台

QinSQL = Lealone + 改编的 [Apache Calcite](https://calcite.apache.org/) 和 [Apache Drill](http://drill.apache.org/)


## 开发环境

* JDK 1.8+
* Maven 3.3+


需要先执行 mvn eclipse:eclipse，否则项目里用到的一些 java 源文件会找不到，这些 java 源文件是通过模板生成的。



## 在 IDE 中运行

代码导入 IDE 后，直接运行 [QinEngineStart](https://github.com/lealone/QinSQL/blob/master/qinsql-test/src/test/java/org/qinsql/test/start/QinEngineStart.java) 

然后执行 [JdbcTest](https://github.com/lealone/QinSQL/blob/master/qinsql-test/src/test/java/org/qinsql/test/jdbc/JdbcTest.java) 通过标准 JDBC API 访问数据库。



## 打包后运行

### 打包

先执行以下命令打包:

`mvn package assembly:assembly -Dmaven.test.skip=true`

生成的文件放在 `target\qinsql-5.0.0-SNAPSHOT` 目录


### 运行

打开两个命令行窗口，都切换到 `target\qinsql-5.0.0-SNAPSHOT\bin` 目录

在第一个窗口中输入 `qinsql` 启动数据库

在第二个窗口中输入 `sqlshell -url jdbc:lealone:tcp://localhost:9210/lealone -user root` 打开一个 SQL Shell 窗口


#### 微服务

```sql
--创建服务，关联到指定的 java 类
create service hello_service (hello(name varchar) varchar) implement by 'org.qinsql.test.service.HelloService';

--调用服务
execute service hello_service hello('zhh');
```


#### OLTP 数据库

```sql
CREATE TABLE IF NOT EXISTS my_table(name varchar(20) primary key, f2 int);

INSERT INTO my_table(name, f2) VALUES('a', 123);

SELECT count(*) FROM my_table WHERE name>='a';
```


#### 数据湖仓

```sql
--直接查询本地文件
SELECT count(*) FROM dfs.`E:\lealone\qinsql\qinsql-test\src\test\resources\test.csvh`;

--使用 OLAP 引擎执行查询语句
SELECT count(*) FROM olap.my_table WHERE name>='a';
```



## 模块依赖关系

```
                                  rpc ----------------------|
                                   ↑                        |
                                   |                        ↓
test --> engine --> function --> executor --> vector --> common
                                   |                        ↑
                                   |                        |
                                   |--------------------> logical --> optimizer
```
