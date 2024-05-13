package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String HASHED_PASSWORD = "hashedPassword";

    private final String id;
    private final String username;
    private final String hashedPassword;

    public User(String id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String token(String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder().setSubject(username).signWith(key).compact();
    }

    public static void assertAuth(String secret, String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Unauthorized(e.getMessage());
        }
    }

    public static User fetch(String un) {
        Connection cxn = null;
        PreparedStatement stmt = null;
        User user = null;

        try {
            cxn = Postgres.connection();
            stmt = cxn.prepareStatement("SELECT * FROM users WHERE username = ? LIMIT 1");
            stmt.setString(1, un);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString(ID);
                String userUsername = rs.getString(USERNAME);
                String userPassword = rs.getString(HASHED_PASSWORD);
                user = new User(userId, userUsername, userPassword);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            System.getLogger().error(e.getClass().getName() + ": " + e.getMessage());
            return null;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (cxn != null) cxn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return user;
    }
}

class Unauthorized extends Exception { //TDB
}