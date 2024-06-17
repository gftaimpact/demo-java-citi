import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    @Test
    public void token_ShouldReturnValidToken() {
        User user = new User("1", "testUser", "hashedPassword");
        String secret = "secret";
        String token = user.token(secret);
        assertNotNull(token, "Token should not be null");
    }

    @Test
    public void assertAuth_ShouldNotThrowException_WhenTokenIsValid() {
        User user = new User("1", "testUser", "hashedPassword");
        String secret = "secret";
        String token = user.token(secret);
        assertDoesNotThrow(() -> User.assertAuth(secret, token), "Exception should not be thrown for valid token");
    }

    @Test
    public void assertAuth_ShouldThrowException_WhenTokenIsInvalid() {
        String secret = "secret";
        String invalidToken = "invalidToken";
        assertThrows(Unauthorized.class, () -> User.assertAuth(secret, invalidToken), "Exception should be thrown for invalid token");
    }

    @Test
    public void fetch_ShouldReturnUser_WhenUserExists() throws Exception {
        String username = "testUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn(username);
        when(mockResultSet.getString("password")).thenReturn("hashedPassword");

        Postgres.setConnection(mockConnection);
        User user = User.fetch(username);

        assertNotNull(user, "User should not be null");
        assertEquals(username, user.username, "Username should match");
    }

    @Test
    public void fetch_ShouldReturnNull_WhenUserDoesNotExist() throws Exception {
        String username = "testUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setConnection(mockConnection);
        User user = User.fetch(username);

        assertNull(user, "User should be null when user does not exist");
    }

    @Test
    public void fetch_ShouldCloseResources_WhenFinished() throws Exception {
        String username = "testUser";
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setConnection(mockConnection);
        User.fetch(username);

        verify(mockStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }
}
