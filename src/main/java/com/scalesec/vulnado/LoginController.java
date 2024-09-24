package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.beans.factory.annotation.*;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class LoginController {
  @Value("${app.secret}")
  private String secret;

  @CrossOrigin(origins = "http://localhost:8080") // Replace with specific allowed origins
  @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
  LoginResponse login(@RequestBody LoginRequest input) {
    User user = User.fetch(input.username);
    if (Postgres.md5(input.password).equals(user.hashedPassword)) {
      return new LoginResponse(user.token(secret));
    } else {
      throw new Unauthorized("Access Denied");
    }
  }
}

class LoginRequest implements Serializable {
  private String username;
  private String password;
  public String getUsername() { return username; }
}
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
class LoginResponse implements Serializable {
  public void setPassword(String password) { this.password = password; }
  private String token;
  public String getToken() { return token; }
  public LoginResponse(String msg) { this.token = msg; }
  public void setToken(String token) { this.token = token; }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized extends RuntimeException {
  public Unauthorized(String exception) {
    super(exception);
  }
}
