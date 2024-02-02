package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class User {
  private static final String ID = "id";
  private static final String USERNAME = "username";
  private static final String HASHED_PASSWORD = "hashedPassword";

  public User(String id, String username, String hashedPassword) {
    this.ID = id;
    this.USERNAME = username;
    this.HASHED_PASSWORD = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.USERNAME).signWith(key).compact();
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token);
    } catch(Exception e) {
      e.printStackTrace();
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    PreparedStatement stmt = null;
    User user = null;
    Connection cxn = null;
    try {
      cxn = Postgres.connection();
      stmt = cxn.prepareStatement("select * from users where username = ? limit 1");
      stmt.setString(1, un);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String userId = rs.getString("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        user = new User(userId, username, password);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (cxn != null) cxn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return user;
  }
}