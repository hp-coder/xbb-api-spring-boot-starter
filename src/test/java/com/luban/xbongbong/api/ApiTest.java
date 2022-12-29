package com.luban.xbongbong.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.sdk.customer.CustomerApi;
import com.luban.xbongbong.api.sdk.form.FormApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author HP 2022/12/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XbbApiAutoConfiguration.class)
public class ApiTest {

    @Test
    public void test_customer_get(){
        JSONArray formList = getFormList(null, 1, 100, 1, 100);
        System.out.println(formList.toJSONString());

//        // 表单id，根据表单模板列表接口查出
//        Long formId = 7174754L;
//
//        //获取字段解释-formId根据表单模板列表接口回参中获取
//        JSONArray explainList = getExplainList(formId, 100);
//        System.out.println(explainList.toJSONString());
//
//        //获取客户列表-formId根据表单模板列表接口回参中获取
//        JSONArray customerList = getCustomerList(formId,  1, 10);
//        System.out.println(customerList.toJSONString());
//
//
//        final JSONObject object = customerGet(106037143L);
//        System.out.println(object.toJSONString());


        //拼装客户数据
//		JSONObject dataList = new JSONObject();
//
//		String phoneTypeString1 = "工作";
//		String phoneTypeString2 = "手机";
//		String phone1 = "0571-87422223";
//		String phone2 = "13700000003";
//
//		JSONObject phoneObject1 = new JSONObject();
//		phoneObject1.put("text_1", phoneTypeString1);
//		phoneObject1.put("text_2", phone1);
//
//		JSONObject phoneObject2 = new JSONObject();
//		phoneObject2.put("text_1", phoneTypeString2);
//		phoneObject2.put("text_2", phone2);
//
//		JSONArray array = new JSONArray();
//		array.add(phoneObject1);
//		array.add(phoneObject2);
//
//		dataList.put("text_1", "王五");
//		dataList.put("text_16", ConfigConstant.USER_ID);
//		dataList.put("subForm_1", array);
//		//新建客户
//		Long dataId = customerAdd(formId, dataList);
//		System.out.println("id:" + dataId);
    }

    /**
     * 获取表单模版列表
     * @param name 模版名称模糊查询
     * @param businessType 业务类型
     * @param page 页码
     * @param pageNum 每页数
     * @return 接口回参
     */
    static JSONArray getFormList(String name, Integer saasMark, Integer businessType, Integer page, Integer pageNum){
        try {
            JSONArray formList = FormApi.list(name, saasMark, businessType, page, pageNum);
            return formList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * 获取表单解释
     * @param formId 表单id
     * @param businessType 业务类型
     * @return 接口回参-字段解释
     */
    static JSONArray getExplainList(Long formId, Integer businessType) {
        try {
            JSONArray explainList = FormApi.get(formId, businessType);
            return explainList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * 获取客户列表
     * @param formId 表单id
     * @param page 页码
     * @param pageNum 每页数据量
     * @return 接口回参-客户列表
     */
    static JSONArray getCustomerList(Long formId, Integer page, Integer pageNum) {
        try {
            JSONArray customerList = CustomerApi.list(formId,  page, pageNum);
            return customerList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * 新建客户
     * @param formId 表单id
     * @param dataList 客户数据
     * @return 接口回参
     */
    static Long customerAdd(Long formId, JSONObject dataList) {
        try {
            Long dataId = CustomerApi.add(formId, dataList);
            return dataId;
        } catch (XbbException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    static JSONObject customerGet(Long dataId){
        try {
            return CustomerApi.get(dataId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
