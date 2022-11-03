# 说明

## 使用场景

- 由于RPC框架的重试机制或者接口重复请求，导致平台出现重复的接口调用
- 因此，当需要多次调用达到一次执行的效果，则考虑使用本组件

## 使用约束

- 禁止用于查询接口
- 谨慎用于耗时很低（小于10毫秒）的方法，这种场景下，幂等能力会放大对方法性能的影响
- 框架是根据IdemInspect的参数生成幂等key，因此，在使用时要考虑幂等key的唯一性，否则，影响正常业务处理
- 采用幂等策略时，修饰方法的返回值必须要支持可序列化，否则，策略会异常并且无法生效

## 使用方法

- 添加pom依赖

```
        <dependency>
               <groupId>top.yueshushu</groupId>
                <artifactId>Idem</artifactId>
                <version>1.0-SNAPSHOT</version>
        </dependency>
```

- application.yml配置

```
idem:
    enable: true        #整个功能开关
    isolateGroup: xxx   #自定义隔离组
    cache:
        address: redis://192.168.100.148:6379   #redis地址
        password: zk123   #redis密码
        connectionPoolSize: 100 #连接池最大值
        connectionMinimumIdleSize: 50 #连接池最小值
        database: 1 #redis的index
```

- 接口设置幂等功能

```
    /**
    * 幂等key的生成规则：
    *    根据@IdemInspect的parseHttpURI、parseHttpMethod、httpHeadersName、httpParamsName、methodParamsName拼接而成
    *    使用时要保证生成的幂等key的唯一性，否则影响正常使用
    */
    
    //幂等策略
        @IdemInspect(
            policy = IdemChainPolicy.class,             #策略
            waitTimeSeconds = 100,                      #等待时间
            expiredTimeSeconds = 60,                    #过期时间
            methodParamsName = {"reqTest"},             #方法参数，不填时默认全取
            parseHttpURI = true,                        #http URI
            parseHttpMethod = true,                     #http Method
            httpParamsName = {"xxx"},                   #http paramter
            httpHeadersName = {"yyyy"}                  #http header
    )
    @RequestMapping(value = "/echo2/{test}", method = RequestMethod.GET)
    public String echo2(@PathVariable("test") String test, @RequestBody ReqTest reqTest) throws Exception {
     ... ...
    }
    
    //去重策略
    @IdemInspect(
            policy = DedupChainPolicy.class,              #策略
            expiredTimeSeconds = 120,                     #过期时间
            methodParamsName = {"reqTest"},               #方法参数，不填时默认全取
            parseHttpURI = true,                          #http URI
            parseHttpMethod = true,                       #http Method
            httpParamsName = {"xxx"},                     #http paramter
            httpHeadersName = {"yyyy"}                    #http header
    )
    @RequestMapping(value = "/echo2/{test}", method = RequestMethod.GET)
    public String echo2(@PathVariable("test") String test, @RequestBody ReqTest reqTest) throws Exception {
     ... ...
    }
```

## 设计思路

整体设计思路为：责任链 + SPI，灵感来源于Sentinel框架

IdemEntry提供入口， IdemContext提供上下文，IdemRequest提供请求参数

框架提供两种幂等策略： IdemChainPolicy和DedupChainPolicy

IdemChainPolicy - 幂等策略(默认策略)，当幂等Key已被使用，会取上一次执行结果；当幂等Key正在被使用，等待获取执行结果，责任链如下：

- UniqueIdParseProcessHandle -> ParamParseProcessHandle -> LogPrintProcessHandle -> ObtainIdemLockProcessHandle ->
  IdemCacheProcessHandle

DedupChainPolicy - 去重策略，当幂等Key已被使用或正在被使用，会拒绝请求，责任链如下：

- UniqueIdParseProcessHandle -> ParamParseProcessHandle -> DebupProcessHandle

框架已有的ProcessHandle：

- UniqueIdParseProcessHandle：解析方法参数的@IdemUniqueId注解
- ParamParseProcessHandle：解析入参各种参数，比如：URI、Method等
- LogPrintProcessHandle：打印数据
- ObtainIdemLockProcessHandle：获取幂等锁
- IdemCacheProcessHandle：获取和保存缓存的执行结果

## 扩展性

- 首先，对于IdemChainPolicy，通过SPI增加ProcessHandle方式扩展处理逻辑，比如：LogPrintProcessHandle

- 然后，通过SPI增加新的Policy，可以自定义新策略，比如：DedupChainPolicy

## 测试报告

- 见链接：https://note.youdao.com/s/Vz3iW5Ds



