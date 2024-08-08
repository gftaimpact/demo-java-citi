package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.Statement;  
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Postgres {

    private static final Logger logger = LogManager.getLogger(Postgres.class);

    private Postgres() {
        // private constructor to hide the implicit public one
    }

    public static Connection connection() {
        try {
            String url = new StringBuilder()
                    .append("jdbc:postgresql://")
                    .append(System.getenv("PGHOST"))
                    .append("/")
                    .append(System.getenv("PGDATABASE")).toString();
            return DriverManager.getConnection(url,
                    System.getenv("PGUSER"), System.getenv("PGPASSWORD"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
        return null;
    }
    
    public static void setup(){
        Connection c = null;
        Statement stmt = null;
        try {
            logger.info("Setting up Database...");
            c = connection();
            stmt = c.createStatement();

            // Create Schema
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");

            // Clean up any existing data
            stmt.executeUpdate("DELETE FROM users");
            stmt.executeUpdate("DELETE FROM comments");

            // Insert seed data
            insertUser("admin", "!!SuperSecretAdmin!!");
            insertUser("alice", "AlicePassword!");
            insertUser("bob", "BobPassword!");
            insertUser("eve", "$EVELknev^l");
            insertUser("rick", "!GetSchwifty!");

            insertComment("rick", "cool dog m8");
            insertComment("alice", "OMG so cute!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    // Use a stronger hashing algorithm
    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes());
        BigInteger hashed = new BigInteger(1, digest);  
        String hashtext = hashed.toString(16);
        while (hashtext.length() < 64) {
            hashtext = "0" + hashtext; 
        }
        return hashtext;
    }

    private static void insertUser(String username, String password) {
       String sql = "INSERT INTO users (user_id, username, password, created_on) VALUES (?, ?, ?, current_timestamp)";
       Connection c = null;
       PreparedStatement pStatement = null;
       try {
          c = connection();
          pStatement = c.prepareStatement(sql);
          pStatement.setString(1, UUID.randomUUID().toString());
          pStatement.setString(2, username);
          pStatement.setString(3, hash(password));
          pStatement.executeUpdate();
       } catch(Exception e) {
         logger.error(e.getMessage(), e);
       } finally {
           try {
               if (pStatement != null) pStatement.close();
               if (c != null) c.close();
           } catch (Exception e) {
               logger.error(e.getMessage(), e);
           }
       }
    }

    private static void insertComment(String username, String body) {
        String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?, ?, ?, current_timestamp)";
        Connection c = null;
        PreparedStatement pStatement = null;
        try {
            c = connection();
            pStatement = c.prepareStatement(sql);
            pStatement.setString(1, UUID.randomUUID().toString());
            pStatement.setString(2, username);
            pStatement.setString(3, body);
            pStatement.executeUpdate();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (pStatement != null) pStatement.close();
                if (c != null) c.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}