package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.ExpertSystemApp;
import com.bru.jhipster.expertsystem.domain.ExpertSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
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
        //ExpertSystemApp.main(new String[]{});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String userJSON = "{\"username\": \"admin\", \"password\": \"admin\"}";
        HttpEntity<String> entity = new HttpEntity<>(userJSON, headers);
        // sign in
        String result =
            template.postForEntity("http://localhost:8080/api/authenticate", entity, String.class)
                .getBody();

        System.out.println("String result: " + result);

        String token = (String) mapper.readValue(result, Map.class).get("id_token");
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((httpRequest, bytes, clientHttpRequestExecution) -> {
            httpRequest.getHeaders().add("Authorization", "Bearer " + token);
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        });
        template.setInterceptors(interceptors);
    }


    /*
     * curl -H "Content-Type:application/json" -H "x-auth-token: user:1447234640550:c57b56a3b70351f66f74318706b90b92" -X POST localhost:8080/api/firmwares
     * -d '{"version":"curl", "firmware":"QUxFWA==", "firmwareContentType":"application/octetstream"}'
     */
    @Test
    public void stage1_testXmlUpload() throws IOException {
        URL url = new ClassPathResource("/loader/BlueBall.xml").getURL();
        String xml = Resources.toString(url, Charsets.UTF_8);
        assert(xml != null && xml.length() > 0);
        System.out.println(xml);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        ExpertSystem result = template.postForEntity("http://localhost:8080/api/expert-systems/upload",
            new HttpEntity<>(xml, headers),
            ExpertSystem.class).getBody();

        System.out.println("!!!" + result.getTitle());


     }
}
