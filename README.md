# chensoul-cloud

基于 Jdk 1.8、SpringBoot 2.7.18、SpringCloud 2021.0.9 和 Spring Cloud Alibaba 2021.0.6.0 且采用前后端分离的微服务多租户体系架构。

## 项目介绍

### 技术选型

Spring Cloud每个业务领域都有多个可供选择的组件，这里也列出了微服务章节中将要用到的组件+中间件的技术选型，这也是当前主流的选型。

⚠️注意：JDK 使用的是 1.8，Spring Boot 只能使用 2.x 版本。

| 模块名称   | 技术选型                               | 使用版本       | 最新版本                                                                                                                                                                                                                                       | 端口         | web 访问地址               | 用户名/密码      |
|--------|------------------------------------|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|------------------------|-------------|
| 语言     | Java                               | 1.8        |                                                                                                                                                                                                                                            |            |                        |             |
| 构建工具   | Maven                              | 3.9.6      |                                                                                                                                                                                                                                            |            |                        |             |
| 数据库    | MySQL                              | 8          |                                                                                                                                                                                                                                            | 3306       |                        | user/123456 |
| 缓存     | Redis                              | 7          |                                                                                                                                                                                                                                            | 6379       |                        | 123456      |
| 消息队列   | Rabbitmq                           | 3.13.0     |                                                                                                                                                                                                                                            | 5672、15672 | http://localhost:15672 | user/123456 |
| 服务治理   | Spring Cloud Netflix Eureka        | 2021.0.9   | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">     | 8761       | http://localhost:8761  | user/123456 |
| 配置中心   | Spring Cloud Config                | 2021.0.9   | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">     | 8888       | http://localhost:8888  | user/123456 |
| 服务网关   | Spring Cloud Gateway               | 2021.0.9   | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">     | 8443       | http://localhost:8443  |             |
| 消息总线   | Spring Cloud Bus                   | 2021.0.9   | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">     |            |                        |             |
| 调用链追踪  | Spring Cloud Sleuth + Zipkin       | 2.2.8      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://s01.oss.sonatype.org/content/repositories/releases/org/springframework/cloud/spring-cloud-starter-zipkin/maven-metadata.xml"> | 9411       | http://localhost:9411  |             |
| 服务容错   | Spring Cloud Alibaba Sentinel      | 2021.0.6.0 | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml">       | 5300       | http://localhost:5300  | user/123456 |
| 认证授权   | Spring OAuth2 Authorization Server | 0.4.5      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=0.&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/spring-security-oauth2-authorization-server/maven-metadata.xml">              | 9999       | http://localhost:9999  | user/pw     |
| 服务监控   | Spring Boot Admin                  | 2.7.15     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2.&metadataUrl=https://oss.sonatype.org/content/repositories/releases/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml">                 | 5100       | http://localhost:5100  | user/123456 |
| 任务调度   | Xxl Job                            | 2.4.0      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/xuxueli/xxl-job/maven-metadata.xml">                                                            | 5200       | http://localhost:5200  | user/123456 |
| 在线文档   | SpringDoc OpenApi                  | 1.8.0      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/org/springdoc/springdoc-openapi-ui/maven-metadata.xml">                                             |            | http://localhost:9000  |             |
| 分布式日志  | ELK                                |            |                                                                                                                                                                                                                                            |            |                        |             |
| 数据库持久化 | mybatis-plus                       | 3.5.5      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/baomidou/mybatis-plus-boot-starter/maven-metadata.xml">                                         |            |                        |             |
| 分布式锁   | redisson                           | 3.27.2     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/org/redisson/redisson/maven-metadata.xml">                                                          |            |                        |             |
| 验证码    | redisson                           | 1.6.2      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&metadataUrl=https://oss.sonatype.org/content/repositories/releases/com/github/whvcse/easy-captcha/maven-metadata.xml">                                                 |            |                        |             |


## 快速开始

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

### Idea 中运行

首先需要配置 hosts 文件
```bash
127.0.0.1 mysql
127.0.0.1 mysql-master
127.0.0.1 mysql-slave

127.0.0.1 redis
127.0.0.1 rabbitmq

127.0.0.1 config-server
127.0.0.1 auth-server
127.0.0.1 nacos
127.0.0.1 eureka
127.0.0.1 gateway
127.0.0.1 zipkin
```

其次，启动 mysql、redis、rabbitmq、zipkin 服务。

然后，依次启动 `chensoul-cloud` 项目中的以下模块：

- config-server
- eureka
- gateway
- auth-server

### 通过 docker 运行

首先，编译项目并打包

```bash
mvn clean package -DskipTests=true
```

运行 docker compose 文件

```bash
docker compose up -d
```

查看日志：
```bash
docker compose logs -f
```

查看启动的容器：

```bash
docker ps --format {{.Names}}
```

停止容器：

```bash
docker compose down

docker compose down --remove-orphans
```

重启某个微服务：

```bash
docker-compose restart admin-server
```

扩容微服务：

```bash
docker-compose up -d --scale admin-server=2
```

回收 docker 磁盘空间：

```bash
docker system prune -f --volumes
```

### 访问服务

- [Config Server](http://localhost:8888)
- [Eureka](http://localhost:8761)
- [Gateway & OpenAPI](http://localhost:8443)
- [Zipkin](http://localhost:9411)
- [Spring Boot Admin](http://localhost:5100)
- [Auth Server](http://localhost:9999)
- [Xxl Job](http://localhost:5200)
- [RabbitMQ](http://localhost:15672)

## 参考资料

- [Microservices with Spring Boot 3 and Spring Cloud](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition)

