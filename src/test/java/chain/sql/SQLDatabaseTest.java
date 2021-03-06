package chain.sql;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;


/**
 * This class is responsible for carrying out unit tests on the
 * SQLDatabase class. It creates a mock database using the SQLMockDatabase
 * class and connects to it before running the tests.
 */
public class SQLDatabaseTest {

    private SQLDatabase database;

    @Before
    public void setup() throws SQLException {
        SQLMockDatabase mockDb = new SQLMockDatabase();

        ResultSet rsTables = mock(ResultSet.class);
        ResultSet rsColumns1 = mock(ResultSet.class);
        ResultSet rsColumns2 = mock(ResultSet.class);

        when(rsTables.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsTables.getString(1)).thenReturn("users").thenReturn("roles");

        when(rsColumns1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsColumns1.getString(1)).thenReturn("username").thenReturn("password");

        when(rsColumns2.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsColumns2.getString(1)).thenReturn("access").thenReturn("level");

        mockDb.returnWhenQuerying("SHOW TABLES", rsTables);
        mockDb.returnWhenQuerying("DESCRIBE users", rsColumns1);
        mockDb.returnWhenQuerying("DESCRIBE roles", rsColumns2);

        database = new SQLDatabase(mockDb.getConnection());
    }


    /**
     * Test 10.1
	 * Tests that the getTable function returns a SQLTable when given the
	 * table name “users” as input for the mock database.
     * @throws TableNotFoundException
     */
    @Test
    public void getTable() throws TableNotFoundException {
        assertThat( database.getTable("users"), instanceOf(SQLTable.class));
    }

    @Test(expected = TableNotFoundException.class)
    public void getTableThrowException() throws TableNotFoundException {
       database.getTable("false_table");
    }


    /**
     * Test 10.2
	 * This test checks that the containsTable function of SQLDatabase works
	 * correctly for tables that do exist in the mock database and tables which
	 * do not exist.
     * @throws TableNotFoundException
     */
    @Test
    public void containsTable() {
        assertTrue("Could not find table called users", database.containsTable( "users"));
        assertTrue("Could not find table called roles", database.containsTable( "roles"));
        assertFalse("Found table passwords which should not exist",database.containsTable("passwords"));
    }


    /**
     * Test 10.3
	 * Tests the tableContainsColumn function of SQLDatabase. It checks that the
	 * table “users” in the mock database contains a column named “username”
	 * but does not contain a column named “firstname”.
     * @throws TableNotFoundException
     */
    @Test
    public void tableContainsColumn() throws TableNotFoundException {
        assertTrue("Table \"users\" should contain column \"username\"", database.tableContainsColumn("users", "username"));
        assertFalse("Table \"users\" should contain column \"username\"", database.tableContainsColumn("users", "firstname"));

    }

    @Test(expected = TableNotFoundException.class)
    public void tableContainsColumnThrowException() throws TableNotFoundException
    {
        database.tableContainsColumn("false_table","username");
    }

    @Test
    public void columnInAnyTable() {
        assertTrue(database.columnInAnyTable("username"));
        assertTrue(database.columnInAnyTable("access"));
        assertFalse(database.columnInAnyTable("kettle"));
    }
}