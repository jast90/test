## redis处理性能
```
redis-benchmark -c l -q -h 192.168.99.100
```
执行结果
```
C:\Users\lenovo>redis-benchmark -c l -q -h 192.168.99.100
PING_INLINE: 6911.33 requests per second
PING_BULK: 7336.76 requests per second
SET: 6518.90 requests per second
GET: 6895.13 requests per second
INCR: 6644.96 requests per second
LPUSH: 6564.70 requests per second
RPUSH: 6414.78 requests per second
LPOP: 6441.64 requests per second
RPOP: 6642.75 requests per second
SADD: 6775.53 requests per second
SPOP: 6989.10 requests per second
LPUSH (needed to benchmark LRANGE): 6539.79 requests per second
LRANGE_100 (first 100 elements): 5715.59 requests per second
LRANGE_300 (first 300 elements): 3118.67 requests per second
LRANGE_500 (first 450 elements): 2840.10 requests per second
LRANGE_600 (first 600 elements): 2794.70 requests per second
MSET (10 keys): 6643.64 requests per second
```
