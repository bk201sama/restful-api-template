package com.sce.data.gaia.core;

import com.google.common.base.Charsets;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.service.bo.WechatSendMsgBO;
import com.sce.data.gaia.service.common.WeChatService;
import com.sce.data.gaia.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Plugin(name = "WeChatAppender", category = "Core", elementType = "appender", printObject = true)
public class WeChatAppender extends AbstractAppender {
    private WeChatService weChatService;

    public WeChatAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, String host) {
        super(name, filter, layout, ignoreExceptions);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.WECHAT_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
       this.weChatService =  retrofit.create(WeChatService.class);
    }

    @Override
    public void append(LogEvent event) {
        final byte[] bytes = getLayout().toByteArray(event);
        try {
            Call<Map<String,Object>> call =  weChatService.getWeChatToken("xxxx","xxxx");
            Map<String,Object> tokenMap =  call.execute().body();
            WechatSendMsgBO wechatSendMsgBO = new WechatSendMsgBO();
            wechatSendMsgBO.setAgentid("xxxx");
            wechatSendMsgBO.setMsgtype("text");
            wechatSendMsgBO.setSafe(0);
            wechatSendMsgBO.setTouser("@all");
            Map<String,String> text = new HashMap<>();
            text.put("content",new String(bytes, Charsets.UTF_8));
            wechatSendMsgBO.setText(text);
            Call<Map<String,Object>> send = weChatService.sendMsg(String.valueOf(tokenMap.get("access_token")),wechatSendMsgBO);
            log.info("==============resquest"+send.request().url());
            log.info("==============response:"+JsonUtils.objectToStr(send.execute().body())) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PluginFactory
    public static WeChatAppender createAppender(@PluginAttribute("name") String name,
                                                @PluginAttribute("host") String host,
                                                @PluginElement("Filter") final Filter filter,
                                                @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("no name defined in conf.");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new WeChatAppender(name, filter, layout, ignoreExceptions, host);
    }

}
