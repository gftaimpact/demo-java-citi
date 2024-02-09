package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

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
        String jws = Jwts.builder().setSubject(this.username).signWith(key).compact();
        return jws;
    }

    public static void assertAuth(String secret, String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new Unauthorized(e.getMessage());
        }
    }

    public static User fetch(String username) {
        Connection cxn = null;
        PreparedStatement stmt = null;
        User user = null;
        try {
            cxn = Postgres.connection();
            stmt = cxn.prepareStatement("select * from users where username = ? limit 1");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String user_id = rs.getString("user_id");
                String password = rs.getString("password");
                user = new User(user_id, username, password);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (cxn != null) cxn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return user;
    }
}