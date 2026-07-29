package org.airtribe.AuthenticationAuthorizationBelC20.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.airtribe.AuthenticationAuthorizationBelC20.entity.User;


public class JwtUtil {

  public static String generateJwtToken(User user) {

    return Jwts.builder().subject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000))
        .claim("roles", "ROLE_" + user.getRole())
        .claim("dummyValue", "1234")
        .claim("emailVerified", user.isEnabled())
        .signWith(SignatureAlgorithm.HS256, "AirtribeTestingC20AuthenticationAuthorizationAirtribeTestingC20AuthenticationAuthorizationAirtribeTestingC20AuthenticationAuthorization")
        .compact();
  }

  public static Claims validateJwtToken(String token) {
    Claims claims = Jwts.parser().setSigningKey("AirtribeTestingC20AuthenticationAuthorizationAirtribeTestingC20AuthenticationAuthorizationAirtribeTestingC20AuthenticationAuthorization")
        .build().parseClaimsJws(token).getPayload();

    return claims;
  }
}
