## 淡竹项目

SpringBoot 端 为小程序端 danzhu-mini 提供 API

## 部署流程

- 本地打包

```sh
$ mvn clean package -Dmaven.test.skip=true
```

- 传输到服务器的 app 目录下

```sh
$ scp ./target/*.jar root@xx.xxx.xx.xx:/app
```

- 线上运行 jar 包

```sh
$ nohup java -jar /app/danzhu-*.jar > /logs/danzhu.log 2>&1 &
```
