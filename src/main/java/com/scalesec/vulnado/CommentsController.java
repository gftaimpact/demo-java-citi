package com.scalesec.vulnado;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
public class CommentsController {

  private static final String SECRET = "secret";

  @GetMapping("/comments")
  List<Comment> comments(@RequestHeader("x-auth-token") String token) {
    User.assertAuth(SECRET, token);
    return Comment.fetch_all();
  }

  @PostMapping("/comments") 
  Comment createComment(@RequestHeader("x-auth-token") String token, @RequestBody CommentRequest input) {
    return Comment.create(input.getUsername(), input.getBody());
  }

  @DeleteMapping("/comments/{id}")
  Boolean deleteComment(@RequestHeader("x-auth-token") String token, @PathVariable("id") String id) {
    return Comment.delete(id);
  }
}

class CommentRequest {

  private static final String USERNAME = "username";
  private static final String BODY = "body";
  
  public String getUsername() {
    return USERNAME; 
  }
  
  public String getBody() {
    return BODY;
  }
  
}

@ResponseStatus(HttpStatus.BAD_REQUEST)  
class BadRequest extends RuntimeException {

  // existing code
  
}

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ServerError extends RuntimeException {

  // existing code

}