import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostgresTest {

    @Test
    public void connection_ShouldReturnConnection() {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any())).thenReturn(mockConnection);

            Connection connection = Postgres.connection();
            assertNotNull(connection, "Connection should not be null");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void setup_ShouldSetupDatabase() {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any())).thenReturn(mockConnection);

            Postgres.setup();

            verify(mockStatement, times(2)).executeUpdate(any());
        } catch (Exception e) {
            fail("Exception should not be thrown");
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
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any())).thenReturn(mockConnection);

            Postgres.insertUser("test", "test");

            verify(mockPreparedStatement, times(3)).setString(anyInt(), any());
            verify(mockPreparedStatement).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void insertComment_ShouldInsertComment() {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any())).thenReturn(mockConnection);

            Postgres.insertComment("test", "test");

            verify(mockPreparedStatement, times(3)).setString(anyInt(), any());
            verify(mockPreparedStatement).executeUpdate();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}
