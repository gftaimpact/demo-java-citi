import com.scalesec.vulnado.User;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    @Test
    public void token_ShouldReturnValidToken() {
        User user = new User("1", "testUser", "hashedPassword");
        String secret = "secret";
        String token = user.token(secret);

        assertDoesNotThrow(() -> User.assertAuth(secret, token), "Token should be valid");
    }

    @Test
    public void assertAuth_ShouldThrowExceptionForInvalidToken() {
        String secret = "secret";
        String invalidToken = "invalidToken";

        Exception exception = assertThrows(Unauthorized.class, () -> User.assertAuth(secret, invalidToken));
        assertTrue(exception.getMessage().contains("JWT signature does not match locally computed signature"));
    }

    @Test
    public void fetch_ShouldReturnUserForValidUsername() throws SQLException {
        String username = "testUser";
        String userId = "1";
        String password = "hashedPassword";

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn(userId);
        when(mockResultSet.getString("username")).thenReturn(username);
        when(mockResultSet.getString("password")).thenReturn(password);

        Postgres.setMockConnection(mockConnection);

        User user = User.fetch(username);

        assertNotNull(user, "User should not be null");
        assertEquals(userId, user.id, "User id should match");
        assertEquals(username, user.username, "Username should match");
        assertEquals(password, user.hashedPassword, "Password should match");

        Postgres.setMockConnection(null); // reset mock connection
    }

    @Test
    public void fetch_ShouldReturnNullForInvalidUsername() throws SQLException {
        String username = "invalidUser";

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setMockConnection(mockConnection);

        User user = User.fetch(username);

        assertNull(user, "User should be null for invalid username");

        Postgres.setMockConnection(null); // reset mock connection
    }

    @Test
    public void fetch_ShouldReturnNullForSQLException() throws SQLException {
        String username = "testUser";

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(SQLException.class);

        Postgres.setMockConnection(mockConnection);

        User user = User.fetch(username);

        assertNull(user, "User should be null for SQLException");

        Postgres.setMockConnection(null); // reset mock connection
    }
}
