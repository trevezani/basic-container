package br.com.rjconsultores.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping({"/hello"})
@RestController
public class TestController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test() {
        var json = new HashMap<String, Object>();
        json.put("message", "Hello!!!");

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
}
