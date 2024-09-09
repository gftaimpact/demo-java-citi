import com.scalesec.vulnado.User;
import com.scalesec.vulnado.Unauthorized;
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
    public void assertAuth_ShouldThrowUnauthorized_WhenTokenIsInvalid() {
        String secret = "secret";
        String invalidToken = "invalidToken";

        assertThrows(Unauthorized.class, () -> User.assertAuth(secret, invalidToken), "Should throw Unauthorized when token is invalid");
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

        User expectedUser = new User(userId, username, password);
        User actualUser = User.fetch(username);

        assertEquals(expectedUser.id, actualUser.id, "User id should match");
        assertEquals(expectedUser.username, actualUser.username, "Username should match");
        assertEquals(expectedUser.hashedPassword, actualUser.hashedPassword, "Password should match");
    }

    @Test
    public void fetch_ShouldReturnNull_WhenUsernameDoesNotExist() throws SQLException {
        String username = "nonExistingUser";

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        User actualUser = User.fetch(username);

        assertNull(actualUser, "Should return null when username does not exist");
    }
}
