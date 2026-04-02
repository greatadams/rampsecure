package com.rampsecure.rampsecure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RampSecureApplication {

    public static void main(String[] args) {

        SpringApplication.run(RampSecureApplication.class, args);
    }

}
