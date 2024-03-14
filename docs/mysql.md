# MySQL 读写分离

## 启动数据库

```bash
docker compose -f docker compose.yml -p chensoul-cloud up -d mysql-master mysql-slave
```

## 配置主从复制

主数据库：

```sql
docker exec -it chensoul-cloud-mysql-master-1 /bin/bash

mysql -uroot -p123456
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'chensoul'@'%';

SHOW MASTER STATUS;
```

从数据库：

```sql
docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' chensoul-cloud-mysql-master-1

docker exec -it chensoul-cloud-mysql-slave-1 /bin/bash

mysql -uroot -p123456
> change master to master_host='192.168.181.5',master_user='chensoul',master_password='chensoul',master_log_file='mysql-bin.000003',master_log_pos=642;

>SHOW SLAVE STATUS \G;
```

正常情况下，SlaveIORunning 和 SlaveSQLRunning 都是 No，因为我们还没有开启主从复制过程。使用 start slave
开启主从复制过程，然后再次查询主从同步状态 show slave status \G;。

```sql
>start slave;
>SHOW SLAVE STATUS \G;
>SHOW BINARY LOGS;
```

## 集成 Sharding Sphere 实现读写分离

1). 在pom.xml中增加shardingJdbc的maven坐标

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.1.1</version>
</dependency>
```

2). 在 application.yml中增加数据源的配置

先注释原来的数据源配置：

```yml
#spring.datasource:
#  url: jdbc:mysql://localhost:3306/chensoul-cloud?connectTimeout=2000&socketTimeout=150000&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
#  driver-class-name: com.mysql.cj.jdbc.Driver
#  username: chensoul
#  password: chensoul
```

然后，添加 shardingsphere 配置：

```yml
spring.shardingsphere:
  datasource:
    names:
      master,slave
    master:
      type: com.zaxxer.hikari.HikariDataSource
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbcUrl: jdbc:mysql://${mysql-master:mysql-master}:3307/chensoul-cloud?connectTimeout=2000&socketTimeout=150000&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
      username: chensoul
      password: chensoul
    slave:
      type: com.zaxxer.hikari.HikariDataSource
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbcUrl: jdbc:mysql://${mysql-slave:mysql-slave}:3308/chensoul-cloud?connectTimeout=2000&socketTimeout=150000&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
      username: chensoul
      password: chensoul
  master-slave:
    load-balance-algorithm-type: round_robin
    # 最终的数据源名称
    name: dataSource
    # 主库数据源名称
    master-data-source-name: master
    # 从库数据源名称列表，多个逗号分隔
    slave-data-source-names: slave
  props:
    sql:
      show: true
```

增加下面配置，允许 bean 覆盖：

```yml
spring:
  main:
    allow-bean-definition-overriding: true
```

3). 在本地的 hosts 文件添加：

```bash
127.0.0.1 mysql-master
127.0.0.1 mysql-slave
```

4). 启动应用

在启动过程中出现异常：

```bash
Caused by: java.sql.SQLFeatureNotSupportedException: isValid
```

Sharding Sphere 不支持数据库健康检查，关闭数据库健康检查即可。

```yml
# Sharding Sphere 不支持数据库健康检查
management.health.db.enabled: false
```

# MySQL 分库分表

