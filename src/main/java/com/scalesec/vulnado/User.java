package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import javax.sql.DataSource;

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
    } catch (Exception e) {
      throw new Unauthorized("Token is invalid: " + e.getMessage());
    }
  }

  public static User fetch(String un) {
    String query = "SELECT * FROM users WHERE username = ? LIMIT 1";
    try (Connection cxn = Postgres.connection();
         PreparedStatement stmt = cxn.prepareStatement(query)) {
      
      stmt.setString(1, un);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String user_id = rs.getString("user_id");
          String username = rs.getString("username");
          String password = rs.getString("password");
          return new User(user_id, username, password);
        }
      }
    } catch (SQLException e) {
      System.err.println("Database error: " + e.getMessage());
    }
    return null;
  }
}
