/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.config;

import com.google.common.base.Predicate;
import static com.google.common.base.Predicates.or;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.*;
import pt.solvit.probe.server.interceptor.AuthInterceptor;
import pt.solvit.probe.server.interceptor.LoggingInterceptor;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author AnaRita
 */
@Configuration
@EnableSwagger2
@Controller
public class Config extends WebMvcConfigurationSupport{// implements WebMvcConfigurer {

    //interceptor
    @Autowired
    AuthInterceptor authInterceptor;

    @Autowired
    LoggingInterceptor loggingInterceptor;

    /*
    @Autowired
    UserRoleInterceptor userRoleInterceptor;
    */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        AppConfiguration.URL_LOGIN//,
                        //AppConfiguration.URL_GET_LOGGEDIN_USER
                );

    }




    //CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }




    //SWAGGER
    @Bean
    public Docket probeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(getApis())
                //.apis(RequestHandlerSelectors.any())
                //.paths(regex("/api.*"))
                .paths(PathSelectors.any())
                .build()
                //.useDefaultResponseMessages(false)
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("SolvitProbe API with PS Students")
                .description("This is the description for the SolvitProbe API with PS Students. The API is not yet completed. Soon.")
                .build();
    }

    private Predicate<RequestHandler> getApis() {
        // Defines the packages to be shown in the UI
        return or(
                RequestHandlerSelectors.basePackage("pt.solvit.probe.server.controller.impl")
        );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        //webjars?
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /*@RequestMapping(value = "/api")
    public String index() {
        return "redirect:swagger-ui.html";
    }*/
}
