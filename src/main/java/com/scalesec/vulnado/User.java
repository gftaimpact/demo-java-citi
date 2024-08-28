package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class User {
  public String id, username, hashedPassword;

  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    // Define um tempo de expiração para o token
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    Date exp = new Date(nowMillis + 3600000); // Expira em 1 hora

    String jws = Jwts.builder()
      .setSubject(this.username)
      .setIssuedAt(now)
      .setExpiration(exp)
      .signWith(key)
      .compact();
    return jws;
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
      parser.parseClaimsJws(token);
    } catch(Exception e) {
      e.printStackTrace();
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    String query = "SELECT * FROM users WHERE username = ? LIMIT 1";
    try (
      Connection cxn = Postgres.connection();
      PreparedStatement pstmt = cxn.prepareStatement(query)
    ) {
      pstmt.setString(1, un);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          String user_id = rs.getString("user_id");
          String username = rs.getString("username");
          String password = rs.getString("password");
          return new User(user_id, username, password);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      return null;
    }
    return null; // Caso nenhum usuário seja encontrado
  }
}
