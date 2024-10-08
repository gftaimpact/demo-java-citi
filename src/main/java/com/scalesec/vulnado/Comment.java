 package com.scalesec.vulnado;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
  private static final String COMMENT_ID = "";
  private static final String COMMENT_USERNAME = "";
  private static final String COMMENT_BODY = "";
  private static final Timestamp COMMENT_CREATED_ON;
  
  public Comment(String commentId, String commentUsername, String commentBody, Timestamp commentCreatedOn) {
    COMMENT_ID = commentId;
    COMMENT_USERNAME = commentUsername;
    COMMENT_BODY = commentBody;
    COMMENT_CREATED_ON = commentCreatedOn;
  }

  public static Comment create(String username, String body){
    long time = new Date().getTime();
    Timestamp timestamp = new Timestamp(time);
    Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
    try(Connection con = Postgres.connection(); 
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
    try(Connection con = Postgres.connection();
        Statement stmt = con.createStatement()){

      String query = "select * from comments;";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String id = rs.getString("id");
        String username = rs.getString("username");
        String body = rs.getString("body");
        Timestamp createdOn = rs.getTimestamp("created_on");
        Comment c = new Comment(id, username, body, createdOn);
        comments.add(c);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Logger.error(e.getMessage());
    }
    return comments;
  }

  public static boolean delete(String commentId) {
    try(Connection con = Postgres.connection();
        PreparedStatement pStatement = con.prepareStatement(sql)) {
      String sql = "DELETE FROM comments where id = ?";
      pStatement.setString(1, commentId);
      return pStatement.executeUpdate() == 1; 
    } catch(Exception e) {
      Logger.error(e.getMessage());
    } 
    return false;
  }

  private boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    try(Connection con = Postgres.connection();
        PreparedStatement pStatement = con.prepareStatement(sql)) {
      pStatement.setString(1, COMMENT_ID);
      pStatement.setString(2, COMMENT_USERNAME); 
      pStatement.setString(3, COMMENT_BODY);
      pStatement.setTimestamp(4, COMMENT_CREATED_ON);
      return pStatement.executeUpdate() == 1;
    }
  }
}