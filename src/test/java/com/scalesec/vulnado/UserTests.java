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
    public void assertAuth_ShouldThrowException_WhenTokenIsInvalid() {
        String secret = "secret";
        String invalidToken = "invalidToken";

        assertThrows(Unauthorized.class, () -> User.assertAuth(secret, invalidToken), "Exception should be thrown for invalid token");
    }

    @Test
    public void fetch_ShouldReturnUser_WhenUsernameExists() throws SQLException {
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
    }

    @Test
    public void fetch_ShouldReturnNull_WhenUsernameDoesNotExist() throws SQLException {
        String username = "testUser";

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setMockConnection(mockConnection);

        User user = User.fetch(username);

        assertNull(user, "User should be null when username does not exist");
    }

    @Test
    public void fetch_ShouldReturnNull_WhenSQLExceptionOccurs() throws SQLException {
        String username = "testUser";

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Postgres.setMockConnection(mockConnection);

        User user = User.fetch(username);

        assertNull(user, "User should be null when SQLException occurs");
    }
}
