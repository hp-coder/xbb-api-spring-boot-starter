# xbb-api-spring-boot-starter

销帮帮CRM系统API及业务API

## 说明
1. 由于销帮帮的关联表单并非通过关联键查询子表单数据，而是通过主键（销帮帮客户记录ID）关联，也就是说主表的记录只能是1，字表是多，无法实现多对多的场景`
2. **经销帮帮开发反馈, 带审核业务的模块, 其记录在审核通过后可以通过接口查询到, webhook事件回调机制也是一样, 在审核通过后能收到对应模块的事件通知.**
   1. 然而他们的垃圾文档又不写这些东西

## 使用
获取项目并安装
```shell
git clone http://47.106.251.114:3000/hup/xbb-api-spring-boot-starter
cd xbb-api-spring-boot-starter
mvn clean install
```
引入Maven坐标
```xml
<dependency>
    <groupId>com.luban</groupId>
    <artifactId>xbb-api-spring-boot-starter</artifactId>
    <version>3.0.0-SNAPSHOT</version>
</dependency>
```
### Springboot配置
详情参考配置类说明: 
- 系统配置
`com.luban.xbongbong.api.helper.config.ConfigConstant`
- 业务配置
`com.luban.xbongbong.api.biz.config.XbbBizConfig`

配置文件添加
```yaml
xbb:
  gateway: https://proapi.xbongbong.com
  corp-id: ding81a031835ed7c67e35c2f4657eb6378f
  user-id: 28361647261063310
  token: ce42190bbfda01eafe6fadb2f83895cf
  webhook-token: 9c106a50fb6ee47331f4d97288ccfe51
  enable-request-control: true
  request-per-day: 50000
  request-per-minute: 500
  write-per-second: 2
  biz:
    open-bid-form-name: "开标记录"
    bid-winning-name: "中标记录"
    customer-form-corp-id-field-name: "text_23"
    customer-form-id: 7174754
```
### SDK
销帮帮API的封装
- 自定义表单
- CRM客户
- 系统表单
- 系统用户 

详细参考：[销帮帮接口文档](http://profapi.xbongbong.com/#/apilist/181)
### BIZ API
系统业务API

接口：`com.luban.xbongbong.api.biz.XbbLubanBizApi`
- 通过qyId获取销帮帮客户id
- 添加一条开标记录
- 添加一条中标记录 

#### 同步调用逻辑
1. 通过乐标cms接口获取乐标用户分配的企业id集合:`ip:port/xbb/api/qyIds?random={Long}`
2. 通过本模块的`com.luban.xbongbong.api.biz.XbbLubanBizApi.getCustomerIdsByQyIds`获取企业id与销帮帮客户id的对应关系
3. 根据qyId获取对应业绩等外部数据，通过qyId对应的客户id列表，构建对应每个客户id的数据对象
4. 通过本模块的`com.luban.xbongbong.api.biz.XbbLubanBizApi.addOpenBid`或`com.luban.xbongbong.api.biz.XbbLubanBizApi.addBidWinning`添加对应数据

*注意：需要考虑尽量减少总的销帮帮API调用次数，所有接口指所有到达API的请求，即使被频次限制拦截的请求，同样会计算在所有接口频次限制中。*
- 写接口：3次/秒
- 所有接口：1000次/分钟
- 所有接口：100000(十万)次/天
