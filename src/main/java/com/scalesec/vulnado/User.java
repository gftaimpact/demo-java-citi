\npackage com.scalesec.vulnado;\n\nimport java.sql.Connection;\nimport java.sql.PreparedStatement;\nimport java.sql.ResultSet;\nimport java.util.logging.Logger;\n\npublic class User {\n\n    private static final Logger LOGGER = Logger.getLogger(User.class.getName());\n\n    private final String id;\n    private final String username;\n    private final String hashedPassword;\n\n    public User(String id, String username, String hashedPassword) {\n        this.id = id;\n        this.username = username;\n        this.hashedPassword = hashedPassword;\n    }\n\n    public String token(String secret) {\n        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());\n        String jws = Jwts.builder().setSubject(this.username).signWith(key).compact();\n        return jws;\n    }\n\n    public static void assertAuth(String secret, String token) {\n        try {\n            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());\n            Jwts.parser()\n                    .setSigningKey(key)\n                    .parseClaimsJws(token);\n        } catch (Exception e) {\n            LOGGER.severe(e.getMessage());\n            throw new Unauthorized(e.getMessage());\n        }\n    }\n\n    public static User fetch(String username) {\n        PreparedStatement stmt = null;\n        User user = null;\n        Connection cxn = null; // Incluido por GFT AI Impact Bot\n        try {\n            cxn = Postgres.connection(); // Alterado por GFT AI Impact Bot\n            stmt = cxn.prepareStatement(\"select * from users where username = ? limit 1\");\n            stmt.setString(1, username);\n            ResultSet rs = stmt.executeQuery();\n            if (rs.next()) {\n                String user_id = rs.getString(\"user_id\");\n                String password = rs.getString(\"password\");\n                user = new User(user_id, username, password);\n            }\n        } catch (Exception e) {\n            LOGGER.severe(e.getClass().getName() + \": \" + e.getMessage());\n            return null; // Alterado por GFT AI Impact Bot\n        } finally {\n            try {\n                if (stmt != null) stmt.close(); // Incluido por GFT AI Impact Bot\n                if (cxn != null) cxn.close(); // Incluido por GFT AI Impact Bot\n            } catch (Exception e) {\n                LOGGER.severe(e.getMessage());\n            }\n        }\n        return user; // Alterado por GFT AI Impact Bot\n    }\n}\n