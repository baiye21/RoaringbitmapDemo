package com.demo.roaringbitmap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demo.roaringbitmap.db.mysql.dao")
public class RoaringbitmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoaringbitmapApplication.class, args);
    }

}
