import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostgresTest {

    @Test
    public void connection_ShouldReturnConnection() {
        Connection mockConnection = Mockito.mock(Connection.class);
        Postgres postgres = new Postgres();
        assertEquals(mockConnection, postgres.connection());
    }

    @Test
    public void setup_ShouldSetupDatabase() {
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);
        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            Postgres postgres = new Postgres();
            postgres.setup();
            verify(mockStatement, times(2)).executeUpdate(anyString());
        } catch (SQLException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void hashPassword_ShouldReturnHashedPassword() {
        String password = "password";
        Postgres postgres = new Postgres();
        String hashedPassword = postgres.hashPassword(password);
        assertNotNull(hashedPassword);
        assertEquals(64, hashedPassword.length());
    }

    @Test
    public void insertUser_ShouldInsertUser() {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            Postgres postgres = new Postgres();
            postgres.insertUser("username", "password");
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (SQLException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void insertComment_ShouldInsertComment() {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            Postgres postgres = new Postgres();
            postgres.insertComment("username", "comment");
            verify(mockPreparedStatement, times(1)).executeUpdate();
        } catch (SQLException e) {
            fail("Exception should not be thrown");
        }
    }
}
