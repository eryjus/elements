//===================================================================================================================
// SecurityController.java -- This class will handle the security administration for users
// -----------------------------------------------------------------------------------------------------------------
//
// This class is the application responsible for interfacing with MySQL to read, write and update the Security 
// data for users to the cba_system database.
//
// -----------------------------------------------------------------------------------------------------------------
//
//    Date     Programmer    Version    Comment
// ----------  ----------  -----------  ----------------------------------------------------------------------------
// 2018-03-11     adcl       v0.1.0     Initial version
//
//===================================================================================================================


package com.eryjus.cba.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.eryjus.cba.ElementsApp;


/**
 * The controller for security and log ins
 */
public class SecurityController {
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
    private static String schema;


    /**
     * private constructor to prevent instantiation
     */
    private SecurityController() {}


    /**
     * static initialization function to initialize the class members
     */
    public static void initialize() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            ElementsApp.LOGGER.fatal("Unable to load the SQL driver");
            throw ex;
        }

        Properties connProps = new Properties();
        connProps.put("user", IniController.getSystemUser());
        connProps.put("password", IniController.getSystemPassword());
        schema = IniController.getSystemSchema();

        conn = DriverManager.getConnection("jdbc:mysql://localhost/", connProps);

        stmt = conn.createStatement();
        String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = '" + 
                IniController.getSystemSchema() + "'";

        rs = stmt.executeQuery(sql);
        
        if (!rs.first()) {
            // we have no records so create the database and populate the default data
            initSecurity();
        }
    }


    /**
     * When the database does not exist, create it and populate defaults
     */
    private static void initSecurity() throws Exception {
        String sql;

        ElementsApp.LOGGER.debug("Creating the {} database", schema);
        sql = "CREATE SCHEMA " + schema + " CHARACTER SET utf8 COLLATE utf8_general_ci";
        stmt.execute(sql);

        ElementsApp.LOGGER.debug("Creating the users table");
        sql = "CREATE TABLE " + schema + ".users (" + 
            "    user_id VARCHAR(25), " +
            "    user_name VARCHAR(120), " +
            "    user_password CHAR(32), " +
            "    user_email VARCHAR(120), " +
            "    user_enabled TINYINT(1), " +
            "    PRIMARY KEY (user_id)" +
            ")";
        stmt.execute(sql);

        ElementsApp.LOGGER.debug("Establishing the admin user");
        sql = "INSERT INTO " + schema + ".users " +
            "    VALUES ('admin', 'Admin User', MD5('password'), 'email@example.com', TRUE)";
        stmt.execute(sql);
    }


    /**
     * This function will authenticate a user.
     */
    public static boolean authenticateUser(String user, String pwd) throws Exception {
        String sql;

        user = user.trim();
        user = user.toLowerCase();

        sql = "SELECT user_enabled " + 
                "FROM " + schema + ".users " + 
                "WHERE user_id = '" + user + "'" +
                "   AND user_password = MD5('" + pwd + "')";
        rs = stmt.executeQuery(sql);

        if (!rs.first()) {
            ElementsApp.LOGGER.warn("User name or password is not valid");
            return false;
        } else {
            if (rs.getBoolean(1)) return true;
            else {
                ElementsApp.LOGGER.warn("User is noe enabled to log in");
                return false;
            }
        }
    }
}