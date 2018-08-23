package com.sce.data.gaia;

import com.google.common.base.Throwables;
import com.sce.data.gaia.service.bo.WechatSendMsgBO;
import com.sce.data.gaia.service.common.WeChatService;
import com.sce.data.gaia.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RetrofitTest {
    @Test
    public void testRetroFit() throws Exception{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://qyapi.weixin.qq.com/cgi-bin/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        WeChatService weChatService = retrofit.create(WeChatService.class);
        Call<Map<String,Object>> call =  weChatService.getWeChatToken("xxx","xxx");
        log.info("==============resquest"+call.request().url());
        Map<String,Object> tokenMap = call.execute().body();
        log.info("==============response:"+JsonUtils.objectToStr(tokenMap)) ;
        WechatSendMsgBO wechatSendMsgBO = new WechatSendMsgBO();
        wechatSendMsgBO.setAgentid("1000003");
        wechatSendMsgBO.setMsgtype("text");
        wechatSendMsgBO.setSafe(0);
        wechatSendMsgBO.setTouser("@all");
        Map<String,String> text = new HashMap<>();
        text.put("content","测试000");
        wechatSendMsgBO.setText(text);
        Call<Map<String,Object>> send = weChatService.sendMsg(String.valueOf(tokenMap.get("access_token")),wechatSendMsgBO);
        log.info("==============resquest"+send.request().url());
        log.info("==============response:"+JsonUtils.objectToStr(send.execute().body())) ;
    }
    @Test
    public void testError(){
        log.error("测试错误");
        System.out.println("111");
    }
}
