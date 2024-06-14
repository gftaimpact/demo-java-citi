import com.scalesec.vulnado.Comment;
import com.scalesec.vulnado.Postgres;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(resultSet.getString("username")).thenReturn("testUser");
        when(resultSet.getString("body")).thenReturn("testBody");
        when(resultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));
        Postgres.setConnection(connection);
    }

    @Test
    public void createComment_ShouldReturnComment() {
        Comment comment = Comment.create("testUser", "testBody");
        assertNotNull(comment);
        assertEquals("testUser", comment.username);
        assertEquals("testBody", comment.body);
    }

    @Test
    public void fetchAllComments_ShouldReturnListOfComments() throws SQLException {
        List<Comment> comments = Comment.fetch_all();
        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals("testUser", comments.get(0).username);
        assertEquals("testBody", comments.get(0).body);
    }

    @Test
    public void deleteComment_ShouldReturnTrue() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        assertTrue(Comment.delete(UUID.randomUUID().toString()));
    }

    @Test
    public void deleteComment_ShouldReturnFalse() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(Comment.delete(UUID.randomUUID().toString()));
    }
}
