import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    @Test
    public void create_ShouldReturnComment_WhenParametersAreValid() {
        String username = "testUser";
        String body = "testBody";
        Comment comment = Comment.create(username, body);
        assertNotNull(comment, "Comment should not be null");
        assertEquals(username, comment.username, "Username should match");
        assertEquals(body, comment.body, "Body should match");
    }

    @Test
    public void fetch_all_ShouldReturnComments_WhenCommentsExist() {
        List<Comment> comments = Comment.fetch_all();
        assertNotNull(comments, "Comments list should not be null");
        assertFalse(comments.isEmpty(), "Comments list should not be empty");
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentExists() {
        String id = UUID.randomUUID().toString();
        Boolean result = Comment.delete(id);
        assertTrue(result, "Delete should return true when comment exists");
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Boolean result = Comment.delete(id);
        assertFalse(result, "Delete should return false when comment does not exist");
    }

    @Test
    public void commit_ShouldReturnTrue_WhenCommentIsSaved() throws SQLException {
        String id = UUID.randomUUID().toString();
        String username = "testUser";
        String body = "testBody";
        long time = new Date().getTime();
        Timestamp timestamp = new Timestamp(time);
        Comment comment = new Comment(id, username, body, timestamp);
        Boolean result = comment.commit();
        assertTrue(result, "Commit should return true when comment is saved");
    }

    @Test
    public void commit_ShouldReturnFalse_WhenCommentIsNotSaved() throws SQLException {
        String id = UUID.randomUUID().toString();
        String username = "testUser";
        String body = "testBody";
        long time = new Date().getTime();
        Timestamp timestamp = new Timestamp(time);
        Comment comment = new Comment(id, username, body, timestamp);
        Boolean result = comment.commit();
        assertFalse(result, "Commit should return false when comment is not saved");
    }
}
