import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    @Test
    public void create_ShouldReturnComment_WhenUsernameAndBodyAreProvided() {
        String username = "testUser";
        String body = "testBody";
        Comment comment = Comment.create(username, body);
        assertNotNull(comment);
        assertEquals(username, comment.username);
        assertEquals(body, comment.body);
    }

    @Test
    public void fetch_all_ShouldReturnListOfComments_WhenCommentsExist() {
        List<Comment> comments = Comment.fetch_all();
        assertNotNull(comments);
        assertFalse(comments.isEmpty());
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentExists() {
        String id = UUID.randomUUID().toString();
        Boolean result = Comment.delete(id);
        assertTrue(result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Boolean result = Comment.delete(id);
        assertFalse(result);
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
        assertTrue(result);
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
        assertFalse(result);
    }
}
