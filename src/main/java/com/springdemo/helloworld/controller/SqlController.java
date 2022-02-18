package com.springdemo.helloworld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SqlController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void testinsert() {
        String sql = "INSERT INTO wheather (location, temp, temp_feels_like, lat, lon) VALUES ('Novaya Gollandiya', 274.46 - 273.15, 269.87 - 273.15, 59.9375, 30.3086)";
        int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
    }
}
