# Bats

面向 OLAP 场景的 SQL 引擎

改编自 [Apache Calcite](https://calcite.apache.org/) 和 [Apache Drill](http://drill.apache.org/)



## 开发环境

* JDK 1.8+
* Maven 3.3+


需要先执行 mvn eclipse:eclipse，否则项目里用到的一些 java 源文件会找不到，这些 java 源文件是通过模板生成的。



## 运行

代码导入 IDE 后，直接运行 [BatsEngineStart](https://github.com/lealone/Bats/blob/master/bats-test/src/test/java/org/lealone/bats/test/start/BatsEngineStart.java) 

然后执行 [JdbcTest](https://github.com/lealone/Bats/blob/master/bats-test/src/test/java/org/lealone/bats/test/jdbc/JdbcTest.java) 通过标准 JDBC API 访问数据库。



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