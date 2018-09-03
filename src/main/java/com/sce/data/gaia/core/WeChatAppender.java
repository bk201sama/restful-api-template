package com.sce.data.gaia.core;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Charsets;
import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.service.bo.WechatGetTokenParamBO;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Plugin(name = "WeChatAppender", category = "Core", elementType = "appender", printObject = true)
public class WeChatAppender extends AbstractAppender {
    private WeChatService weChatService;
    private LoadingCache<WechatGetTokenParamBO, String> cache;

    public WeChatAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.WECHAT_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.weChatService = retrofit.create(WeChatService.class);
        this.cache = Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build(k -> {
                    Call<Map<String, Object>> call = weChatService.getWeChatToken(k.getCorId(), k.getCorSecret());
                    Map<String, Object> tokenMap = call.execute().body();
                    if (tokenMap != null) {
                        return String.valueOf(tokenMap.get(CommonConstant.WECHAT_ACCESS_TOKEN));
                    } else {
                        return "";
                    }
                });

    }

    @Override
    public void append(LogEvent event) {
        final byte[] bytes = getLayout().toByteArray(event);
        try {
            String serverIp = HttpUtils.getServerIp();
            //exclude dev net
            for (String ipe : CommonConstant.WECHAT_IP_EXCLUDE_LIST) {
                Pattern p = Pattern.compile(ipe);
                Matcher m = p.matcher(serverIp);
                if (m.matches()) {
                    return;
                }
            }

            WechatSendMsgBO wechatSendMsgBO = new WechatSendMsgBO();
            wechatSendMsgBO.setAgentid(CommonConstant.WECHAT_AGENT_ID);
            wechatSendMsgBO.setMsgtype("text");
            wechatSendMsgBO.setSafe(0);
            wechatSendMsgBO.setTouser("@all");
            Map<String, String> text = new HashMap<>();
            String sendStr = "host:" + serverIp + ",message:" + new String(bytes, Charsets.UTF_8);
            text.put("content", sendStr);
            wechatSendMsgBO.setText(text);
            WechatGetTokenParamBO wechatGetTokenParamBO = WechatGetTokenParamBO.builder().corId(CommonConstant.WECHAT_CORID).corSecret(CommonConstant.WECHAT_CORSECRET).build();
            Call<Map<String, Object>> send = weChatService.sendMsg(cache.get(wechatGetTokenParamBO), wechatSendMsgBO);
            Map<String, Object> ret = send.execute().body();
            if (ret != null) {
                int errorCode = (int) ret.get(CommonConstant.WECHAT_ERROR_CODE);
                if (!Objects.equals(errorCode, 0)) {
                    LOGGER.warn(ret.get(CommonConstant.WECHAT_ERROR_MSG));
                    switch (errorCode) {
                        case 42001: {
                            //access_token expire
                            cache.refresh(wechatGetTokenParamBO);
                            send = weChatService.sendMsg(cache.get(wechatGetTokenParamBO), wechatSendMsgBO);
                            ret = send.execute().body();
                            break;
                        }
                        default: {
                            Thread.sleep(5000);
                            send = weChatService.sendMsg(cache.get(wechatGetTokenParamBO), wechatSendMsgBO);
                            ret = send.execute().body();
                            break;
                        }
                    }
                }
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("==============resquest" + send.request().url());
                LOGGER.debug("==============response:" + JsonUtils.objectToStr(ret));
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
