# Bats

面向单机与分布式 OLTP/OLAP 场景的可暂停的渐进式 SQL 引擎 <b><i>(只用于研究)</i></b>

改编自 [Apache Calcite](https://calcite.apache.org/) 和 [Apache Drill](http://drill.apache.org/)


## 开发环境

* JDK 1.8+
* Maven 3.3+


需要先执行 mvn eclipse:eclipse，否则项目里用到的一些 java 源文件会找不到，这些 java 源文件是通过模板生成的。



## 在 IDE 中运行

代码导入 IDE 后，直接运行 [BatsEngineStart](https://github.com/lealone/Bats/blob/master/bats-test/src/test/java/org/lealone/bats/test/start/BatsEngineStart.java) 

然后执行 [JdbcTest](https://github.com/lealone/Bats/blob/master/bats-test/src/test/java/org/lealone/bats/test/jdbc/JdbcTest.java) 通过标准 JDBC API 访问数据库。



## 打包后运行

#### 打包

先执行以下命令打包:

mvn package assembly:assembly -Dmaven.test.skip=true

生成的文件放在 target\bats-5.0.0-SNAPSHOT 目录


#### 运行

打开两个命令行窗口，都切换到 target\bats-5.0.0-SNAPSHOT\bin 目录

在第一个窗口中输入 lealone 启动数据库

在第二个窗口中输入 sqlshell -url jdbc:lealone:tcp://localhost:9210/lealone -user root 打开一个 SQL Shell 窗口

输入以下命令创建表、新增记录、查询记录

CREATE TABLE IF NOT EXISTS my_table(name varchar(20) primary key, f2 int);

INSERT INTO my_table(name, f2) VALUES('a', 123);

用普通的 OLTP SQL 引擎执行

SELECT count(*) FROM my_table WHERE name>='a';

加 olap 前缀用 bats 这个 OLAP SQL 引擎执行

SELECT count(*) FROM olap.my_table WHERE name>='a';





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
