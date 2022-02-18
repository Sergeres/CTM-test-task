package com.springdemo.helloworld.controller;

import com.springdemo.helloworld.dto.CurrentWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Controller
public class CurrentWeatherController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping()
    public String getMainPage(Model model) {
        final String url = "https://api.openweathermap.org/data/2.5/weather?lat=59.937500&lon=30.308611&appid=32f50aa50c0a9f5120a6de75767885e4";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.getForEntity(url, LinkedHashMap.class);

        testinsert();

        LinkedHashMap answer = response.getBody();
        ArrayList<LinkedHashMap> arr = (ArrayList<LinkedHashMap>) answer.get("weather");
        LinkedHashMap arrH = arr.get(0);
        LinkedHashMap main = (LinkedHashMap) answer.get("main");

        CurrentWeather currentWeather = new CurrentWeather((String) answer.get("name"), (String) arrH.get("main"), (Double) main.get("temp") - 273.15, (Double) main.get("feels_like") - 273.15);
        model.addAttribute("currentWeather", currentWeather);
        return "main";
    }

    public void testinsert() {
        String sql = "INSERT INTO wheather (location, temp, temp_feels_like, lat, lon) VALUES ('Novaya Gollandiya', 274.46 - 273.15, 269.87 - 273.15, 59.9375, 30.3086)";
        int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
    }
}
