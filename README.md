# chensoul-cloud

基于 Jdk 1.8、SpringBoot 2.7.18、SpringCloud 2021.0.9 和 Spring Cloud Alibaba 2021.0.6.0 且采用前后端分离的微服务多租户体系架构。

## 项目介绍

### 技术选型

Spring Cloud每个业务领域都有多个可供选择的组件，这里也列出了微服务章节中将要用到的组件+中间件的技术选型，这也是当前主流的选型。

| 内容    |            技术选型             | 描述                                          |
|:------|:---------------------------:|:--------------------------------------------|
| 服务网关  |    Spring Cloud Gateway     | 实现流量配置动态化、API 管理和路由、负载均衡和容错、解决跨域问题、鉴权、限流、熔断 |
| 服务治理  |            Nacos            | 实现多配置、分群组、分命名空间、多业务模块的注册和发现功能               |
| 负载均衡  |  Spring Cloud Loadbalancer  |                                             |
| 服务调用  |          OpenFeign          | 自定义内部注解，支持免认证内部调用微服务接口                      |
| 服务容错  |          Sentinel           | 实现业务熔断处理，避免服务之间出现雪崩                         |
| 消息总线  | Spring Cloud Bus + RabbitMQ |                                             |
| 调用链追踪 |    Sleuth + Zipkin + ELK    |                                             |
| 消息驱动  |      Stream + RabbitMQ      |                                             |
| 统一认证  |           Oauth2            | 扩展授权类型支持手机验证码登录                             |
| 分布式事物 |            Seata            | 实现多个微服务分布式事物一致                              |
| 任务调度  |           XxlJob            | 实现多个微服务分布式任务调度                              |
| 在线文档  |          SpringDoc          | 实现在线 API 文档的查看与调试                           |
| 业务监控  |      Spring Boot Admin      | 监控各个独立微服务运行状态                               |

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

| 模块           | 模块名称      | 服务名                   | 端口         | 用户名/密码       |
|--------------|-----------|-----------------------|------------|--------------|
| MySQL        | 数据库       | mysql                 | 3306       | root/123456  |
| Redis        | 数据库       | redis                 | 6379       | 123456       |
| ActiveMQ     | 队列        | activemq              | 61616、8161 |              |
| RabbitMQ     | 队列        | rabbitmq              | 5672、15672 | guest/123456 |
| Nacos        | 注册中心、配置中心 | nacos                 | 8848       | nacos/123456 |
| Eureka       | 注册中心      | eureka                | 8761       |              |
| Gateway      | 网关        | gateway               | 8443       |              |
| Admin Server | 监控        | admin-server          | 5100       |              |
| Xxljob       | XXLJob    | xxljob                | 5200       |              |
| Sentinel     | sentinel  | sentinel              | 5300       |              |
| Auth         | 授权        | chensoul-auth-service | 6666       |              |

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
mvn clean package -DskipTests=true && docker compose build && docker compose up -d

docker ps --format {{.Names}}
```

## 参考资料

- [Microservices with Spring Boot 3 and Spring Cloud](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition)

