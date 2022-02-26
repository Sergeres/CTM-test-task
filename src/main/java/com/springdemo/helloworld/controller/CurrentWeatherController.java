package com.springdemo.helloworld.controller;

import com.springdemo.helloworld.dto.CurrentWeather;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Slf4j
@Controller
public class CurrentWeatherController {

    private final JdbcTemplate jdbcTemplate;
    private final Environment env;
    private final RestTemplate restTemplate;

    @Autowired
    public CurrentWeatherController(JdbcTemplate jdbcTemplate, Environment env, RestTemplateBuilder restTemplateBuilder){
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
        this.restTemplate = restTemplateBuilder.build();
    }


    ParseController parseController = new ParseController();

    Logger logger = LoggerFactory.getLogger(CurrentWeatherController.class);

    @GetMapping()
    public String getMainPage(Model model) {

        LinkedHashMap answer = getWeather().getBody();
        LinkedHashMap weather = parseController.parseWeather(answer);
        LinkedHashMap temperature = parseController.parseTemperature(answer);

        CurrentWeather currentWeather = new CurrentWeather((String) answer.get("name"), (String) weather.get("main"), (Double) temperature.get("temp") - 273.15, (Double) temperature.get("feels_like") - 273.15);
        model.addAttribute("currentWeather", currentWeather);
        return "main";
    }

    private ResponseEntity<LinkedHashMap> getWeather() {
        final String url = String.join("", "https://api.openweathermap.org/data/2.5/weather?lat=",
                env.getProperty("constant.lat"),
                "&lon=",
                env.getProperty("constant.lon"),
                "&appid=",
                env.getProperty("constant.api"));

        logger.debug("Request for api completed");

        return  restTemplate.getForEntity(url, LinkedHashMap.class);
    }

    public void putDataToDb(String loc, Double temp, Double feelsLike) {
        String sql = "INSERT INTO wheather (location, temp, temp_feels_like, lat, lon) VALUES ('" + loc + "', " + temp + ", " + feelsLike +  "," + env.getProperty("constant.lat") + "," + env.getProperty("constant.lon") + ")";
        int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            logger.info("A new row has been inserted.");
        }
    }

    @Scheduled (initialDelayString = "3000", fixedDelayString = "60000")
    public void getNewWeather () {

        LinkedHashMap answer  = getWeather().getBody();
        LinkedHashMap temperature = parseController.parseTemperature(answer);

        putDataToDb((String) answer.get("name"), (Double) temperature.get("temp") - 273.15, (Double) temperature.get("feels_like") - 273.15);
    }
}
