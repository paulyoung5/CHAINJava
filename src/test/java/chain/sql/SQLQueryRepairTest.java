package chain.sql;

import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * SQLQueryRepairTest
 * 
 * Responsible for testing the SQLQueryRepair class. It will create
 * some mock database tables and connect to a database which will
 * return those tables before running the tests.
 *
 */
public class SQLQueryRepairTest {

    private SQLDatabase mockDb;

    @Before
    public void setUp() {
        Set<String> mockTables = new HashSet<>();
        mockTables.add("users");
        mockTables.add("roles");

        this.mockDb = mock(SQLDatabase.class);
        when(mockDb.containsTable("user")).thenReturn(false);
        when(mockDb.getTableNames()).thenReturn(mockTables);
    }

    /**
     * Test 13.1
	 * Check that the runRepairer function works as expected. It should
	 * return the query “SELECT * FROM users” having replaced the table
	 * name “user” with “users” when run over the mock database.
     * @throws ChainDataSourceException
     * @throws SQLException
     * @throws SPSMMatchingException
     * @throws SMatchException
     */
    @Test
    public void runRepairer() throws ChainDataSourceException, SQLException, WordNetMatchingException, SMatchException {
        SQLQueryRepair queryRepair = new SQLQueryRepair( "SELECT * from user", mockDb);
        String repairedQuery = queryRepair.runRepairer();
        assertEquals("SELECT * FROM users", repairedQuery);
    }

    /**
     * Test 13.2
	 * Check that the repairColumnNames functionality works as expected by
	 * comparing the value returned from this function with the manually
	 * repaired query “SELECT lastname FROM users”. The function should have
	 * replaced “surname” with “lastname”.
     * @throws SQLException
     * @throws ChainDataSourceException
     */
    @Test
    public void testSetColumns() throws SQLException, ChainDataSourceException {
        SQLQueryRepair queryRepair = new SQLQueryRepair( "SELECT surname FROM users", mockDb);
        Map<String, String> test = new HashMap<>();
        test.put("surname", "lastname");
        queryRepair.repairColumnNames(test);

        String query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users", query);

        // Test EqualTo expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname = 'Smith'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname = 'Smith'", query);

        // Test GreaterThan expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname > 'Smith'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname > 'Smith'", query);

        // Test GreaterThanEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname >= 'Smith'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname >= 'Smith'", query);

        // Test LessThan expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname < 'Smith'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname < 'Smith'", query);

        // Test LessThanEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname <= 'Smith'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname <= 'Smith'", query);

        // Test NotEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname != 'Smith'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname != 'Smith'", query);

        // Test NotEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname IS NULL", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname IS NULL", query);

        // Test Like expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname LIKE '%Smith%'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname LIKE '%Smith%'", query);

        // Test In expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname IN ('Smith', 'Union')", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname IN ('Smith', 'Union')", query);

        // Test OR expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname = 'Smith' OR surname = 'Union'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname = 'Smith' OR lastname = 'Union'", query);

        // Test AND expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname = 'Smith' AND surname = 'Union'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname = 'Smith' AND lastname = 'Union'", query);

        // Test BETWEEN expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname BETWEEN 0 AND 10", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname BETWEEN 0 AND 10", query);
    }
}