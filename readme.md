# xbb-api-spring-boot-starter

易用性封装

销帮帮CRM系统API: [销帮帮接口文档](http://profapi.xbongbong.com/#/apilist/181)

## Note
1. 由于销帮帮的关联表单并非通过关联键查询子表单数据，而是通过主键（销帮帮客户记录ID）关联，也就是说主表的记录只能是1，子表是多，无法实现多对多的场景`
2. *经销帮帮开发反馈, 带审核业务的模块, 其记录在审核通过后可以通过接口查询到, webhook事件回调机制也是一样, 在审核通过后能收到对应模块的事件通知.*
   1. 2023-12之前他们的webhook回调机制都存在重复发送“创建”事件的问题, 即使第一次“创建”事件已经正确响应了.

## Usage

### Install
```shell
git clone [repository]
cd xbb-api-spring-boot-starter
mvn clean install
```
引入Maven坐标
```xml
<dependency>
    <groupId>[groupId]</groupId>
    <artifactId>xbb-api-spring-boot-starter</artifactId>
    <version>1.0.3-sp2-SNAPSHOT</version>
</dependency>
```
### SpringBoot
详情参考配置类说明: 
- 系统配置
[XbbProperties.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2FXbbProperties.java)

配置文件添加
```yaml
xbb:
  gateway: # 默认 https://proapi.xbongbong.com 
  api-prefix: # 默认 pro
  api-version: # 默认 v2
  api-suffix: # 默认 api
  corp-id: # 必须
  user-id: # 必须
  token: # 必须
  webhook-token: # 必须
  enable-request-control: # true/false 是否开启客户端限流
  request-per-day: # 每日调用次数上限
  request-per-minute: # 每分调用次数上限
  write-per-second: # 每秒写接口调用次数上限
  max-retry: # 因限流未获取到ticket后重试次数
```
### Default

固定使用 [XbbWebhookController.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fcontroller%2FXbbWebhookController.java)
作为webhook回调入口. 自定义实现 [XbbWebhookEventProcessor.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fprocessor%2FXbbWebhookEventProcessor.java) 完成对事件的处理.

[RedissonBasedXbbRateLimiter.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fratelimiter%2FRedissonBasedXbbRateLimiter.java)
作为默认限流器, 可自定义实现 [XbbRateLimiter.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fratelimiter%2FXbbRateLimiter.java) 扩展

### SDK

URL配置 [XbbUrl.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fhelper%2FXbbUrl.java)

销帮帮API的封装
- 合同订单 [XbbContractApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Fcontract%2FXbbContractApi.java)
- 自定义表单 [XbbCustomFormApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Fcustomform%2FXbbCustomFormApi.java)
- 标签 [XbbLabelApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Flabel%2FXbbLabelApi.java)
- 回款单 [XbbPaymentSheetApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Fpaymentsheet%2FXbbPaymentSheetApi.java)
- CRM客户 [XbbCustomerApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Fcustomer%2FXbbCustomerApi.java)
- 系统表单 [XbbFormApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Fform%2FXbbFormApi.java)
- 系统用户 [XbbUserApi.java](src%2Fmain%2Fjava%2Fcom%2Fhp%2Fxbongbong%2Fapi%2Fservice%2Fuser%2FXbbUserApi.java)

*注意：需要考虑尽量减少总的销帮帮API调用次数，所有接口指所有到达API的请求，即使被频次限制拦截的请求，同样会计算在所有接口频次限制中。*

- 写接口：3次/秒
- 所有接口：1000次/分钟
- 所有接口：100000(十万)次/天
