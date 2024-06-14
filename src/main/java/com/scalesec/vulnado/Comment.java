package com.scalesec.vulnado;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
  private static final String ID;
  private static final String USERNAME;
  private static final String BODY;
  private static final Timestamp CREATED_ON;

  public Comment(String id, String username, String body, Timestamp createdOn) {
    this.ID = id;
    this.USERNAME = username;
    this.BODY = body;
    this.CREATED_ON = createdOn;
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
    try (Connection cxn = Postgres.connection()) {
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
      System.err.println(e.getClass().getName()+": "+e.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return comments;
  }

  public static boolean delete(String id) {
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement("DELETE FROM comments where id = ?")) {
      pStatement.setString(1, id);
      int result = pStatement.executeUpdate();
      return 1 == result;
    } catch(Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    try (Connection con = Postgres.connection();
         PreparedStatement pStatement = con.prepareStatement(sql)) {
      pStatement.setString(1, this.ID);
      pStatement.setString(2, this.USERNAME);
      pStatement.setString(3, this.BODY);
      pStatement.setTimestamp(4, this.CREATED_ON);
      return 1 == pStatement.executeUpdate();
    }
  }
}