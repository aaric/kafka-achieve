package com.sample.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author Aaric, created on 2020-05-15T11:18.
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@SpringBootApplication
public class App implements CommandLineRunner {

    /**
     * Main
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("hello world");
    }
}
