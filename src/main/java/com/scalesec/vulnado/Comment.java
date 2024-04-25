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
  private static final Timestamp createdOn;

  public Comment(String id, String username, String body, Timestamp createdOn) {
    this.id = id;
    this.username = username;
    this.body = body;
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
    Connection cxn = null;
    try {
      cxn = Postgres.connection();
      stmt = cxn.createStatement();

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
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (cxn != null) cxn.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return comments;
  }

  public static boolean delete(String id) {
    PreparedStatement pStatement = null;
    Connection con = null;
    try {
      String sql = "DELETE FROM comments where id = ?";
      con = Postgres.connection();
      pStatement = con.prepareStatement(sql);
      pStatement.setString(1, id);
      int result = pStatement.executeUpdate();
      return 1 == result;
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (pStatement != null) pStatement.close();
        if (con != null) con.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return false;
  }

  private boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    Connection con = Postgres.connection();
    PreparedStatement pStatement = con.prepareStatement(sql);
    pStatement.setString(1, this.id);
    pStatement.setString(2, this.username);
    pStatement.setString(3, this.body);
    pStatement.setTimestamp(4, this.createdOn);
    boolean result = 1 == pStatement.executeUpdate();
    pStatement.close();
    con.close();
    return result;
  }
}