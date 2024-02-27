# chensoul-cloud

一个基于 Spring Boot2、Spring Cloud 2021 & Alibaba、OAuth2、Mybatis Plus、ActiveMQ、XxlJob、Docker
等前沿技术搭建的微服务SASS多租户RBAC权限后端管理系统。

## 项目介绍

### 特性

- 主体框架：采用Spring Boot 2.7.18、Spring Cloud 2021.0.9、Spring Cloud Alibaba 2021.0.5.0版本进行系统设计。
- 统一注册：支持 Nacos 作为注册中心，实现多配置、分群组、分命名空间、多业务模块的注册和发现功能。
- 统一认证：统一 Oauth2 认证协议，并支持自定义 grant_type 实现手机验证码登录。
- 业务监控：利用 Spring Boot Admin 来监控各个独立微服务运行状态。
- 内部调用：集成了 Feign 与自定义内部注解，支持内部调用。
- 业务熔断：采用 Sentinel 实现业务熔断处理，避免服务之间出现雪崩。
- 在线文档：通过接入 OpenAPI，实现在线 API 文档的查看与调试。
- 业务分离：采用前后端分离的框架设计，提高开发效率、降低维护成本、增强系统稳定性和灵活性。
- 多租户功能：集成 Mybatis Plus，自定义 SQL 执行拦截器，实现 SAAS 多租户。
- 消息中间件：采用 ActiveMQ 实现服务之间消息转发。
- 分布式事物方案：采用 Seata 实现多个微服务分布式事物一致。
- 分布式定时器：采用 XxlJob 实现多个微服务分布式任务调度。
- 微服务网关：采用 Spring Gateway 实现流量配置动态化、API 管理和路由、负载均衡和容错、解决跨域问题、鉴权、限流、熔断等等。

### 开发环境

| 组件          | 用途  	  |              版本号              |
|:------------|:------:|:-----------------------------:|
| Maven       |  依赖管理  |            3.0.4以上            |
| Java        | 编译运行项目 | 1.8以上（推荐8u161以后的版本，否则要装JCE插件） |
| MySQL       |  数据库   |              8.x              |
| Redis     	 | 缓存组件 	 |              7.x              |
| RabbitMQ    | 消息中间件  |            3.7.15             |
| Kafka       | 消息中间件  |             2.2.0
| Lua         |  限流脚本  |             5.3.5             |

### 依赖版本

| 依赖                                                                                                   | 本项目版本         | 新版                                                                                                                                                                                                                                                        | 说明                                                                                                                             |
|------------------------------------------------------------------------------------------------------|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| [spring-boot](https://github.com/spring-projects/spring-boot)                                        | 2.7.18        | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/boot/spring-boot-dependencies/maven-metadata.xml">                        | 限制 Spring Boot 2.x                                                                                                             |
| [spring-cloud](https://github.com/spring-cloud)                                                      | 2021.0.9      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">                    | 限制 Spring Boot 2.x                                                                                                             |
| [spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba)                              | 2021.0.5.0    | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml">                      | 限制 Spring Boot 2.x，<a href="https://github.com/alibaba/spring-cloud-alibaba/blob/2022.x/README-zh.md">Spring Cloud Alibaba</a> |
| [spring-boot-admin](https://github.com/codecentric/spring-boot-admin)                                | 2.7.15        | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml">                                | 限制 Spring Boot 2.x                                                                                                             |
| [spring-security-oauth2-autoconfigure](https://github.com/spring-attic/spring-security-oauth2-boot/) | 2.6.8         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/org/springframework/security/oauth/boot/spring-security-oauth2-autoconfigure/maven-metadata.xml"> | 限制 Spring Boot 2.x                                                                                                             |
| [spring-security-oauth2](https://github.com/spring-attic/spring-security-oauth)                      | 2.5.2.RELEASE | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/oauth/spring-security-oauth2/maven-metadata.xml">                                            | 限制 Spring Boot 2.x                                                                                                             |
| [spring-security-jwt](https://github.com/spring-attic/spring-security-oauth)                         | 1.1.1.RELEASE | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=1.&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/security//spring-security-jwt/maven-metadata.xml">                        | 限制 Spring Boot 2.x                                                                                                             |
| [mybatis-plus](https://github.com/baomidou/mybatis-plus)                                             | 3.5.5         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/baomidou/mybatis-plus-boot-starter/maven-metadata.xml">                                                        |                                                                                                                                |
| [dynamic-datasource](https://github.com/baomidou/dynamic-datasource)                                 | 3.6.1         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=3.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/baomidou/dynamic-datasource-spring-boot-starter/maven-metadata.xml">                          | 限制 JDK 8，指 dynamic-datasource-spring-boot-starter                                                                              |
| [springdoc](https://github.com/springdoc)                                                            | 1.7.0         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/org/springdoc/springdoc-openapi-ui/maven-metadata.xml">                                                            | 用于生成 API doc，支持从 javadoc 中获取字段注释                                                                                               |
| [xxl-job](https://github.com/xuxueli/xxl-job)                                                        | 2.4.0         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/xuxueli/xxl-job/maven-metadata.xml">                                                                           | 分布式任务调度平台XXL-JOB                                                                                                               |

## 本地快速启动

### 1. 设置环境变量

```bash
MYSQL_ROOT_PASSWORD=123456
REDIS_PASSWORD=123456
MQ_PASSWORD=admin
```

### 2. 安装基础服务

#### 基于 docker 安装：

mysql:

```bash
docker run \
  -d \
  --restart always \
  --privileged=true \
  --name mysql8 \
  -v /etc/localtime:/etc/localtime \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=true\
  mysql:8 \
  --log-bin=mysql-bin \
  --server-id=1 \
  --binlog_expire_logs_seconds=1209600 \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_general_ci
```

redis:

```bash
docker run \
  -d \
  --restart always \
  --privileged=true \
  --name redis \
  -v /etc/localtime:/etc/localtime \
  -p 6379:6379 \
  redis:7 \
  --requirepass $REDIS_PASSWORD \
  --appendonly yes
```

activemq:

```bash
docker run \
  -d \
  --restart always \
  --name activemq \
  -p 61616:61616 \
  -p 8161:8161 \
  symptoma/activemq:latest
```

### 2. 配置本地 hosts 文件

```bash
127.0.0.1 chensoul-auth
127.0.0.1 chensoul-gateway
127.0.0.1 chensoul-registry
127.0.0.1 chensoul-xxljob
127.0.0.1 chensoul-mysql
127.0.0.1 chensoul-redis
127.0.0.1 chensoul-mq
```

## 端口

| 模块                | 模块名称          | 服务名               | 端口    | 用户名/密码 |
|-------------------|---------------|-------------------|-------|--------|
| Nacos             | 注册、配置中心       | chensoul-registry | 8848  |        |
| MySQL             | 数据库           | chensoul-mysql    | 3306  |        |
| Redis             | 数据库           | chensoul-redis    | 6379  |        |
| ActiveMQ          | 队列            | chensoul-mq       | 11111 |        |
| chensoul-gateway  | 网关            | chensoul-gateway  | 9999  |        |
| chensoul-auth     | 授权            | chensoul-auth     | 6666  |        |
| chensoul-upms     | 用户            | chensoul-upms     | 6100  |        |
| chensoul-task     | 任务            | chensoul-task     | 6200  |        |
| chensoul-msg      | 消息            | chensoul-msg      | 6300  |        |
| chensoul-flows    | 任务流程          | chensoul-flows    | 6400  |        |
| chensoul-monitor  | 监控            | chensoul-monitor  | 5100  |        |
| chensoul-xxljob   | XXL Job Admin | chensoul-xxljob   | 5200  |        |
| chensoul-sentinel | sentinel      | chensoul-sentinel | 5300  |        |

## TODO

- syslog
- 模块移出：redis、mybatis-plus
- oauth2 server
- springdoc
- WebSocket
- k8s

### 原则

- 知道每一行代码的作用
- 编写单元测试
- 不要重复造轮子
