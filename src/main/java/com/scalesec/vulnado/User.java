package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
  private static final Logger LOGGER = Logger.getLogger(User.class.getName());
  private String id;
  private String username;
  private String hashedPassword;

  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token);
    } catch(Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String username) {
    String query = "SELECT * FROM users WHERE username = ? LIMIT 1";
    try (Connection cxn = Postgres.connection();
         PreparedStatement stmt = cxn.prepareStatement(query)) {
      
      LOGGER.log(Level.INFO, "Opened database successfully");

      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String userId = rs.getString("user_id");
        String userName = rs.getString("username");
        String password = rs.getString("password");
        return new User(userId, userName, password);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage(), e);
    }
    return null;
  }
}