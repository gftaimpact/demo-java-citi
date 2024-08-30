package com.scalesec.vulnado;

import java.util.logging.Level;
import java.sql.Connection;
import java.util.logging.Logger;
import java.sql.DriverManager;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

private Postgres() {}
public class Postgres {

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
            e.printStackTrace();
            Logger.getLogger(Postgres.class.getName()).log(Level.SEVERE, null, e);
            System.exit(1);
        }
        return null;
    }
    public static void setup(){
        Connection c = null;
        Statement stmt = null;
        try {
            Logger.getLogger(Postgres.class.getName()).log(Level.INFO, "Setting up Database...");
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
            Logger.getLogger(Postgres.class.getName()).log(Level.SEVERE, null, e);
            System.exit(1);
        } finally {
            try {
                if (stmt != null) stmt.close(); // Alterado por GFT AI Impact Bot
                if (c != null) c.close(); // Alterado por GFT AI Impact Bot
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Java program to calculate MD5 hash value
    public static String md5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = hashtext.concat("0");
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(e);
        }
    }

    private static void insertUser(String username, String password) {
       String sql = "INSERT INTO users (user_id, username, password, created_on) VALUES (?, ?, ?, current_timestamp)";
       Connection c = null; // Incluido por GFT AI Impact Bot
       PreparedStatement pStatement = null;
       try {
          c = connection(); // Alterado por GFT AI Impact Bot
          pStatement = c.prepareStatement(sql);
          pStatement.setString(1, UUID.randomUUID().toString());
          pStatement.setString(2, username);
          pStatement.setString(3, md5(password));
          pStatement.executeUpdate();
       } catch(Exception e) {
         Logger.getLogger(Postgres.class.getName()).log(Level.SEVERE, null, e);
       } finally {
           try {
               if (pStatement != null) pStatement.close(); // Incluido por GFT AI Impact Bot
               if (c != null) c.close(); // Incluido por GFT AI Impact Bot
           } catch (Exception e) {
               Logger.getLogger(Postgres.class.getName()).log(Level.SEVERE, null, e);
           }
       }
    }

    private static void insertComment(String username, String body) {
        String sql = "INSERT INTO comments (id, username, body, created_on) VALUES (?, ?, ?, current_timestamp)";
        Connection c = null; // Incluido por GFT AI Impact Bot
        PreparedStatement pStatement = null;
        try {
            c = connection(); // Alterado por GFT AI Impact Bot
            pStatement = c.prepareStatement(sql);
            pStatement.setString(1, UUID.randomUUID().toString());
            pStatement.setString(2, username);
            pStatement.setString(3, body);
            pStatement.executeUpdate();
        } catch(Exception e) {
            Logger.getLogger(Postgres.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (pStatement != null) pStatement.close(); // Incluido por GFT AI Impact Bot
                if (c != null) c.close(); // Incluido por GFT AI Impact Bot
            } catch (Exception e) {
                Logger.getLogger(Postgres.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}