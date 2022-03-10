package com.smart.ecommerce.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
                .groupName("administration-connector").apiInfo(apiInfo())
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any()).build()
                .tags(
                        new Tag("label-system", "Set of services that perform the administration of system labels."),
                        new Tag("menu-system", "Set of services that perform system menu administration."),
                        new Tag("user-system", "Set of services for system user administration."),
                        new Tag("client", "Set of services for CRUD of clients."),
                        new Tag("group", "Set of services for CRUD of groups.")
                )
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
	}
	
	private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }
	
	private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }
	
	private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("apiKey", authorizationScopes));
        }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder().title("AdministrationConnector")
                .description("Microservice for system administration"
                        + " on the SMART E-Commerce platform.")
                .version("1.0").build();
    }
	
}
