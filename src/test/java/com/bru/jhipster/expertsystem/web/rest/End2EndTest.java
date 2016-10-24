package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.ExpertSystemApp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bral on 24.10.2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class End2EndTest {

    private static final RestTemplate template = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() throws IOException, InterruptedException {
        ExpertSystemApp.main(new String[]{});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String userJSON = "{\"username\": \"admin\", \"password\": \"admin\"}";
        HttpEntity<String> entity = new HttpEntity<>(userJSON, headers);
        // sign in
        String result =
            template.postForEntity("http://localhost:8080/api/authenticate", entity, String.class)
                .getBody();

        String token = (String) mapper.readValue(result, Map.class).get("id_token");
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((httpRequest, bytes, clientHttpRequestExecution) -> {
            httpRequest.getHeaders().add("Authorization", "Bearer " + token);
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        });
        template.setInterceptors(interceptors);
    }

    @Test
    public void fuckYou() {
        System.out.println("Oooooh looooooong Joooooohnson!!!!!");
        // it didn't work dude. Got and fuck yourself. So we're just testing the auth bit. Fuck you. Again.
    }
}
