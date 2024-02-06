\npackage com.scalesec.vulnado;\n\nimport java.sql.*;\nimport java.util.Date;\nimport java.util.List;\nimport java.util.ArrayList;\nimport java.util.UUID;\n\npublic class Comment {\n\n    private String id;\n    private String username;\n    private String body;\n    private Timestamp createdOn;\n\n    public Comment(String id, String username, String body, Timestamp createdOn) {\n        this.id = id;\n        this.username = username;\n        this.body = body;\n        this.createdOn = createdOn;\n    }\n\n    public static Comment create(String username, String body) {\n        long time = new Date().getTime();\n        Timestamp timestamp = new Timestamp(time);\n        Comment comment = new Comment(UUID.randomUUID().toString(), username, body, timestamp);\n        try {\n            if (comment.commit()) {\n                return comment;\n            } else {\n                throw new BadRequest(\"Unable to save comment\");\n            }\n        } catch (Exception e) {\n            throw new ServerError(e.getMessage());\n        }\n    }\n\n    public static List\u003cComment\u003e fetchAll() {\n        Statement stmt = null;\n        List\u003cComment\u003e comments = new ArrayList\u003c\u003e();\n        Connection cxn = null; // Incluido por GFT AI Impact Bot\n        try {\n            cxn = Postgres.connection(); // Alterado por GFT AI Impact Bot\n            stmt = cxn.createStatement();\n            String query = \"select * from comments;\";\n            ResultSet rs = stmt.executeQuery(query);\n            while (rs.next()) {\n                String id = rs.getString(\"id\");\n                String username = rs.getString(\"username\");\n                String body = rs.getString(\"body\");\n                Timestamp createdOn = rs.getTimestamp(\"created_on\");\n                Comment c = new Comment(id, username, body, createdOn);\n                comments.add(c);\n            }\n        } catch (Exception e) {\n            e.printStackTrace();\n            System.err.println(e.getClass().getName() + \": \" + e.getMessage());\n        } finally {\n            try {\n                if (stmt != null) stmt.close(); // Incluido por GFT AI Impact Bot\n                if (cxn != null) cxn.close(); // Incluido por GFT AI Impact Bot\n            } catch (SQLException se) {\n                se.printStackTrace();\n            }\n        }\n        return comments; // Alterado por GFT AI Impact Bot\n    }\n\n    public static Boolean delete(String id) {\n        PreparedStatement pStatement = null; // Incluido por GFT AI Impact Bot\n        Connection con = null; // Incluido por GFT AI Impact Bot\n        try {\n            String sql = \"DELETE FROM comments where id = ?\";\n            con = Postgres.connection(); // Alterado por GFT AI Impact Bot\n            pStatement = con.prepareStatement(sql);\n            pStatement.setString(1, id);\n            int result = pStatement.executeUpdate(); // Alterado por GFT AI Impact Bot\n            return 1 == result; // Alterado por GFT AI Impact Bot\n        } catch (Exception e) {\n            e.printStackTrace();\n        } finally {\n            try {\n                if (pStatement != null) pStatement.close(); // Incluido por GFT AI Impact Bot\n                if (con != null) con.close(); // Incluido por GFT AI Impact Bot\n            } catch (SQLException se) {\n                se.printStackTrace();\n            }\n        }\n        return false; // Alterado por GFT AI Impact Bot\n    }\n\n    private Boolean commit() throws SQLException {\n        String sql = \"INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)\";\n        Connection con = Postgres.connection();\n        PreparedStatement pStatement = con.prepareStatement(sql);\n        pStatement.setString(1, this.id);\n        pStatement.setString(2, this.username);\n        pStatement.setString(3, this.body);\n        pStatement.setTimestamp(4, this.createdOn);\n        Boolean result = 1 == pStatement.executeUpdate(); // Incluido por GFT AI Impact Bot\n        pStatement.close(); // Incluido por GFT AI Impact Bot\n        con.close(); // Incluido por GFT AI Impact Bot\n        return result; // Incluido por GFT AI Impact Bot\n    }\n}\n