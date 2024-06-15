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
        Postgres.setConnection(connection);
    }

    @Test
    public void create_ShouldReturnComment_WhenCommentIsSaved() throws SQLException {
        String id = UUID.randomUUID().toString();
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(preparedStatement.executeUpdate()).thenReturn(1);

        Comment comment = Comment.create(username, body);

        assertNotNull(comment);
        assertEquals(username, comment.username);
        assertEquals(body, comment.body);
        verify(connection, times(1)).prepareStatement(any(String.class));
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void fetch_all_ShouldReturnComments_WhenCommentsAreFetched() throws SQLException {
        String id = UUID.randomUUID().toString();
        String username = "testUser";
        String body = "testBody";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(resultSet.getString("id")).thenReturn(id);
        when(resultSet.getString("username")).thenReturn(username);
        when(resultSet.getString("body")).thenReturn(body);
        when(resultSet.getTimestamp("created_on")).thenReturn(timestamp);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals(id, comments.get(0).id);
        assertEquals(username, comments.get(0).username);
        assertEquals(body, comments.get(0).body);
        assertEquals(timestamp, comments.get(0).created_on);
        verify(connection, times(1)).createStatement();
        verify(statement, times(1)).executeQuery(any(String.class));
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentIsDeleted() throws SQLException {
        String id = UUID.randomUUID().toString();

        when(preparedStatement.executeUpdate()).thenReturn(1);

        Boolean result = Comment.delete(id);

        assertTrue(result);
        verify(connection, times(1)).prepareStatement(any(String.class));
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentIsNotDeleted() throws SQLException {
        String id = UUID.randomUUID().toString();

        when(preparedStatement.executeUpdate()).thenReturn(0);

        Boolean result = Comment.delete(id);

        assertFalse(result);
        verify(connection, times(1)).prepareStatement(any(String.class));
        verify(preparedStatement, times(1)).executeUpdate();
    }
}
