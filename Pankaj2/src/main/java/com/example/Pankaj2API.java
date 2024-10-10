package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Pankaj2API {

    @GetMapping("/pankaj2")
    public String pankaj1() { return "Perfect 2";}

}
