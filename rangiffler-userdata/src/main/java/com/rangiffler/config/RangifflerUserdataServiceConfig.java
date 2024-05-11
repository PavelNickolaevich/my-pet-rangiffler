package com.rangiffler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

@EnableWs
@Configuration
public class RangifflerUserdataServiceConfig {

    private final String rangifflerUserdataBaseUri;

    public RangifflerUserdataServiceConfig(@Value("${rangiffler-userdata.base-uri}") String rangifflerUserdataBaseUri) {
        this.rangifflerUserdataBaseUri = rangifflerUserdataBaseUri;
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

//    @Bean(name = "userdata")
//    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userdataSchema) {
//        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
//        wsdl11Definition.setPortTypeName("NifflerUserdataPort");
//        wsdl11Definition.setLocationUri(rangifflerUserdataBaseUri + "/ws");
//        wsdl11Definition.setTargetNamespace(userdataSchema.getTargetNamespace());
//        wsdl11Definition.setSchema(userdataSchema);
//        return wsdl11Definition;
//    }
}
