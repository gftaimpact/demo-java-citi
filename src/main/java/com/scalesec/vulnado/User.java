package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class User {
  private String id, username, hashedPassword;

  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
    return jws;
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
    Statement stmt = null;
    User user = null;
    Connection cxn = null; // Incluido por GFT AI Impact Bot
    try {
      cxn = Postgres.connection(); // Alterado por GFT AI Impact Bot
      stmt = cxn.createStatement();
      Logger.getLogger(User.class.getName()).info("Opened database successfully");

      String query = "select * from users where username = '" + un + "' limit 1";
      Logger.getLogger(User.class.getName()).info(query);
      ResultSet rs = stmt.executeQuery("select * from users where username = ? limit 1");
      if (rs.next()) {
        String userId = rs.getString("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        user = new User(user_id, username, password);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Logger.getLogger(User.class.getName()).severe(e.getClass().getName()+": "+e.getMessage());
      return null; // Alterado por GFT AI Impact Bot
    } finally {
      try {
        if (stmt != null) stmt.close(); // Incluido por GFT AI Impact Bot
        if (cxn != null) cxn.close(); // Incluido por GFT AI Impact Bot
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return user; // Alterado por GFT AI Impact Bot
  }
}