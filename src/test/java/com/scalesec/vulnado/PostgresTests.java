import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PostgresTest {

    @Test
    public void hashPassword_ShouldReturnHashedPassword() {
        String password = "password";
        String hashedPassword = Postgres.hashPassword(password);
        assertEquals(64, hashedPassword.length());
    }

    @Test
    public void insertUser_ShouldExecuteUpdate() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doNothing().when(mockStatement).setString(anyInt(), anyString());
        when(mockStatement.executeUpdate()).thenReturn(1);

        Postgres.insertUser("username", "password");

        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void insertUser_ShouldThrowException_WhenSQLExceptionOccurs() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doNothing().when(mockStatement).setString(anyInt(), anyString());
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> Postgres.insertUser("username", "password"));
    }

    @Test
    public void insertComment_ShouldExecuteUpdate() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doNothing().when(mockStatement).setString(anyInt(), anyString());
        when(mockStatement.executeUpdate()).thenReturn(1);

        Postgres.insertComment("username", "comment");

        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void insertComment_ShouldThrowException_WhenSQLExceptionOccurs() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doNothing().when(mockStatement).setString(anyInt(), anyString());
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> Postgres.insertComment("username", "comment"));
    }

    @Test
    public void setup_ShouldExecuteUpdate() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        Postgres.setup();

        verify(mockStatement, times(6)).executeUpdate(anyString());
    }

    @Test
    public void setup_ShouldThrowException_WhenSQLExceptionOccurs() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeUpdate(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> Postgres.setup());
    }
}
