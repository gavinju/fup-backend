package com.lty.config;

import com.lty.aop.IpBlacklistInterceptor;
import com.lty.constant.FileProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 全局跨域配置
 *
 * @author lty
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Resource
    private FileProperties fileProperties;

    @Resource
    private IpBlacklistInterceptor ipBlacklistInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送Cookie
                .allowCredentials(true)
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 设置允许的方法
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    /**
     * 拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipBlacklistInterceptor)
                .addPathPatterns("/file/**");
    }

    /**
     * 静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:" + fileProperties.getSavePath());
    }
}
