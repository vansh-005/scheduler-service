package com.example.schedularservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

@SpringBootApplication
@EnableScheduling
public class SchedularServiceApplication   {

    public static void main(String[] args) {
        SpringApplication.run(SchedularServiceApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception{
//        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5,
//                new ThreadFactory() {
//                    @Override
//                    public Thread newThread(Runnable r) {
//                        System.out.println("New Thread");
//                        return new Thread(r, "New Thread") ;
//                    }
//                }
//        );
//    }

}
