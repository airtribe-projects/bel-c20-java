package org.airtribe.AuthenticationAuthorizationBelC20.service;

import java.util.Optional;
import org.airtribe.AuthenticationAuthorizationBelC20.entity.User;
import org.airtribe.AuthenticationAuthorizationBelC20.entity.VerificationToken;
import org.airtribe.AuthenticationAuthorizationBelC20.repository.UserRepository;
import org.airtribe.AuthenticationAuthorizationBelC20.repository.VerificationTokenRepository;
import org.airtribe.AuthenticationAuthorizationBelC20.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements UserDetailsService {
  @Autowired
  private UserRepository _userRepository;

  @Autowired
  private BCryptPasswordEncoder _passwordEncoder;

  @Autowired
  private VerificationTokenRepository _verificationTokenRepository;

  public User registerUser(User user) {
    String hashedPassword = _passwordEncoder.encode(user.getPassword());
    user.setPassword(hashedPassword);
    return _userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = _userRepository.findByUsername(username);
    if (!user.isPresent()) {
      throw new UsernameNotFoundException("User name " + username + " not found");
    }

    User fetchedUser = user.get();
    return org.springframework.security.core.userdetails.User.builder()
        .username(fetchedUser.getUsername())
        .password(fetchedUser.getPassword())
        .roles(fetchedUser.getRole())
        .disabled(!fetchedUser.isEnabled()).build();
  }

  public void saveVerificationToken(String verificationToken, User registerUser) {
    VerificationToken token = new VerificationToken();
    token.setToken(verificationToken);
    token.setUser(registerUser);
    token.setExpiryAt(new java.util.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
    _verificationTokenRepository.save(token);
  }

  // get the token
  // if the token is not found -> return "Invalid token"
  // if the token is found
  // check the expiry -> if the token is expired -> return "Token expired"
  // if the token is valid -> enable the user and return "User enabled successfully"
  // delete the token from the database
  public String verifyTokenAndEnableUser(String token) {
    Optional<VerificationToken> verificationTokenOptional = _verificationTokenRepository.findByToken(token);
    if (!verificationTokenOptional.isPresent()) {
      return "Invalid token";
    }

    VerificationToken verificationToken = verificationTokenOptional.get();
    if (verificationToken.getExpiryAt().before(new java.util.Date())) {
      _verificationTokenRepository.delete(verificationToken);
      return "Token has been expired, please re-register yourself";
    }

    User user = verificationToken.getUser();
    user.setEnabled(true);
    _verificationTokenRepository.delete(verificationToken);
    _userRepository.save(user);
    return  "User enabled successfully";
  }

  // You need to check the user exists
  // If the user exists -> If they are enabled
  // Then you would compare the credentials (hashes)
  // If the passwords match -> generate the JWT token by signing it with a client secret
  public String generateJwtToken(String username, String password) {
    Optional<User> userOptional = _userRepository.findByUsername(username);
    if (!userOptional.isPresent()) {
      return "User is not registered, please register";
    }

    User user = userOptional.get();
    if (!user.isEnabled()) {
      return "Please verify your registration";
    }

    Boolean isPasswordMatching = _passwordEncoder.matches(password, user.getPassword());
    if (!isPasswordMatching) {
      return "Invalid Credentials";
    }

    return JwtUtil.generateJwtToken(user);
  }
}
