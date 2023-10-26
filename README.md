# QinSQL

QinSQL 是一个基于 [Lealone](https://github.com/lealone/Lealone) 的可暂停的渐进式 OLAP 引擎


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

进入 `qinsql\target\qinsql-6.0.0-SNAPSHOT\bin` 目录，运行: `qinsql`

或者进入 `qinsql\target` 目录，运行: `java -jar qinsql-6.0.0-SNAPSHOT.jar`

```java
INFO 08:50:14.554 Lealone version: 6.0.0-SNAPSHOT
INFO 08:50:14.561 Loading config from file:/E:/qinsql/qinsql-test/target/test-classes/qinsql-test.yaml
INFO 08:50:14.611 Base dir: E:/qinsql/qinsql-test/target/test-data
INFO 08:50:14.619 Init storage engines: 5 ms
INFO 08:50:14.654 Init transaction engines: 35 ms
INFO 08:50:14.660 Init sql engines: 6 ms
INFO 08:50:14.835 Init protocol server engines: 174 ms
INFO 08:50:14.836 Init lealone database: 0 ms
INFO 08:50:14.837 TcpServer started, host: 127.0.0.1, port: 9210
INFO 08:50:14.838 MongoServer started, host: 127.0.0.1, port: 27017
INFO 08:50:14.841 MySQLServer started, host: 127.0.0.1, port: 3306
INFO 08:50:14.841 PgServer started, host: 127.0.0.1, port: 5432
INFO 08:50:14.842 Total time: 286 ms (Load config: 54 ms, Init: 226 ms, Start: 6 ms)
INFO 08:50:14.842 Exit with Ctrl+C
```

## 在 IDE 中运行

代码导入 IDE 后，直接运行 [QinServerStart](https://github.com/qinsql/QinSQL/blob/master/qinsql-test/src/test/java/org/qinsql/test/QinServerStart.java)


## 启用 OLAP 引擎

`set olap_threshold 1000;`

当执行 select 语句时，如果遍历了1000条记录还没有结束就会自动启用 OLAP 引擎
