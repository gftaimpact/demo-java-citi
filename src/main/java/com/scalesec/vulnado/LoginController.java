package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

  private static final String USERNAME = "admin";
  private static final String PASSWORD = "password123";
  private static final String TOKEN = "123abc456def";

  @PostMapping("/login")
  LoginResponse login(@RequestBody LoginRequest input) {
    if (input.username.equals(USERNAME) && Postgres.md5(input.password).equals(PASSWORD)) {
      return new LoginResponse(TOKEN);
    } else {
      throw new Unauthorized("Access Denied");  
    }
  }
}

class LoginRequest {
  public String username;
  public String password;
}

class LoginResponse {
  public String token;
  public LoginResponse(String token) {
    this.token = token;
  }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)  
class Unauthorized extends RuntimeException {
  public Unauthorized(String message) {
    super(message);
  }
}