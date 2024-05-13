package com.scalesec.vulnado;

import org.apache.commons.lang3.StringUtils;
import java.sql.*; 
import java.util.Date; 
import java.util.List; 
import java.util.ArrayList; 
import java.util.UUID;

public class Comment { 

  public static final String ID = "id"; 
  public static final String USERNAME = "username"; 
  public static final String BODY = "body"; 
  public static final String CREATED_ON = "created_on";

  private static final Logger logger = Logger.getLogger(Comment.class.getName());

  private String id;
  private String username;
  private String body;
  private Timestamp created_on;

  public Comment(String id, String username, String body, Timestamp created_on) { 
    this.id = id; 
    this.username = username; 
    this.body = body; 
    this.created_on = created_on;
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
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
     }
   }

  public static List<Comment> fetch_all() { 
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
        Timestamp created_on = rs.getTimestamp("created_on");
        Comment c = new Comment(id, username, body, created_on);
        comments.add(c); 
       }
     } catch (Exception e) {
      e.printStackTrace();
     } finally {
      try {
        if (stmt != null) stmt.close(); // Incluido por GFT AI Impact Bot
        if (cxn != null) cxn.close(); // Incluido por GFT AI Impact Bot
       } catch (SQLException se) { 
        se.printStackTrace();
       }
     }
    return comments; // Alterado por GFT AI Impact Bot
   }

  public static Boolean delete(String id) {
    PreparedStatement pStatement = null;
    Connection con = null;
    try {
      String sql = "DELETE FROM comments where id = ?";
      con = Postgres.connection(); // Alterado por GFT AI Impact Bot
      pStatement = con.prepareStatement(sql);
      pStatement.setString(1, id);
      int result = pStatement.executeUpdate();
      return 1 == result; // Alterado por GFT AI Impact Bot
     } catch(Exception e) {
      logger.error(e.getMessage());
     } finally {
      try {
        if (pStatement != null) pStatement.close(); // Incluido por GFT AI Impact Bot
        if (con != null) con.close(); // Incluido por GFT AI Impact Bot
       } catch (SQLException se) { 
        logger.error(se.getMessage());
       }
     }
    return false; // Alterado por GFT AI Impact Bot
   }

  private Boolean commit() throws SQLException {
    String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?, ?, ?, ?)";
    Connection con = Postgres.connection();
    PreparedStatement pStatement = con.prepareStatement(sql);
    pStatement.setString(1, this.id);
    pStatement.setString(2, this.username);
    pStatement.setString(3, this.body);
    pStatement.setTimestamp(4, this.created_on);
    Boolean result = 1 == pStatement.executeUpdate(); // Incluido por GFT AI Impact Bot
    pStatement.close(); // Incluido por GFT AI Impact Bot
    con.close(); // Incluido por GFT AI Impact Bot
    return result; // Incluido por GFT AI Impact Bot
   }
}