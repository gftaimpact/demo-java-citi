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
        assertNotNull(postgres.connection());
    }

    @Test
    public void setup_ShouldSetupDatabase() {
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);

        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeUpdate(anyString())).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Postgres postgres = new Postgres();
        postgres.setup();

        verify(mockStatement, times(6)).executeUpdate(anyString());
    }

    @Test
    public void md5_ShouldReturnMD5Hash() {
        String input = "test";
        String expectedOutput = "098f6bcd4621d373cade4e832627b4f6";
        Postgres postgres = new Postgres();
        assertEquals(expectedOutput, postgres.md5(input), "MD5 hash does not match expected output");
    }

    @Test
    public void insertUser_ShouldInsertUser() {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Postgres postgres = new Postgres();
        postgres.insertUser("testUser", "testPassword");

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void insertComment_ShouldInsertComment() {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        try {
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Postgres postgres = new Postgres();
        postgres.insertComment("testUser", "testComment");

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
