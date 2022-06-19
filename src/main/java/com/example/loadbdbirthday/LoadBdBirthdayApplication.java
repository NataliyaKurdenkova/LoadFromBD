package com.example.loadbdbirthday;

import com.example.loadbdbirthday.service.LoaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadBdBirthdayApplication {

    public static void main(String[] args) {

        SpringApplication.run(LoadBdBirthdayApplication.class, args);
        LoaderService.start();
    }

}
