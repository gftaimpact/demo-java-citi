Here are the unit tests for the provided Java code:

```java
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    @Test
    public void create_ShouldReturnComment_WhenGivenValidInput() {
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
    public void delete_ShouldReturnTrue_WhenCommentIsDeleted() {
        String id = "testId";
        Boolean result = Comment.delete(id);
        assertTrue(result);
    }

    @Test
    public void commit_ShouldReturnTrue_WhenCommentIsSaved() throws SQLException {
        String id = "testId";
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Comment comment = new Comment(id, username, body, timestamp);
        Boolean result = comment.commit();
        assertTrue(result);
    }
}
```

Please note that these tests assume that the database is properly set up and contains the necessary data. If this is not the case, you may need to mock the `Postgres.connection()` method and any other database interactions.
