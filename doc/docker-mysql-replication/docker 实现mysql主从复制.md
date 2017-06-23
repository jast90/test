## docker实现mysql主从复制
### 1. 下载mysql docker image
```docker
docker pull mysql
```
参考：
[镜像地址](https://hub.docker.com/_/mysql/)

### 2. 创建一个mysql容器
```docker
docker run -t --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD='123456' -d  mysql
```
### <span id="3">3. 实现主从复制</span>
- 创建主mysql服务器（指定配置文件）
```docker
docker run -t --name mysql-master -v /e/code/test/doc/docker-mysql-replication/mysql-cnf/master:/etc/mysql/conf.d -p 3306:3306 -e MYSQL_ROOT_PASSWORD='123456' -d mysql
```
- 创建从mysql服务器（指定配置文件）
```docker
docker run -t --name mysql-slave -v /e/code/test/doc/docker-mysql-replication/mysql-cnf/slave:/etc/mysql/conf.d -p 3307:3306 -e MYSQL_ROOT_PASSWORD='123456' -d mysql
```
遇到的坑：
`docker run -v`主机中某个目录被多次指定时会出现文件编程目录的问题？究竟什么原因暂未知。

### 4. mysql实现主从复制原理
通过binary logging来实现复制

#### 4.1 在哪配置？

`/etc/mysql/conf.d/`
#### 4.2 配置什么？  
在`/etc/mysql/conf.d/`目录下添加*.cnf文件（名字随便取）
- 主服务器配置（master.cnf）

```
[mysqld]
log-bin=mysql-bin 
server-id=1  
```
- 从服务器(slave.cnf)
```
[mysqld]
#log-bin=mysql-bin #没必要启用 binary logging除非 从服务器 要作为其他服务器的 主服务器
server-id=2 
```

#### 4.3 docker如何将上面的配置加入到之前创建的`mysql-master`及`mysql-slave`（废弃，请不要参考,实现请参考 [3. 实现主从复制](#3)）
直接将文件复制到容器的相应木目录下,然后重启容器
```docker
docker cp master.cnf mysql-master:/etc/mysql/my.cnf 
docker cp slave.cnf mysql-master:/etc/mysql/my.cnf 
``` 

#### 4.4 主服务器创建一个具有复制权相的用户
- docker进入mysql命令行
```docker
docker exec -it mysql-master mysql -uroot -p123456
```
- 创建复制权限用户并赋予相关权限
```sql
CREATE USER 'repl'@'%' IDENTIFIED BY '123456';       -- '%'意味着所有的终端都可以用这个用户登录
GRANT SELECT,REPLICATION SLAVE ON *.* TO 'repl'@'%'; -- SELECT权限是为了让repl可以读取到数据，生产环境建议创建另一个用户
```
- 从服务器连接到主服务器
```docker
docker exec -it mysql-slave mysql -uroot -p123456
```

```sql
CHANGE MASTER TO MASTER_HOST='192.168.99.100',MASTER_PORT=3308, MASTER_USER='repl', MASTER_PASSWORD='123456';
START SLAVE;
SHOW SLAVE STATUS\G       -- \G用来代替";"，能把查询结果按键值的方式显示
```
由于刚开始没有配置主服务器的端口导致如下错误运行结果
```
mysql> show slave status \G
*************************** 1. row ***************************
               Slave_IO_State: Connecting to master
                  Master_Host: 192.168.99.100
                  Master_User: repl
                  Master_Port: 3306
                Connect_Retry: 60
              Master_Log_File:
          Read_Master_Log_Pos: 4
               Relay_Log_File: d0f565d3c84f-relay-bin.000001
                Relay_Log_Pos: 4
        Relay_Master_Log_File:
             Slave_IO_Running: Connecting
            Slave_SQL_Running: Yes
              Replicate_Do_DB:
          Replicate_Ignore_DB:
           Replicate_Do_Table:
       Replicate_Ignore_Table:
      Replicate_Wild_Do_Table:
  Replicate_Wild_Ignore_Table:
                   Last_Errno: 0
                   Last_Error:
                 Skip_Counter: 0
          Exec_Master_Log_Pos: 0
              Relay_Log_Space: 154
              Until_Condition: None
               Until_Log_File:
                Until_Log_Pos: 0
           Master_SSL_Allowed: No
           Master_SSL_CA_File:
           Master_SSL_CA_Path:
              Master_SSL_Cert:
            Master_SSL_Cipher:
               Master_SSL_Key:
        Seconds_Behind_Master: 0
Master_SSL_Verify_Server_Cert: No
                Last_IO_Errno: 2003
                Last_IO_Error: error connecting to master 'repl@192.168.99.100:3306' - retry-time: 60  retries: 2
               Last_SQL_Errno: 0
               Last_SQL_Error:
  Replicate_Ignore_Server_Ids:
             Master_Server_Id: 0
                  Master_UUID:
             Master_Info_File: /var/lib/mysql/master.info
                    SQL_Delay: 0
          SQL_Remaining_Delay: NULL
      Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
           Master_Retry_Count: 86400
                  Master_Bind:
      Last_IO_Error_Timestamp: 170622 08:50:54
     Last_SQL_Error_Timestamp:
               Master_SSL_Crl:
           Master_SSL_Crlpath:
           Retrieved_Gtid_Set:
            Executed_Gtid_Set:
                Auto_Position: 0
         Replicate_Rewrite_DB:
                 Channel_Name:
           Master_TLS_Version:
1 row in set (0.00 sec)
```
正确运行结果
```
mysql> change master to
    -> master_host='192.168.99.100'
    -> master_port=3308
    -> master_user='repl';
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'master_port=3308
master_user='repl'' at line 3
mysql> change master to master_host='192.168.99.100',master_port=3308,master_user='repl',master_passwrod='123456';
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'master_passwrod='123456'' at line 1
mysql> change master to master_host='192.168.99.100',master_port=3308,master_user='repl',master_password='123456';
Query OK, 0 rows affected, 2 warnings (0.03 sec)

mysql> start slave
    -> ;
Query OK, 0 rows affected (0.00 sec)

mysql> show slave status \G
*************************** 1. row ***************************
               Slave_IO_State: Waiting for master to send event
                  Master_Host: 192.168.99.100
                  Master_User: repl
                  Master_Port: 3308
                Connect_Retry: 60
              Master_Log_File: mysql-bin.000001
          Read_Master_Log_Pos: 313
               Relay_Log_File: d0f565d3c84f-relay-bin.000002
                Relay_Log_Pos: 526
        Relay_Master_Log_File: mysql-bin.000001
             Slave_IO_Running: Yes
            Slave_SQL_Running: Yes
              Replicate_Do_DB:
          Replicate_Ignore_DB:
           Replicate_Do_Table:
       Replicate_Ignore_Table:
      Replicate_Wild_Do_Table:
  Replicate_Wild_Ignore_Table:
                   Last_Errno: 0
                   Last_Error:
                 Skip_Counter: 0
          Exec_Master_Log_Pos: 313
              Relay_Log_Space: 740
              Until_Condition: None
               Until_Log_File:
                Until_Log_Pos: 0
           Master_SSL_Allowed: No
           Master_SSL_CA_File:
           Master_SSL_CA_Path:
              Master_SSL_Cert:
            Master_SSL_Cipher:
               Master_SSL_Key:
        Seconds_Behind_Master: 0
Master_SSL_Verify_Server_Cert: No
                Last_IO_Errno: 0
                Last_IO_Error:
               Last_SQL_Errno: 0
               Last_SQL_Error:
  Replicate_Ignore_Server_Ids:
             Master_Server_Id: 1
                  Master_UUID: 2b3c5dee-5722-11e7-8ddb-0242ac110003
             Master_Info_File: /var/lib/mysql/master.info
                    SQL_Delay: 0
          SQL_Remaining_Delay: NULL
      Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
           Master_Retry_Count: 86400
                  Master_Bind:
      Last_IO_Error_Timestamp:
     Last_SQL_Error_Timestamp:
               Master_SSL_Crl:
           Master_SSL_Crlpath:
           Retrieved_Gtid_Set:
            Executed_Gtid_Set:
                Auto_Position: 0
         Replicate_Rewrite_DB:
                 Channel_Name:
           Master_TLS_Version:
1 row in set (0.00 sec)
```
### 5. 测试
在`mysql-master`执行如下操作
```
CREATE DATABASE test;
```
查看`mysql-slave`中也会创建相关的数据库，至此，主从备份搞定。

参考官网  
- [Chapter 16 Replication](https://dev.mysql.com/doc/refman/5.7/en/replication.html)
- [基于docker的MySQL主从复制（replication）](http://qinghua.github.io/mysql-replication/)


