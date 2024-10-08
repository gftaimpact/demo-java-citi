 package com.scalesec.vulnado;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
  
  private static final String ID = "";
  private static final String USERNAME = ""; 
  private static final String BODY = "";
  private static final String CREATED_TIMESTAMP = "";

  public Comment(String id, String username, String body, Timestamp createdTimestamp) {
    this.ID = id;
    this.USERNAME = username;
    this.BODY = body;
    this.CREATED_TIMESTAMP = createdTimestamp;
  }

  public static Comment create(String username, String body){
    long time = new Date().getTime();
    Timestamp timestamp = new Timestamp(time);
    Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
    try (Connection con = Postgres.connection(); 
         PreparedStatement pStatement = con.prepareStatement(sql)) {
      if (comment.commit()) {
        return comment;
      } else {
        throw new BadRequest("Unable to save comment");
      }
    } catch (Exception e) {
      throw new ServerError(e.getMessage());
    }
  }

  public static List<Comment> fetchAll() {
    List<Comment> comments = new ArrayList();
    try (Connection con = Postgres.connection();
         Statement stmt = con.createStatement()) {

      String query = "select * from comments;";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String id = rs.getString("id");
        String username = rs.getString("username");
        String body = rs.getString("body");
        Timestamp createdTimestamp = rs.getTimestamp("created_on");
        Comment c = new Comment(id, username, body, createdTimestamp);
        comments.add(c);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Logger.error(e.getMessage());
    }
    return comments;
  }

  public static boolean delete(String id) {
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement(sql)) {
      pStatement.setString(1, id);
      return 1 == pStatement.executeUpdate();
    } catch(Exception e) {
      Logger.error(e.getMessage());
    } 
    return false;
  }

  private boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, createdTimestamp) VALUES (?,?,?,?)";
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement(sql)) {
      pStatement.setString(1, this.ID);
      pStatement.setString(2, this.USERNAME);
      pStatement.setString(3, this.BODY);    
      pStatement.setTimestamp(4, this.CREATED_TIMESTAMP);
      return 1 == pStatement.executeUpdate();
    }
  }
}