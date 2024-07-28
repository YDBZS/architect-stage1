package org.architect.oss.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 配置Swagger2
 *
 * @author 多宝
 * @since 2021/3/13 16:45
 */
@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.
                        basePackage("org.architect.oss.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货电商后端API")
                .description("专为天天吃货提供的API文档")
                .version("1.0.0")
                .termsOfServiceUrl("https://www.duobao.com")
                .build();
    }

}
