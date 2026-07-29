package org.airtribe.AuthenticationAuthorizationBelC20.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.airtribe.AuthenticationAuthorizationBelC20.util.JwtUtil;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(2)
public class JwtFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Validate the JWT Token for every incoming request
    // check the existence of the token -> Headers in the request
    // if you dont have it you reject the request
    // if you have the header -> Bearer
    // you validate the token signature and extract the payload
    // if the signature verfication completes successfully
    // filterChain.doNext()

    String token = request.getHeader("Authorization");

    if (token == null || token.isBlank() || token.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Missing authorization header");
      return;
    }

    Claims parsedClaims;
    try {
      parsedClaims = JwtUtil.validateJwtToken(token);
      String role = parsedClaims.get("roles", String.class);
      List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

      SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(parsedClaims.getSubject(), null, authorities));
    } catch (SignatureException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid Signature of the token");
      return;
    } catch (ExpiredJwtException exception) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token expired, please relogin");
      return;
    }
    System.out.println(parsedClaims);

    filterChain.doFilter(request, response);

  }

  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return request.getRequestURI().contains("/register") ||
        request.getRequestURI().contains("/signin")
        || request.getRequestURI().contains("/verifyRegistrationToken");
  }
}
