package com.se.softengineer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 使用 @Configuration 注解，表示这是一个配置类。
 * 它实现了 WebMvcConfigurer 接口，这个接口提供了一些方法来配置 Spring MVC。
 * 在这个类中，我们重写了 addCorsMappings 方法，这个方法用来配置跨域资源共享（CORS）。
 * 在这个方法中，我们使用 CorsRegistry 对象来配置 CORS。
 * 我们调用了 addMapping 方法来指定哪些 URL 需要支持 CORS。
 * 这里我们使用了通配符 “/”，表示所有的 URL 都需要支持 CORS。
 *
 * 然后我们调用了 allowedOriginPatterns 方法来指定允许的来源.
 * 这里我们使用了通配符“”，表示允许来自任何来源的请求。
 *
 * 接着我们调用了 allowCredentials 方法来指定是否允许发送 cookie 等凭证信息，这里我们设置为 true，表示允许发送凭证信息。
 *
 * 然后我们调用了 allowedMethods 方法来指定允许的 HTTP 方法，这里我们设置为 GET、POST、PUT、DELETE 和 OPTIONS。
 * 最后我们调用了 maxAge 方法来指定响应的缓存时间，这里我们设置为 3600 秒。
 * 这段代码的作用是配置 CORS，使得我们的应用程序可以接受来自任何来源的请求，并且允许发送凭证信息，支持 GET、POST、PUT、DELETE 和 OPTIONS方法，并且响应的缓存时间为 3600 秒
 **/
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600);
    }
}
