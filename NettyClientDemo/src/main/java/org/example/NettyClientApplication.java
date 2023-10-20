package org.example;

import org.example.client.NettyClientBootStrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    @Autowired
    private NettyClientBootStrap clientBootStrap;

    public static void main( String[] args ) {
        SpringApplication.run(NettyClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        clientBootStrap.start();
    }
}
