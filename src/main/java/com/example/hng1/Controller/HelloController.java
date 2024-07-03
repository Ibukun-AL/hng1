package com.example.hng1.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Map<String, String> hello(@RequestParam String visitor_name, HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

         // For development purposes, let's use a public IP for testing
         if (clientIp.equals("0:0:0:0:0:0:0:1") || clientIp.equals("127.0.0.1")) {
            clientIp = "8.8.8.8";  // Use Google's public DNS IP address for testing
        }

        // Fetch location details using ipinfo.io
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ipinfo.io/" + clientIp + "/json";
        @SuppressWarnings("unchecked")
        Map<String, String> locationData = restTemplate.getForObject(url, Map.class);

        String location = locationData != null ? locationData.get("city") : "Unknown";

        Map<String, String> response = new HashMap<>();
        response.put("client_ip", clientIp);
        response.put("location", location);
        response.put("greeting", "Hello, " + visitor_name.replace("\"", "") + "!, "+"the temperature is 11 degrees Celcius in "+ location);

        return response;
    }
}
