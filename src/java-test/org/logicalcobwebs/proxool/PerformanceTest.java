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
import org.logicalcobwebs.dbscript.ScriptFacade;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Tests how fast Proxool is compared to the "perfect" pool, {@link SimpoolAdapter}.
 *
 * @version $Revision: 1.5 $, $Date: 2002/11/09 16:01:53 $
 * @author Bill Horsman (bill@logicalcobwebs.co.uk)
 * @author $Author: billhorsman $ (current maintainer)
 * @since Proxool 0.5
 */
public class PerformanceTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(PerformanceTest.class);

    public PerformanceTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        AllTests.globalSetup();
    }

    protected void tearDown() throws Exception {
        AllTests.globalTeardown();
    }

    public void testScript() throws ParserConfigurationException, SAXException, IOException, SQLException {
        String scriptLocation = System.getProperty("script");
        if (scriptLocation != null) {
            ScriptFacade.runScript(scriptLocation, new ProxoolAdapter());
            ScriptFacade.runScript(scriptLocation, new SimpoolAdapter());
        } else {
            LOG.info("Skipping performance test because 'script' System Property was not set");
        }
    }

}

/*
 Revision history:
 $Log: PerformanceTest.java,v $
 Revision 1.5  2002/11/09 16:01:53  billhorsman
 fix doc

 Revision 1.4  2002/11/02 14:22:16  billhorsman
 Documentation

 Revision 1.3  2002/11/02 13:57:34  billhorsman
 checkstyle

 Revision 1.2  2002/11/02 11:37:48  billhorsman
 New tests

 Revision 1.1  2002/10/30 21:17:51  billhorsman
 new performance tests

*/