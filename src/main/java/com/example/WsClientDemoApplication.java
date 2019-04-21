package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import com.example.wsClientDemo.component.SpringHelper;
import com.example.wsClientDemo.component.WsMonitor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAsync
@Slf4j
public class WsClientDemoApplication {
    
    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(WsClientDemoApplication.class, args);

        SpringHelper.setApplicationContext(run);
        try {
            WsMonitor monitor = new WsMonitor();
            Thread threadMonitor = new Thread(monitor);
            threadMonitor.start();
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
