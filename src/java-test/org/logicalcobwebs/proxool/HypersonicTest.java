/*
* Copyright 2002, Findexa AS (http://www.findexa.no)
*
* This software is the proprietary information of Findexa AS.
* Use is subject to license terms.
*/
package org.logicalcobwebs.proxool;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Very basic test to see if Hypersonic test database is working
 *
 * @version $Revision: 1.4 $, $Date: 2002/11/09 16:01:38 $
 * @author Bill Horsman (bill@logicalcobwebs.co.uk)
 * @author $Author: billhorsman $ (current maintainer)
 * @since Proxool 0.5
 */
public class HypersonicTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(HypersonicTest.class);

    private static final String TEST_TABLE = "test";

    public HypersonicTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        AllTests.globalSetup();
        TestHelper.createTable(TEST_TABLE);
    }

    protected void tearDown() throws Exception {
        TestHelper.dropTable(TEST_TABLE);
        AllTests.globalTeardown();
    }

    public void testInsert() throws SQLException, ClassNotFoundException {

        Connection c;

        c = TestHelper.getDirectConnection();
        TestHelper.insertRow(c, TEST_TABLE);
        TestHelper.insertRow(c, TEST_TABLE);
        TestHelper.insertRow(c, TEST_TABLE);
        assertEquals("Wrong number of rows added", 3, TestHelper.getCount(c, TEST_TABLE));

    }

}

/*
 Revision history:
 $Log: HypersonicTest.java,v $
 Revision 1.4  2002/11/09 16:01:38  billhorsman
 fix doc

 Revision 1.3  2002/11/02 14:22:16  billhorsman
 Documentation

 Revision 1.2  2002/11/02 13:57:34  billhorsman
 checkstyle

 Revision 1.1  2002/10/29 23:18:30  billhorsman
 Tests that hypersonic db works ok

*/