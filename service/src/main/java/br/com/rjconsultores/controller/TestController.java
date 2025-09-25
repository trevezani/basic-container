package br.com.rjconsultores.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

@RequestMapping({"/hello"})
@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Value("${message:Hello}")
    private String message;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test() {
        log.info("Request received at /hello endpoint ({})", LocalDateTime.now());

        var json = new HashMap<String, Object>();
        json.put("message", message);
        json.put("time", LocalDateTime.now().toString());

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
