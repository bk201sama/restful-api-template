package com.sce.data.gaia.config

import com.sce.data.gaia.constant.DefaultValues
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.*
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import java.util.ArrayList

/**
 * @author bk201
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Value("\${gaia.api.version}")
    private val apiVersion: String = DefaultValues.DEFAULT_API_VERSION

    @Bean
    fun api(): Docket {
        val tokenPar = ParameterBuilder()
        val pars = ArrayList<Parameter>()
        tokenPar.name("TOKEN").description("令牌").modelRef(ModelRef("string")).parameterType("header").required(true).build()
        pars.add(tokenPar.build())
        return Docket(DocumentationType.SWAGGER_2)
                .groupName("API")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api::class.java))
                .build()
                .globalOperationParameters(pars)
                .useDefaultResponseMessages(true)
    }

    fun apiInfo(): ApiInfo {
        return ApiInfo(
                "RESTFUL API",
                "提供restful api文档",
                apiVersion,
                "Terms of service",
                Contact("", "", ""),
                "License of API", "API license URL", emptyList<VendorExtension<Any>>())
    }
}
