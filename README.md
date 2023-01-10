# xbb-api-spring-boot-starter

销帮帮CRM系统API及业务API

## 使用
springboot配置文件中增加配置, 具体参数可以通过https://pfweb.xbongbong.com/#/apiToken/index等获取

### 必须配置
详情参考配置类说明: `com.luban.xbongbong.api.biz.config.XbbBizConfig`
```yaml
xbb:
  gateway: https://proapi.xbongbong.com
  corp-id: ding81a031835ed7c67e35c2f4657eb6378f
  user-id: 28361647261063310
  token: ce42190bbfda01eafe6fadb2f83895cf
  webhook-token: 9c106a50fb6ee47331f4d97288ccfe51
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

`说明：由于销帮帮的关联表单并非通过关联键查询子表单数据，而是通过主键（销帮帮客户记录ID）关联，也就是说主表的记录只能是1，字表是多，无法实现多对多的场景`

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