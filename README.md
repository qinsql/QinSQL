# Bats

面向 OLAP 场景的 SQL 引擎


## 开发环境

* JDK 1.8+
* Maven 3.3+


需要先执行 mvn eclipse:eclipse，否则项目里用到的一些 java 源文件会找不到，这些 java 源文件是通过模板生成的。


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