package com.sce.data.gaia.utils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.sce.data.gaia.constant.CommonConstant;

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
    public static Any strToAny(String jsonStr) {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        if (jsonStr != null && !Objects.equals(CommonConstant.NULL_STR, jsonStr))
            return JsonIterator.deserialize(jsonStr);
        else
            return null;
    }

    /**
     * json str to Object,need cast to class
     *
     * @param jsonStr
     * @return Object
     */
    public static Object strToObject(String jsonStr) {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        if (jsonStr != null && !Objects.equals(CommonConstant.NULL_STR, jsonStr))
            return JsonIterator.deserialize(jsonStr,Object.class);
        else
            return null;
    }

}
