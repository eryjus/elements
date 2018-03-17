//===================================================================================================================
// DatabaseModel.java -- This class will handle the low-level connections to the database
// -----------------------------------------------------------------------------------------------------------------
//
// This class is the application responsible for establishing a connection with a database.
//
// -----------------------------------------------------------------------------------------------------------------
//
//    Date     Programmer    Version    Comment
// ----------  ----------  -----------  ----------------------------------------------------------------------------
// 2018-03-14     adcl       v0.1.0     Initial version
//
//===================================================================================================================


package com.eryjus.cba.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//-------------------------------------------------------------------------------------------------------------------
// class DatabaseModel -- this is a lower level class that is used to maintain a connection to a database and in
//                        particular to a schema.  The purpose is to abstract some of the messiness of connection
//                        to and running SQL statements by getting the connection and maintaining an internal
//                        Statement that is ready to run.  The result set is kept internally as well.  Therefore, 
//                        the child class already have these at their disposal.
//-------------------------------------------------------------------------------------------------------------------
class DatabaseModel {
    Connection conn;
    Statement stmt;
    ResultSet rs;
    String schema;
    static IniModel INI = new IniModel();
    static Logger LOGGER = null;


    //---------------------------------------------------------------------------------------------------------------
    // DatabaseModel constructor -- this constructor takes a database schema, user, and password and establishes
    //                              a connection to the database server.  It is important to note that this does not
    //                              change the default schema for the connection to be that of the `db` parameter.
    //                              The reason for this is that the database might not exist when making the 
    //                              connection.  As a result, any SQL statement will need to use a qualified name.
    //---------------------------------------------------------------------------------------------------------------
    DatabaseModel(String db, String user, String pwd) throws Exception {
        if (null == LOGGER) LOGGER = LogManager.getLogger();

        // -- load the driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            LOGGER.fatal("Unable to load the SQL driver", ex);
            throw ex;
        }

        // -- set up some properties for the connection
        Properties connProps = new Properties();
        connProps.put("user", user);
        connProps.put("password", pwd);
        schema = db;

        // -- establish the connection to the server and allocate a Statement object
        conn = DriverManager.getConnection("jdbc:mysql://localhost/", connProps);
        stmt = conn.createStatement();
    }


    //---------------------------------------------------------------------------------------------------------------
    // doesSchemaExist() -- inquires with the database to see if the schema exists.  Existence is a bit of a fallacy
    //                      since it could exist and the user might not have permission to the schema and this 
    //                      function will return false.  This is important to note since a decision to create a 
    //                      schema that already exists but the connected user does not have permission will result
    //                      in an error and likely exception.
    //---------------------------------------------------------------------------------------------------------------
    boolean doesSchemaExist() throws SQLException {   
        LOGGER.debug("Checking for the existence of " + schema);

        String sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "SELECT schema_name " + 
        "FROM information_schema.schemata " + 
        "WHERE schema_name = '" + schema + "'";
        //- SQL -----------------------------------------------------------------------------------------------------

        rs = stmt.executeQuery(sql);
        
        return rs.first();
    }
}