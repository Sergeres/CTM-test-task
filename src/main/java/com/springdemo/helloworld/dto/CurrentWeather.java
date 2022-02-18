package com.springdemo.helloworld.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentWeather that = (CurrentWeather) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(temperature, that.temperature) &&
                Objects.equals(feelsLike, that.feelsLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, temperature, feelsLike);
    }
}
