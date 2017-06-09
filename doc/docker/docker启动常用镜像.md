# docker nginx 
## 启动 nginx
```
$ docker run -d -p 81:80 mynginx
```
# docker tomcat
## 启动tomcat
```
docker run -it --rm -p 8080:8080 mytomcat
docker run -it --rm -p 8081:8080 mytomcat
```
#  docker redis 
## 启动redis
```
docker run --name jast/redis -p 6379:6379 -d redis 
```
## 客户端连接redis
```
docker run -it --link jast-redis --rm redis redis-cli -h 192.168.99.100 -p 6379
docker run -it  --rm redis redis-cli -h 192.168.35.27 -p 6379 

```