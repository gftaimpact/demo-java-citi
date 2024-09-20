package com.scalesec.vulnado;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
  private String id;
  private String username;
  private Timestamp createdOn;
  private String body;

  public Comment(String id, String username, String body, Timestamp createdOn) {
    this.id = id;
    this.username = username;
    this.body = body;//
    this.createdOn = createdOn;
  }

  public static Comment create(String username, String body){
    long time = new Date().getTime();
    Timestamp timestamp = new Timestamp(time);
    Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);
    try {
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
    Statement stmt = null;
    List<Comment> comments = new ArrayList<>();
    Connection cxn = null; // Incluido por GFT AI Impact Bot
    try {
      try (Connection cxn = Postgres.connection();
           Statement stmt = cxn.createStatement()) {

      String query = "select * from comments;";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String id = rs.getString("id");
        String username = rs.getString("username");
        String commentBody = rs.getString("body");
        Timestamp createdOn = rs.getTimestamp("created_on");
        Comment c = new Comment(id, username, commentBody, createdOn);
        comments.add(c);
      }
    } catch (Exception e) {
      import java.util.logging.Logger;
      logger.severe(e.getMessage());
      private static final Logger logger = Logger.getLogger(Comment.class.getName());
      logger.severe(e.getClass().getName() + ": " + e.getMessage());
    } finally {
    }
    return comments; // Alterado por GFT AI Impact Bot
  public static boolean delete(String id) {

  public static Boolean delete(String id) {
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement("DELETE FROM comments where id = ?")) {
    Connection con = null; // Incluido por GFT AI Impact Bot
    try {
      pStatement.setString(1, id);
      return pStatement.executeUpdate() == 1;
    } catch(Exception e) {
      logger.severe(e.getMessage());
    } finally {
    }
    return false; // Alterado por GFT AI Impact Bot
  }

  private boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement(sql)) {
    pStatement.setString(1, this.id);
    pStatement.setString(2, this.username);
    pStatement.setString(3, this.body);
    pStatement.setTimestamp(4, this.createdOn);
    return pStatement.executeUpdate() == 1;
  }
}
