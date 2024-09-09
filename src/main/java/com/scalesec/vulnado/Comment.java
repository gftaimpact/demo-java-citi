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
    Statement statement = null;
    List<Comment> comments = new ArrayList<>();
    try (Connection connection = Postgres.connection()) {
      statement = connection.createStatement();

      String query = "select * from comments;";
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        String id = resultSet.getString("id");
        String username = resultSet.getString("username");
        String body = resultSet.getString("body");
        Timestamp createdOn = resultSet.getTimestamp("created_on");
        Comment comment = new Comment(id, username, body, createdOn);
        comments.add(comment);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
    } finally {
      try {
        if (statement != null) statement.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return comments;
  }

  public static boolean delete(String id) {
    try (Connection connection = Postgres.connection();
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM comments where id = ?")) {
      preparedStatement.setString(1, id);
      int result = preparedStatement.executeUpdate();
      return 1 == result;
    } catch(Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    try (Connection connection = Postgres.connection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, this.ID);
      preparedStatement.setString(2, this.USERNAME);
      preparedStatement.setString(3, this.BODY);
      preparedStatement.setTimestamp(4, this.CREATED_ON);
      return 1 == preparedStatement.executeUpdate();
    }
  }
}