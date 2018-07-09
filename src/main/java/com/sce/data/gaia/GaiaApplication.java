package com.sce.data.gaia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author bk201
 */
@SpringBootApplication
@EnableCaching
@Slf4j
public class GaiaApplication {

    public static void main(String[] args) {
        ApplicationContext ctx =   new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(GaiaApplication.class)
                .run(args);
        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            log.info("project is start on profile:{}" , profile);
        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
