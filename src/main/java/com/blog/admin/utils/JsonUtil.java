package com.blog.admin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blog.admin.entity.Log;

public class JsonUtil {

    /**
     * 字符串转json对象
     * @param data
     * @return
     */
    public static JSONObject String2JSON(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject data1 = jsonObject.getJSONObject("data");
        return data1;
    }

    /**
     * object转JSONObject
     * @param o
     * @return
     */
    public static JSONObject Object2JSON(Object o) {
        String s = JSONObject.toJSONString(o);
        JSONObject jsonObject = JSONObject.parseObject(s);
        return jsonObject;
    }

}
