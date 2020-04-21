package com.user.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UserManagementMongodbDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementMongodbDemoApplication.class, args);
    }

}
