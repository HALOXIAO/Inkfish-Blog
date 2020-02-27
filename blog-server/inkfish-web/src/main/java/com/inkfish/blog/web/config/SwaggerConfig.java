package com.inkfish.blog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

/**
 * @author HALOXIAO
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, Arrays.asList(
                                new ResponseMessageBuilder().code(401).message("无").build(),
                        new ResponseMessageBuilder().code(403).message("无").build(),
                        new ResponseMessageBuilder().code(404).message("无").build()
                        )
                )
                .globalResponseMessage(RequestMethod.POST,Arrays.asList(
                        new ResponseMessageBuilder().code(201).message("无").build(),
                        new ResponseMessageBuilder().code(401).message("无").build(),
                        new ResponseMessageBuilder().code(403).message("无").build(),
                        new ResponseMessageBuilder().code(404).message("无").build()
                        ))
                .globalResponseMessage(RequestMethod.DELETE,Arrays.asList(
                        new ResponseMessageBuilder().code(204).message("无").build(),
                        new ResponseMessageBuilder().code(401).message("无").build(),
                        new ResponseMessageBuilder().code(403).message("无").build()
                ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Inkfish Blog",
                "欢迎来github点赞",
                "API V1.0",
                "",
                "",
                "",
                ""
        );
    }

}
