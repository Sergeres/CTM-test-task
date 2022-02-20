package com.springdemo.helloworld.controller;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Controller
public class ParseController {

    public LinkedHashMap parseTemperature (LinkedHashMap response) {
        return (LinkedHashMap) response.get("main");
    }

    public LinkedHashMap parseWeather (LinkedHashMap response) {
        ArrayList<LinkedHashMap> arr = (ArrayList<LinkedHashMap>) response.get("weather");
        return arr.get(0);
    }
}
