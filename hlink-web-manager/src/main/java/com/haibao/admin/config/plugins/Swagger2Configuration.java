package com.haibao.admin.config.plugins;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpSession;

/**
 * @ClassName Swagger2Configuration
 * @Description 访问地址：http://localhost:8080/doc.html
 * @Author ml.c
 * @Date 2020/2/26 11:48 上午
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@Profile({"dev","test"})  //备用,防止上生产不修改配置
public class Swagger2Configuration {

    @Value("${swagger.enable:false}")
    private Boolean enable;

    //api接口包扫描路径
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.haibao.admin.web.controller";
    public static final String VERSION = "1.0.0";

    @Bean(value = "defaultApi2")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(apiInfo())
                //分组名称
                .groupName(VERSION)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build()
                //添加忽略类型
                .ignoredParameterTypes(HttpSession.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("Hlink实时特征加工服务")
                // 设置文档的描述
                .description("Hlink实时特征加工 API 接口文档")
                // 设置文档的版本信息-> 1.0.0 Version information
                .version(VERSION)
                // 设置文档的License信息->1.3 License information
                .termsOfServiceUrl("http://localhost:8080/")
                .contact(new Contact("leon","","ml.c@haibao.net"))
                .build();
    }
}
