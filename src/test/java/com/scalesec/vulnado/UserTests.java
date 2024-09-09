import com.scalesec.vulnado.User;
import com.scalesec.vulnado.Unauthorized;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {
    private User user;
    private String secret = "secret";
    private String token;

    @BeforeEach
    public void setUp() {
        user = new User("1", "testUser", "hashedPassword");
        token = user.token(secret);
    }

    @Test
    public void token_ShouldReturnValidToken() {
        assertNotNull(token, "Token should not be null");
    }

    @Test
    public void assertAuth_ShouldNotThrowException_WhenTokenIsValid() {
        assertDoesNotThrow(() -> User.assertAuth(secret, token), "Exception should not be thrown for valid token");
    }

    @Test
    public void assertAuth_ShouldThrowUnauthorizedException_WhenTokenIsInvalid() {
        String invalidToken = "invalidToken";
        Exception exception = assertThrows(Unauthorized.class, () -> User.assertAuth(secret, invalidToken));
        assertTrue(exception.getMessage().contains("JWT signature does not match locally computed signature"), "Exception message should contain JWT signature error");
    }

    @Test
    public void fetch_ShouldReturnUser_WhenUsernameExists() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("password")).thenReturn("hashedPassword");

        User fetchedUser = User.fetch("testUser");

        assertNotNull(fetchedUser, "Fetched user should not be null");
        assertEquals("1", fetchedUser.id, "Fetched user id should match");
        assertEquals("testUser", fetchedUser.username, "Fetched username should match");
        assertEquals("hashedPassword", fetchedUser.hashedPassword, "Fetched password should match");
    }

    @Test
    public void fetch_ShouldReturnNull_WhenUsernameDoesNotExist() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        User fetchedUser = User.fetch("nonExistentUser");

        assertNull(fetchedUser, "Fetched user should be null for non existent user");
    }

    @Test
    public void fetch_ShouldReturnNull_WhenSQLExceptionOccurs() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        User fetchedUser = User.fetch("testUser");

        assertNull(fetchedUser, "Fetched user should be null when SQLException occurs");
    }

    @AfterEach
    public void tearDown() {
        user = null;
        token = null;
    }
}
