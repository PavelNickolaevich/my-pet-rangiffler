package com.rangiffler;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.rangiffler.service.PropertiesLogger;

@SpringBootApplication
public class RangifflerUserdataApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RangifflerUserdataApplication.class);
        springApplication.addListeners(new PropertiesLogger());
        springApplication.run(args);
    }

}
