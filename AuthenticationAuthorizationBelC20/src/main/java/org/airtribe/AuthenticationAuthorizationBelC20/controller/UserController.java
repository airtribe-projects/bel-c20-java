package org.airtribe.AuthenticationAuthorizationBelC20.controller;

import java.util.UUID;
import org.airtribe.AuthenticationAuthorizationBelC20.entity.User;
import org.airtribe.AuthenticationAuthorizationBelC20.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

  @Autowired
  private AuthenticationService _authenticationService;


  @PostMapping("/register")
  public User registerUser(@RequestBody User user) {
    User registerUser = _authenticationService.registerUser(user);
    String verificationToken = UUID.randomUUID().toString();
    _authenticationService.saveVerificationToken(verificationToken, registerUser);
    System.out.println("Verification token url: " + "http://localhost:2001/verifyRegistrationToken?token=" + verificationToken);
    return registerUser;
  }

  @PostMapping("/verifyRegistrationToken")
  public String verifyRegistrationToken(@RequestParam String token) {
    return _authenticationService.verifyTokenAndEnableUser(token);
  }

  @PostMapping("/signin")
  public String signin(@RequestParam("username") String username, @RequestParam("password") String password) {
    return _authenticationService.generateJwtToken(username, password);
  }

  @GetMapping("/hello")
  @PreAuthorize("hasAnyRole('admin')")
  public String hello() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName());
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Authenticated user " + authentication.getPrincipal());
    System.out.println("Authenticated user " + authentication.getAuthorities());
    return "Not authorized to access this endpoint";
  }
}
