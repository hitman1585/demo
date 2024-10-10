package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Pankaj1API {

    @GetMapping("/pankaj1")
    public String pankaj1() { return "Perfect 1";}

}
