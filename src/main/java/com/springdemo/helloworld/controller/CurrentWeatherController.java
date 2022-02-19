package com.springdemo.helloworld.controller;

import com.springdemo.helloworld.dto.CurrentWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Autowired
    private Environment env;

    @GetMapping()
    public String getMainPage(Model model) {

        LinkedHashMap answer = getWeather().getBody();
        LinkedHashMap weather = parseWeather(answer);
        LinkedHashMap temperature = parseTemperature(answer);

        CurrentWeather currentWeather = new CurrentWeather((String) answer.get("name"), (String) weather.get("main"), (Double) temperature.get("temp") - 273.15, (Double) temperature.get("feels_like") - 273.15);
        model.addAttribute("currentWeather", currentWeather);
        return "main";
    }

    private ResponseEntity<LinkedHashMap> getWeather() {
        final String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + env.getProperty("constant.lat") + "&lon=" + env.getProperty("constant.lon") + "&appid=" + env.getProperty("constant.api") ;
        RestTemplate restTemplate = new RestTemplate();
        return  restTemplate.getForEntity(url, LinkedHashMap.class);
    }

    public void putDataToDb(String loc, Double temp, Double feelsLike) {
        String sql = "INSERT INTO wheather (location, temp, temp_feels_like, lat, lon) VALUES ('" + loc + "', " + temp + ", " + feelsLike +  "," + env.getProperty("constant.lat") + "," + env.getProperty("constant.lon") + ")";
        int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
    }

    @Scheduled (initialDelayString = "3000", fixedDelayString = "60000")
    public void getNewWeather () {

        LinkedHashMap answer  = getWeather().getBody();
        LinkedHashMap temperature = parseTemperature(answer);

        putDataToDb((String) answer.get("name"), (Double) temperature.get("temp") - 273.15, (Double) temperature.get("feels_like") - 273.15);
    }

    public LinkedHashMap parseTemperature (LinkedHashMap response) {
        return (LinkedHashMap) response.get("main");
    }

    public LinkedHashMap parseWeather (LinkedHashMap response) {
        ArrayList<LinkedHashMap> arr = (ArrayList<LinkedHashMap>) response.get("weather");
        return arr.get(0);
    }
}
