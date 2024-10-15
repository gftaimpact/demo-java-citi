 package com.scalesec.vulnado;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
  
  private static final String id;
  private static final String username; 
  private static final String body;
  private static final Timestamp createdTimestamp;

  public Comment(String id, String username, String body, Timestamp createdTimestamp) {
    this.id = id;
    this.username = username;
    this.body = body;
    this.createdTimestamp = createdTimestamp;
  }

  public static Comment create(String username, String body){
    long time = new Date().getTime();
    Timestamp timestamp = new Timestamp(time);
    Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
    try (Connection cxn = Postgres.connection(); 
         PreparedStatement pStatement = cxn.prepareStatement("INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)")) {
      pStatement.setString(1, comment.id);
      pStatement.setString(2, comment.username);
      pStatement.setString(3, comment.body);
      pStatement.setTimestamp(4, comment.createdTimestamp);
      boolean result = pStatement.executeUpdate() == 1;
      return result ? comment : throw new BadRequest("Unable to save comment");
    } catch (Exception e) {
      throw new ServerError(e.getMessage()); 
    }
  }

  public static List<Comment> fetchAll() {
    List<Comment> comments = new ArrayList<>();
    try (Connection cxn = Postgres.connection();
         Statement stmt = cxn.createStatement()) {

      String query = "select * from comments;";
      try (ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
          String id = rs.getString("id");
          String username = rs.getString("username");
          String body = rs.getString("body");
          Timestamp createdTimestamp = rs.getTimestamp("created_on");
          Comment c = new Comment(id, username, body, createdTimestamp);
          comments.add(c);
        }
      }
    } catch (Exception e) {
      // Debug feature deactivated
    }
    return comments;
  }

  public static Boolean deleteComment(String commentId) {
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement("DELETE FROM comments where id = ?")) {
      pStatement.setString(1, commentId);
      return pStatement.executeUpdate() == 1; 
    } catch(Exception e) {
      // Debug feature deactivated
    } 
    return false;
  }

  private Boolean save() throws SQLException {
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement("INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)")) {
      pStatement.setString(1, this.id);
      pStatement.setString(2, this.username);
      pStatement.setString(3, this.body);
      pStatement.setTimestamp(4, this.createdTimestamp);
      return pStatement.executeUpdate() == 1;
    }
  }
}