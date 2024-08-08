import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostgresTest {

    @Test
    public void connection_ShouldReturnConnection() {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Connection connection = Postgres.connection();
            assertNotNull(connection, "Connection should not be null");
        }
    }

    @Test
    public void setup_ShouldSetupDatabase() {
        try (MockedStatic<Postgres> mockedPostgres = Mockito.mockStatic(Postgres.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);

            when(mockConnection.createStatement()).thenReturn(mockStatement);
            mockedPostgres.when(Postgres::connection).thenReturn(mockConnection);

            Postgres.setup();

            verify(mockStatement, times(2)).executeUpdate(anyString());
            verify(mockStatement, times(2)).executeUpdate(anyString());
            verify(mockConnection, times(2)).close();
            verify(mockStatement, times(2)).close();
        }
    }

    @Test
    public void md5_ShouldReturnMD5Hash() {
        String input = "test";
        String expectedOutput = "098f6bcd4621d373cade4e832627b4f6";

        String output = Postgres.md5(input);
        assertEquals(expectedOutput, output, "MD5 hash does not match expected output");
    }

    @Test
    public void insertUser_ShouldInsertUser() {
        try (MockedStatic<Postgres> mockedPostgres = Mockito.mockStatic(Postgres.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            mockedPostgres.when(Postgres::connection).thenReturn(mockConnection);

            Postgres.insertUser("testUser", "testPassword");

            verify(mockPreparedStatement, times(3)).setString(anyInt(), anyString());
            verify(mockPreparedStatement).executeUpdate();
            verify(mockConnection).close();
            verify(mockPreparedStatement).close();
        }
    }

    @Test
    public void insertComment_ShouldInsertComment() {
        try (MockedStatic<Postgres> mockedPostgres = Mockito.mockStatic(Postgres.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            mockedPostgres.when(Postgres::connection).thenReturn(mockConnection);

            Postgres.insertComment("testUser", "testComment");

            verify(mockPreparedStatement, times(3)).setString(anyInt(), anyString());
            verify(mockPreparedStatement).executeUpdate();
            verify(mockConnection).close();
            verify(mockPreparedStatement).close();
        }
    }
}
