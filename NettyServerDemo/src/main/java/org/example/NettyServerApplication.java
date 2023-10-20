package org.example;

import org.example.server.NettyServerBootStrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {

    @Autowired
    private NettyServerBootStrap serverBootStrap;

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        serverBootStrap.start();
    }
}
