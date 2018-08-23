package com.sce.data.gaia.config;

import com.sce.data.gaia.constant.CommonConstant;
import com.sce.data.gaia.service.common.WeChatService;
import org.springframework.context.annotation.Bean;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    @Bean
    WeChatService getWeChatService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstant.WECHAT_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(WeChatService.class);
    }
}
