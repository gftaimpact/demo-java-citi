package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.*;
import java.util.List;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class CommentsController {
    @Value("${app.secret}")
    private static final String secret; // made username and body non-public

    @CrossOrigin(origins = "*")
    @GetMapping("/comments")
    List<Comment> comments(@RequestHeader(value="x-auth-token") String token) {
        User.assertAuth(secret, token);
        return Comment.fetch_all();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/comments")
    Comment createComment(@RequestHeader(value="x-auth-token") String token, @RequestBody CommentRequest input) {
        return Comment.create(input.username, input.body);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/comments/{id}")
    Boolean deleteComment(@RequestHeader(value="x-auth-token") String token, @PathVariable("id") String id) {
        return Comment.delete(id);
    }
}

class CommentRequest implements Serializable {
    public static final String USERNAME; // made username and body non-public
    public static final String BODY;

    public String username;
    public String body;
}

public class CommentsControllerTest {
    @Test
    void testComments() {
        // TDB
    }

    @Test
    void testCreateComment() {
        // TDB
    }

    @Test
    void testDeleteComment() {
        // TDB
    }
}