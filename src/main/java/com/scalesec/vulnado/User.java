package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class User {
  public String id, username, hashedPassword;

  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    } catch(Exception e) {
      throw new Unauthorized("Invalid token");
    }
  }

  public static User fetch(String username) {
    String query = "SELECT user_id, username, password FROM users WHERE username = ? LIMIT 1";
    try (Connection cxn = Postgres.connection(); 
         PreparedStatement stmt = cxn.prepareStatement(query)) {
         
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String user_id = rs.getString("user_id");
        String user_name = rs.getString("username");
        String password = rs.getString("password");
        return new User(user_id, user_name, password);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error fetching user", e);
    }
    return null; 
  }
}
