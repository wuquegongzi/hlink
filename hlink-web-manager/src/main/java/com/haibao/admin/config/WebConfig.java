package com.haibao.admin.config;

import com.haibao.admin.interceptor.LoginAuthInterceptor;
import com.haibao.admin.interceptor.OperLogInterceptor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                //开启快速校验--默认校验所有参数，false校验全部
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }

    /**
     * 权限拦截
     * @return
     */
    @Bean
    public LoginAuthInterceptor getAuthInterceptor() {

        return new LoginAuthInterceptor();
    }

    /**
     * 操作日志拦截
     * @return
     */
    @Bean
    public OperLogInterceptor getOperLogInterceptor() {

        return new OperLogInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration addInterceptor = registry.addInterceptor(getAuthInterceptor());
        // 拦截配置
        addInterceptor.addPathPatterns("/**")
                // 排除配置
                .excludePathPatterns("/user/login").excludePathPatterns("/login**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/doc.html","/swagger**","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
                .excludePathPatterns("/**js").excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/monitoring**");

        //废弃拦截日志 实现，采用切面方式
//        InterceptorRegistration addOperLogInterceptor = registry.addInterceptor(getOperLogInterceptor());
//        addOperLogInterceptor.addPathPatterns("/**");
    }

}
