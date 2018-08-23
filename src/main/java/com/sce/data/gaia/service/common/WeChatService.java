package com.sce.data.gaia.service.common;


import com.sce.data.gaia.service.bo.WechatSendMsgBO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface WeChatService {
    @GET("gettoken")
    Call<Map<String,Object>> getWeChatToken(@Query("corpid") String corpid, @Query("corpsecret") String corpsecret);
    @POST("message/send")
    Call<Map<String,Object>> sendMsg(@Query("access_token") String token,@Body WechatSendMsgBO wechatSendMsgBO);
}
