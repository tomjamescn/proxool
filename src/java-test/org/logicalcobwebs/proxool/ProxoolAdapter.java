/*
* Copyright 2002, Findexa AS (http://www.findexa.no)
*
* This software is the proprietary information of Findexa AS.
* Use is subject to license terms.
*/
package org.logicalcobwebs.proxool;

import org.logicalcobwebs.dbscript.ConnectionAdapterIF;
import org.logicalcobwebs.logging.Log;
import org.logicalcobwebs.logging.LogFactory;

import java.util.Properties;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides Proxool connections to the {@link org.logicalcobwebs.dbscript.ScriptFacade ScriptFacade}
 *
 * @version $Revision: 1.15 $, $Date: 2003/02/06 17:41:03 $
 * @author Bill Horsman (bill@logicalcobwebs.co.uk)
 * @author $Author: billhorsman $ (current maintainer)
 * @since Proxool 0.5
 */
public class ProxoolAdapter implements ConnectionAdapterIF, ConfigurationListenerIF {

    private static final Log LOG = LogFactory.getLog(ProxoolAdapter.class);

    private String alias = String.valueOf(hashCode());

    private String fullUrl;

    private ConnectionPoolDefinitionIF connectionPoolDefinition;

    private Properties changedInfo;

    private Properties completeInfo;

    /**
     * Use this constructor if you want to define the alias
     * @param alias the alias of the pool
     */
    public ProxoolAdapter(String alias) {
        this.alias = alias;
    }

    /**
     * Default constructor. Will use the hashCode as the alias for the pool
     */
    public ProxoolAdapter() {
    }

    public void defintionUpdated(ConnectionPoolDefinitionIF connectionPoolDefinition, Properties completeInfo, Properties changedInfo) {
        setConnectionPoolDefinition(connectionPoolDefinition);
        setCompleteInfo(completeInfo);
        setChangedInfo(changedInfo);
        LOG.debug("Definition updated " + connectionPoolDefinition.getCompleteUrl());
        if (changedInfo != null && changedInfo.size() > 0) {
            LOG.debug(changedInfo.size() + " properties updated");
        } else {
            LOG.debug("No properties updated");
        }
    }

    public ConnectionPoolDefinitionIF getConnectionPoolDefinition() {
        return connectionPoolDefinition;
    }

    public void setConnectionPoolDefinition(ConnectionPoolDefinitionIF connectionPoolDefinition) {
        this.connectionPoolDefinition = connectionPoolDefinition;
        LOG.debug("Setting cpd to " + this.connectionPoolDefinition);
    }

    public Properties getChangedInfo() {
        return changedInfo;
    }

    public void setChangedInfo(Properties changedInfo) {
        this.changedInfo = changedInfo;
    }

    public Properties getCompleteInfo() {
        return completeInfo;
    }

    public void setCompleteInfo(Properties completeInfo) {
        this.completeInfo = completeInfo;
    }

    public String getName() {
        return "proxool";
    }

    public void update(Properties info) throws SQLException, ProxoolException {
        ProxoolFacade.updateConnectionPool(getFullUrl(), info);
    }

    public void update(String url) throws SQLException, ProxoolException {
        ProxoolFacade.updateConnectionPool(url, null);
    }

    public void setup(String driver, String url, Properties info) throws SQLException, ProxoolException {

        try {
            Class.forName(ProxoolDriver.class.getName());
        } catch (ClassNotFoundException e) {
            throw new SQLException("Couldn't find " + driver);
        }

        fullUrl = TestHelper.buildProxoolUrl(alias, driver, url);
        ProxoolFacade.registerConnectionPool(fullUrl, info);
        ProxoolFacade.setConfigurationListener(alias, this);
    }

    public Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(ProxoolConstants.PROXOOL
                + ProxoolConstants.ALIAS_DELIMITER + alias);
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void tearDown()  {
        try {
            ProxoolFacade.removeConnectionPool(alias);
        } catch (ProxoolException e) {
            LOG.error("Problem tearing down " + alias, e);
        }
    }

}

/*
 Revision history:
 $Log: ProxoolAdapter.java,v $
 Revision 1.15  2003/02/06 17:41:03  billhorsman
 now uses imported logging

 Revision 1.14  2003/01/23 11:13:40  billhorsman
 use new setConfiguratorListener method

 Revision 1.13  2003/01/18 15:13:13  billhorsman
 Signature changes (new ProxoolException
 thrown) on the ProxoolFacade API.

 Revision 1.12  2003/01/17 00:38:12  billhorsman
 wide ranging changes to clarify use of alias and url -
 this has led to some signature changes (new exceptions
 thrown) on the ProxoolFacade API.

 Revision 1.11  2002/12/16 16:41:59  billhorsman
 allow URL updates to pool

 Revision 1.10  2002/12/12 10:49:43  billhorsman
 now includes properties in definitionChanged event

 Revision 1.9  2002/12/04 13:20:10  billhorsman
 ConfigurationListenerIF test

 Revision 1.8  2002/11/13 20:23:38  billhorsman
 change method name, throw exceptions differently, trivial changes

 Revision 1.7  2002/11/09 16:09:06  billhorsman
 checkstyle

 Revision 1.6  2002/11/09 16:02:05  billhorsman
 fix doc

 Revision 1.5  2002/11/09 14:45:35  billhorsman
 only close connection if it is open

 Revision 1.4  2002/11/07 18:56:59  billhorsman
 allow explicit definition of alias

 Revision 1.3  2002/11/02 14:22:16  billhorsman
 Documentation

 Revision 1.2  2002/11/02 12:46:42  billhorsman
 improved debug

 Revision 1.1  2002/11/02 11:37:48  billhorsman
 New tests

*/
