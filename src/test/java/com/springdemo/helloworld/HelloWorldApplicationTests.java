package com.springdemo.helloworld;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class HelloWorldApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    @Test
    void mainPageLoad() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Местоположения")))
                .andExpect(content().string(containsString("Показания погоды")))
                .andExpect(content().string(containsString("Температура")));
    }

    @Test
    void apiRequest() {
        final String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + env.getProperty("constant.lat") + "&lon=" + env.getProperty("constant.lon") + "&appid=" + env.getProperty("constant.api") ;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = restTemplate.getForEntity(url, LinkedHashMap.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
