package com.scalesec.vulnado;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import java.util.List;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class CommentsController {
  @Value("${app.secret}")
  private String secret;

  // Make sure that enabling CORS is safe here. If it's not, remove the @CrossOrigin annotation or restrict origins.
  @CrossOrigin(origins = "http://example.com") // Replace "*" with the actual origin or remove if CORS is not needed
  @GetMapping(value = "/comments", produces = "application/json")
  List<Comment> comments(@RequestHeader(value="x-auth-token") String token) {
    User.assertAuth(secret, token);
    return Comment.fetch_all();
  }

  @CrossOrigin(origins = "http://example.com") // Replace "*" with the actual origin or remove if CORS is not needed
  @PostMapping(value = "/comments", produces = "application/json", consumes = "application/json")
  Comment createComment(@RequestHeader(value="x-auth-token") String token, @RequestBody CommentRequest input) {
    return Comment.create(input.getUsername(), input.getBody());
  }

  @CrossOrigin(origins = "http://example.com") // Replace "*" with the actual origin or remove if CORS is not needed
  @DeleteMapping(value = "/comments/{id}", produces = "application/json")
  Boolean deleteComment(@RequestHeader(value="x-auth-token") String token, @PathVariable("id") String id) {
    return Comment.delete(id);
  }
}

class CommentRequest implements Serializable {
  private static final long serialVersionUID = 1L;
  private String username;
  private String body;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest extends RuntimeException {
  public BadRequest(String exception) {
    super(exception);
  }
}

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ServerError extends RuntimeException {
  public ServerError(String exception) {
    super(exception);
  }
}