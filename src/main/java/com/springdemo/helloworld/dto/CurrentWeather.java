package com.springdemo.helloworld.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CurrentWeather implements Serializable {

    private String location;
    private String description;
    private Double temperature;
    private Double feelsLike;

    public CurrentWeather(String location, String description, Double temperature, Double feelsLike) {
        this.location = location;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
    }
}
