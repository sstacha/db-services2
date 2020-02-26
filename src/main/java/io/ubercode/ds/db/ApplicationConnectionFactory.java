package io.ubercode.ds.db;

// H2 imports for the configuration server
// NOTE: we are starting by TCP so we can both use and test
import org.h2.tools.Server;

import java.sql.Statement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// OLD 1.x LOG4J; trying to upgrade to 2.x
// todo: ran into issues; figure out how to upgrade to 2.x later
import org.apache.log4j.LogManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ApplicationConnectionFactory {
    private static Logger log = Logger.getLogger(ApplicationConnectionFactory.class);
    // application connection
    private static Connection applicationConnection = null;
    // pooled external connections
    // todo: make private once we know this works
    public static Map<String, HikariDataSource> dsMap = new LinkedHashMap<String, HikariDataSource>();

    public Connection getApplicationConnection() {return applicationConnection;}


    public void setApplicationConnection(Connection connection) {
        if (ApplicationConnectionFactory.applicationConnection == null)
            ApplicationConnectionFactory.applicationConnection = connection;
    }

    public void initializeConnectionPools() {
        // NOTE: we only initialize once; if we have any existing values we bail
        if (dsMap.size() > 0) {
            log.warn("ATTEMPTED TO INITIALIZE CONNECTION POOLS BUT WAS ALREADY INITIALIZED; SKIPPING INITIALIZATION!");
            return;
        }
        // we must have an application connection available to get the external datasource properties
        if (applicationConnection == null) {
            log.warn("ATTEMPTED TO INITIALIZE CONNECTION POOLS BUT NO DEFAULT CONNECTION; SKIPPING INITIALIZATION!");
            return;
        }
        // read in the connection properties and create our datasource pools
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.getApplicationConnection().createStatement();
            rs = stmt.executeQuery("select * from test");
            while (rs.next()) {
                System.out.println(rs.getString(1) + ": " + rs.getString(2));
            }
        }
        catch (SQLException sqlex) {
            System.out.println("sql exception testing: " + sqlex);
        }
        finally {
            if (rs != null)
                try {
                    rs.close();
                }
                catch (Exception e) {
                    System.out.println("exception closing resultset: " + e);
                }
            if (stmt != null)
                try {
                    stmt.close();
                }
                catch (Exception e) {
                    System.out.println("exception closing statement: " + e);
                }
        }

    }

    public void closeConnections() {
        // start by closing all connection pools

        // last close our application connection reference
        if (applicationConnection != null) {
            try {
                applicationConnection.close();
                applicationConnection = null;
            }
            catch (Exception e) {
                System.out.println("exception closing application connection: " + e);
                applicationConnection = null;
            }
        }
    }

    public void test() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.getApplicationConnection().createStatement();
            rs = stmt.executeQuery("select * from test");
            while (rs.next()) {
                System.out.println(rs.getString(1) + ": " + rs.getString(2));
            }
        }
        catch (SQLException sqlex) {
            System.out.println("sql exception testing: " + sqlex);
        }
        finally {
            if (rs != null)
                try {
                    rs.close();
                }
                catch (Exception e) {
                    System.out.println("exception closing resultset: " + e);
                }
            if (stmt != null)
                try {
                    stmt.close();
                }
                catch (Exception e) {
                    System.out.println("exception closing statement: " + e);
                }
        }
    }
    public static void main (String[] args) {
    }
}
