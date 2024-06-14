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
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Statement mockStatement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createComment_ShouldReturnComment() throws SQLException {
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Postgres.setConnection(mockConnection);

        Comment comment = Comment.create("testUser", "testBody");
        assertNotNull(comment);
        assertEquals("testUser", comment.username);
        assertEquals("testBody", comment.body);
    }

    @Test
    public void fetchAllComments_ShouldReturnListOfComments() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("id")).thenReturn(UUID.randomUUID().toString());
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("testBody");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));
        Postgres.setConnection(mockConnection);

        List<Comment> comments = Comment.fetch_all();
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("testUser", comments.get(0).username);
        assertEquals("testBody", comments.get(0).body);
    }

    @Test
    public void deleteComment_ShouldReturnTrue() throws SQLException {
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Postgres.setConnection(mockConnection);

        assertTrue(Comment.delete(UUID.randomUUID().toString()));
    }

    @Test
    public void deleteComment_ShouldReturnFalse() throws SQLException {
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Postgres.setConnection(mockConnection);

        assertFalse(Comment.delete(UUID.randomUUID().toString()));
    }
}
