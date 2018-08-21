package com.sce.data.gaia.utils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.sce.data.gaia.constant.CommonConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * use jsoniter,http://jsoniter.com/index.cn.html,http://jsoniter.com/java-features.cn.html#%E8%B6%85%E7%AE%80%E5%8D%95%E7%9A%84-api
 */
public class JsonUtils {
    /**
     * object to json str
     *
     * @param object
     * @return
     */
    public static String objectToStr(Object object) {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
        if (object != null) {
            return JsonStream.serialize(object);
        } else {
            return CommonConstant.NULL_STR;
        }
    }

    /**
     * json str to any,any can bind to detail class
     *
     * @param jsonStr
     * @return Any
     */
    public static Any strToObject(String jsonStr) {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
        if (jsonStr!=null&&!Objects.equals(CommonConstant.NULL_STR,jsonStr))
            return JsonIterator.deserialize(jsonStr);
        else
            return null;
    }

    public static void main(String[] args) {
        List<String> ret = new ArrayList<>();
        ret.add("1");
        System.out.println(JsonUtils.strToObject(null));
    }
}
