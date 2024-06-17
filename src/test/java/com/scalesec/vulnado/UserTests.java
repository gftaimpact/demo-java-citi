import com.scalesec.vulnado.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void token_ShouldReturnValidToken() {
        User user = new User("1", "testUser", "hashedPassword");
        String secret = "secret";
        String token = user.token(secret);
        assertNotNull(token, "Token should not be null");

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secret.getBytes()).build();
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        assertEquals(user.username, claimsJws.getBody().getSubject(), "Token subject should match username");
    }

    @Test
    public void assertAuth_ShouldNotThrowException_WhenTokenIsValid() {
        User user = new User("1", "testUser", "hashedPassword");
        String secret = "secret";
        String token = user.token(secret);
        assertDoesNotThrow(() -> User.assertAuth(secret, token), "Valid token should not throw exception");
    }

    @Test
    public void assertAuth_ShouldThrowException_WhenTokenIsInvalid() {
        String secret = "secret";
        String invalidToken = "invalidToken";
        assertThrows(Unauthorized.class, () -> User.assertAuth(secret, invalidToken), "Invalid token should throw exception");
    }

    @Test
    public void fetch_ShouldReturnUser_WhenUserExists() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("user_id")).thenReturn("1");
        when(resultSet.getString("username")).thenReturn("testUser");
        when(resultSet.getString("password")).thenReturn("hashedPassword");

        User user = User.fetch("testUser");
        assertNotNull(user, "User should not be null");
        assertEquals("1", user.id, "User id should match");
        assertEquals("testUser", user.username, "Username should match");
        assertEquals("hashedPassword", user.hashedPassword, "Password should match");
    }

    @Test
    public void fetch_ShouldReturnNull_WhenUserDoesNotExist() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = User.fetch("testUser");
        assertNull(user, "User should be null when not found");
    }
}
