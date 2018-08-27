package com.sce.data.gaia.core;

import com.google.common.base.Charsets;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.service.bo.WechatSendMsgBO;
import com.sce.data.gaia.service.common.WeChatService;
import com.sce.data.gaia.utils.HttpUtils;
import com.sce.data.gaia.utils.JsonUtils;
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

@Plugin(name = "WeChatAppender", category = "Core", elementType = "appender", printObject = true)
public class WeChatAppender extends AbstractAppender {
    private WeChatService weChatService;

    public WeChatAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.WECHAT_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.weChatService = retrofit.create(WeChatService.class);
    }

    @Override
    public void append(LogEvent event) {
        final byte[] bytes = getLayout().toByteArray(event);
        try {
            Call<Map<String, Object>> call = weChatService.getWeChatToken("xxx", "xxx");
            Map<String, Object> tokenMap = call.execute().body();
            if (tokenMap != null) {
                WechatSendMsgBO wechatSendMsgBO = new WechatSendMsgBO();
                wechatSendMsgBO.setAgentid("xxx");
                wechatSendMsgBO.setMsgtype("text");
                wechatSendMsgBO.setSafe(0);
                wechatSendMsgBO.setTouser("@all");
                Map<String, String> text = new HashMap<>();
                StringBuilder sendStr = new StringBuilder("host:");
                sendStr.append(HttpUtils.getServerIp());
                sendStr.append("message:");
                sendStr.append(new String(bytes, Charsets.UTF_8));
                text.put("content",sendStr.toString());
                wechatSendMsgBO.setText(text);
                Call<Map<String, Object>> send = weChatService.sendMsg(String.valueOf(tokenMap.get("access_token")), wechatSendMsgBO);
                Map<String, Object> ret = send.execute().body();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("==============resquest" + send.request().url());
                    LOGGER.debug("==============response:" + JsonUtils.objectToStr(ret));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PluginFactory
    public static WeChatAppender createAppender(@PluginAttribute("name") String name,
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
        return new WeChatAppender(name, filter, layout, ignoreExceptions);
    }

}
