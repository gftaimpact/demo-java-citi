package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class User {

  private static final String ID = "id";
  private static final String USERNAME = "username";
  private static final String HASHED_PASSWORD = "hashedPassword";
  
  private String id;
  private String username;
  private String hashedPassword;

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
      Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token);
    } catch(Exception e) {
      logger.error("Unauthorized", e);
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String username) {
    Statement statement = null; 
    User user = null;
    Connection connection = null;
    try {
      connection = Postgres.connection();
      statement = connection.createStatement();
      logger.info("Opened database successfully");

      String query = "SELECT * FROM users WHERE username = ? LIMIT 1";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setString(1, username);
      ResultSet resultSet = ps.executeQuery();
      if (resultSet.next()) {
        String userId = resultSet.getString("user_id"); 
        String userUsername = resultSet.getString("username");
        String password = resultSet.getString("password");
        user = new User(userId, userUsername, password);
      }
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      return null;
    } finally {
      try {
        if (statement != null) statement.close();
        if (connection != null) connection.close();
      } catch (Exception exception) {
        logger.error(exception.getMessage(), exception);
      }
    }
    return user;
  }
}