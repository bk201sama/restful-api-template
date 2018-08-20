package com.sce.data.gaia;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sce.data.gaia.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.persistence.EntityManager;

/**
 * @author bk201
 */
@SpringBootApplication
@Slf4j
@EnableMethodCache(basePackages = "com.sce.data.gaia.service")
@MapperScan("com.sce.data.gaia.mapper")
public class GaiaApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(GaiaApplication.class)
                .run(args);
        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            log.info("project is start on profile:{}", profile);
        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS setting,take care safe question
     *
     * @return CorsFilter
     */
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader(CommonConstant.TOKEN);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    /**
     * QueryDSL support
     * @param entityManager
     * @return jpaQuery
     */
    @Bean
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
