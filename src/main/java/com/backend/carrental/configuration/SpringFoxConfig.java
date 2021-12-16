package com.backend.carrental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Car Rental Swagger Documentation")
                .description("\"The Car Management API implements a complex domain model flow to rental a car. " +
                        "The flow simulates a real world application in terms of having a variety of rental " +
                        "options and business rules.\"")
                .version("1.1.0")
                .build();
    }
    // for Swagger api doc generation
    // http://localhost:8080/v2/api-docs
    // http://localhost:8080/car-rental/api/swagger-ui.html#/
}
