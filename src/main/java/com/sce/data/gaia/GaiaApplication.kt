package com.sce.data.gaia

import com.alicp.jetcache.anno.config.EnableMethodCache
import com.sce.data.gaia.constant.CommonConstant
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * @author bk201
 */
@SpringBootApplication
@EnableMethodCache(basePackages = ["com.sce.data.gaia.service"])
class GaiaApplication {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowCredentials = true
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        corsConfiguration.addExposedHeader(CommonConstant.TOKEN)
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)
        return CorsFilter(urlBasedCorsConfigurationSource)
    }
}

private val log = LoggerFactory.getLogger(GaiaApplication::class.java)

fun main(args: Array<String>) {
    val ctx = SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .sources(GaiaApplication::class.java)
            .run(*args)
    val activeProfiles = ctx.environment.activeProfiles
    for (profile in activeProfiles) {
        log.info("project is start on profile:{}", profile)
    }
}



