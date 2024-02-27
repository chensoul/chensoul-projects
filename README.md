# chensoul-cloud

一个基于 Spring Boot2、Spring Cloud 2021 & Alibaba、OAuth2、Mybatis Plus、ActiveMQ、XxlJob、Docker
等前沿技术搭建的微服务SASS多租户RBAC权限后端管理系统。

## 项目介绍

### 特性

- 主体框架：采用Spring Boot 2.7.18、Spring Cloud 2021.0.9、Spring Cloud Alibaba 2021.0.6.0版本进行系统设计。
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

### 技术选型

Spring Cloud每个业务领域都有多个可供选择的组件，这里也列出了微服务章节中将要用到的组件+中间件的技术选型，这也是当前主流的选型。

| 内容    |            技术选型             |
|:------|:---------------------------:|
| 服务网关  |    Spring Cloud Gateway     |
| 服务治理  |           Eureka            |
| 负载均衡  |  Spring Cloud Loadbalancer  |
| 服务调用  |          OpenFeign          |
| 服务容错  |          Sentinel           |
| 消息总线  | Spring Cloud Bus + RabbitMQ |
| 调用链追踪 |    Sleuth + Zipkin + ELK    |
| 消息驱动  |      Stream + RabbitMQ      |

## 快速开始

### 环境说明

| 组件       |   用途   |              版本号              |
|:---------|:------:|:-----------------------------:|
| Java     | 编译运行项目 | 1.8以上（推荐8u161以后的版本，否则要装JCE插件） |
| Maven    |  依赖管理  |            3.8.0以上            |
| MySQL    |  数据库   |              8.x              |
| Redis    |  缓存组件  |              7.x              |
| RabbitMQ | 消息中间件  |            3.7.15             |
| ActiveMQ | 消息中间件  |                               |
| Kafka    | 消息中间件  |             2.2.0             |

### Maven 依赖版本

⚠️注意：JDK 使用的是 1.8，Spring Boot 只能使用 2.x 版本。

| 依赖                                                                                                   | 本项目版本         | 新版                                                                                                                                                                                                                                                        | 说明                |
|------------------------------------------------------------------------------------------------------|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| [spring-boot](https://github.com/spring-projects/spring-boot)                                        | 2.7.18        | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/boot/spring-boot-dependencies/maven-metadata.xml">                        |                   |
| [spring-cloud](https://github.com/spring-cloud)                                                      | 2021.0.9      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">                    |                   |
| [spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba)                              | 2021.0.6.0    | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml">                      |                   |
| [spring-boot-admin](https://github.com/codecentric/spring-boot-admin)                                | 2.7.15        | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml">                                |                   |
| [spring-security-oauth2-autoconfigure](https://github.com/spring-attic/spring-security-oauth2-boot/) | 2.6.8         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/org/springframework/security/oauth/boot/spring-security-oauth2-autoconfigure/maven-metadata.xml"> |                   |
| [spring-security-oauth2](https://github.com/spring-attic/spring-security-oauth)                      | 2.5.2.RELEASE | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/oauth/spring-security-oauth2/maven-metadata.xml">                                            |                   |
| [spring-security-jwt](https://github.com/spring-attic/spring-security-oauth)                         | 1.1.1.RELEASE | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=1.&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/security//spring-security-jwt/maven-metadata.xml">                        |                   |
| [mybatis-plus](https://github.com/baomidou/mybatis-plus)                                             | 3.5.5         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/baomidou/mybatis-plus-boot-starter/maven-metadata.xml">                                                        |                   |
| [dynamic-datasource](https://github.com/baomidou/dynamic-datasource)                                 | 3.6.1         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=3.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/baomidou/dynamic-datasource-spring-boot-starter/maven-metadata.xml">                          |                   |
| [springdoc](https://github.com/springdoc)                                                            | 1.7.0         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/org/springdoc/springdoc-openapi-ui/maven-metadata.xml">                                                            | 用于生成 Swagger 文档   |
| [xxl-job](https://github.com/xuxueli/xxl-job)                                                        | 2.4.0         | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/xuxueli/xxl-job/maven-metadata.xml">                                                                           | 分布式任务调度平台 XXL-JOB |

### 默认端口和用户名密码

| 模块       | 模块名称      | 服务名                   | 端口         | 用户名/密码       |
|----------|-----------|-----------------------|------------|--------------|
| MySQL    | 数据库       | mysql                 | 3306       | root/123456  |
| Redis    | 数据库       | redis                 | 6379       | 123456       |
| ActiveMQ | 队列        | activemq              | 61616、8161 |              |
| RabbitMQ | 队列        | rabbitmq              | 5672、15672 | guest/123456 |
| Nacos    | 注册中心、配置中心 | nacos                 | 8848       | nacos/123456 |
| Eureka   | 注册中心      | eureka                | 8761       |              |
| Gateway  | 网关        | gateway               | 8443       |              |
| Monitor  | 监控        | monitor               | 5100       |              |
| Xxljob   | XXLJob    | xxljob                | 5200       |              |
| Sentinel | sentinel  | sentinel              | 5300       |              |
| Auth     | 授权        | chensoul-auth-service | 6666       |              |

### 环境搭建

- [Git](https://git-scm.com/downloads)
- [OrbStack](https://orbstack.dev/)
- [Java](https://www.azul.com/downloads/#zulu)
- [Curl](https://curl.haxx.se/download.html)
- [Jq](https://stedolan.github.io/jq/download/)
- [Spring Boot CLI](https://docs.spring.io/spring-boot/docs/3.0.4/reference/html/getting-started.html#getting-started.installing.cli)
- [Siege](https://github.com/JoeDog/siege#where-is-it)
- [Helm](https://helm.sh/docs/intro/install/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [Istioctl](https://istio.io/latest/docs/setup/getting-started/#download)

安装软件：

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

brew tap spring-io/tap && \
brew tap homebrew/cask-versions && \
brew install zulu21 && \
brew install jq && \
brew install spring-boot && \
brew install helm && \
brew install siege && \
brew install orbstack

echo 'export JAVA_HOME=$(/usr/libexec/java_home -v8)' >> ~/.bash_profile
source ~/.bash_profile
```

验证版本：

```bash
git version && \
docker version -f json | jq -r .Client.Version && \
java -version 2>&1 | grep "openjdk version" && \
curl --version | grep "curl" | sed 's/(.*//' && \
jq --version && \
spring --version && \
siege --version 2>&1 | grep SIEGE && \
helm version --short && \
kubectl version --client -o json | jq -r .clientVersion.gitVersion && \
minikube version | grep "minikube" && \
istioctl version --remote=false
```

### 1. 配置本地 hosts 文件

```bash
127.0.0.1 mysql
127.0.0.1 mysql-master
127.0.0.1 mysql-slave

127.0.0.1 redis
127.0.0.1 rabbitmq
127.0.0.1 activemq

127.0.0.1 config-file
127.0.0.1 nacos
127.0.0.1 eureka
127.0.0.1 gateway
127.0.0.1 zipkin
```

### 2. 启动服务

```bash
mvn clean package -DskipTests=true && docker-compose build && docker-compose up -d

docker ps --format {{.Names}}
```

## 参考资料

- [Microservices with Spring Boot 3 and Spring Cloud](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition)

