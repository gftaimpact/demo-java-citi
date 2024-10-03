import java.util.logging.Logger;
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
      cxn = Postgres.connection(); // Alterado por GFT AI Impact Bot
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
    private static final Logger logger = Logger.getLogger(Comment.class.getName());
    } catch (Exception e) {
      logger.severe(e.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close(); // Incluido por GFT AI Impact Bot
        if (cxn != null) cxn.close(); // Incluido por GFT AI Impact Bot
      } catch (SQLException se) {
        logger.severe(se.getMessage());
      }
    }
    return comments; // Alterado por GFT AI Impact Bot
  }

  public static Boolean delete(String id) {
    PreparedStatement pStatement = null; // Incluido por GFT AI Impact Bot
    Connection con = null; // Incluido por GFT AI Impact Bot
    try {
      String sql = "DELETE FROM comments where id = ?";
      con = Postgres.connection(); // Alterado por GFT AI Impact Bot
      pStatement = con.prepareStatement(sql);
      pStatement.setString(1, id);
      int result = pStatement.executeUpdate(); // Alterado por GFT AI Impact Bot
      return 1 == result; // Alterado por GFT AI Impact Bot
    } catch(Exception e) {
      logger.severe(e.getMessage());
    } finally {
      try {
        if (pStatement != null) pStatement.close(); // Incluido por GFT AI Impact Bot
        if (con != null) con.close(); // Incluido por GFT AI Impact Bot
      } catch (SQLException se) {
        logger.severe(se.getMessage());
      }
    }
    return false; // Alterado por GFT AI Impact Bot
  }

  private Boolean commit() throws SQLException {
    Connection con = null;
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    PreparedStatement pStatement = null;
      String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)";
    try {
      con = Postgres.connection();
      pStatement = con.prepareStatement(sql);
    pStatement.setString(2, this.username);
    pStatement.setString(3, this.body);
      pStatement.setTimestamp(4, this.createdOn);
      return 1 == pStatement.executeUpdate();
    } finally {
      if (pStatement != null) pStatement.close();
      if (con != null) con.close();
    }
}
