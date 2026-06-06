package org.airtribe.LearnerManagementSystemBELC20.controller;

import org.airtribe.LearnerManagementSystemBELC20.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// API endpoint
@RestController
public class HelloWorldController {

  @Autowired
  private HelloWorldService _helloWorldService;


  @GetMapping("/")
  public String helloWorld() {
    return "Hello, World!";
  }

  @GetMapping("/greet")
  public String greet() {
    return _helloWorldService.getGreetMessage();
  }

}
