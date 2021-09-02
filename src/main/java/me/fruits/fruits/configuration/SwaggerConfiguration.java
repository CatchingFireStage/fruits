package me.fruits.fruits.configuration;

import com.fasterxml.classmate.TypeResolver;
import me.fruits.fruits.service.admin.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.*;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket adminApi(TypeResolver typeResolver) {

        ApiSelectorBuilder commentApiSelectorBuilder = this.getCommentApiSelectorBuilder("/admin.*");

        //全局添加参数
        List<RequestParameter> pars = new ArrayList<>();
        pars.add(new RequestParameterBuilder().name(LoginService.HEADER_TOKEN).description("前台登录的token").required(true).in(ParameterType.HEADER).build());

        return commentApiSelectorBuilder
                .build()
                .pathMapping("/")
                .groupName("后台api")
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()))
//                .enableUrlTemplating(true)
                //全局属性设置
                .globalRequestParameters(pars)
                .apiInfo(apiInfo());
    }


    @Bean
    public Docket apiApi(TypeResolver typeResolver) {
        ApiSelectorBuilder commentApiSelectorBuilder = this.getCommentApiSelectorBuilder("/api.*");


        //全局添加参数
        List<RequestParameter> pars = new ArrayList<>();
        pars.add(new RequestParameterBuilder().name(me.fruits.fruits.service.api.LoginService.HEADER_TOKEN).description("登录的token").in(ParameterType.HEADER).build());

        return commentApiSelectorBuilder
                .build()
                .pathMapping("/")
                .groupName("客户端api")
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()))
//                .enableUrlTemplating(true)
                //全局属性设置
                .globalRequestParameters(pars)
                .apiInfo(apiInfo());
    }


    @Bean
    public Docket notifyApi(TypeResolver typeResolver) {
        ApiSelectorBuilder commentApiSelectorBuilder = this.getCommentApiSelectorBuilder("/notify.*");

        return commentApiSelectorBuilder
                .build()
                .pathMapping("/")
                .groupName("三方通知api")
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()))
//                .enableUrlTemplating(true)
                .apiInfo(apiInfo());
    }

    /**
     * 获取公共的设置
     *
     * @param apiPrefix
     */
    private ApiSelectorBuilder getCommentApiSelectorBuilder(String apiPrefix) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                //paths和groupName搭配使用，可以进行多模块配置
                .paths(PathSelectors.regex(apiPrefix));
    }


    private ApiKey apiKey() {
        return new ApiKey("mykey", "api_key", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/anyPath.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference("mykey", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("水果店铺")
                .description("水果店铺api接口")
                .version("1.0")
                .build();
    }


}
