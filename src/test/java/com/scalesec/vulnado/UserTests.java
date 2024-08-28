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
        assertThrows(JwtException.class, () -> User.assertAuth(secret, invalidToken), "Exception should be thrown for invalid token");
    }

    @Test
    public void fetch_ShouldReturnUser_WhenUserExists() throws Exception {
        String username = "testUser";
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Postgres.setConnection(mockConnection);
        User user = User.fetch(username);

        assertNull(user, "User should be null when user does not exist");
    }

    @Test
    public void fetch_ShouldHandleSQLException() throws SQLException {
        String username = "testUser";
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(SQLException.class);

        Postgres.setConnection(mockConnection);
        User user = User.fetch(username);

        assertNull(user, "User should be null when SQLException is thrown");
    }
}
