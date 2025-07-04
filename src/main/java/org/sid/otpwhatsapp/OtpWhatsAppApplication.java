package org.sid.otpwhatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OtpWhatsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtpWhatsAppApplication.class, args);
    }

}
