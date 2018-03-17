//===================================================================================================================
// SecurityModel.java -- This class will handle the security validation and persistent stores
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


package com.eryjus.cba.model;


//-------------------------------------------------------------------------------------------------------------------
// class SecurityModel -- this class is an extension of DatabaseModel.  As such, it will establish a connection to 
//                        the schema that holds the security data for authenticating users.  It will be responsible
//                        for retrieving data from and saving updates to the security tables.
//-------------------------------------------------------------------------------------------------------------------
public class SecurityModel extends DatabaseModel {

    //---------------------------------------------------------------------------------------------------------------
    // SecurityModel constructor -- constructs an instance of the SecurityModel.  This involves establishing a 
    //                              connection to the database and checking for the schema's existence and creating
    //                              it if necessary.  One special task here will be to populate the default `admin`
    //                              user if we are creating the security database.  Someone has to be able to log in.
    //---------------------------------------------------------------------------------------------------------------
    public SecurityModel() throws Exception {
        super(INI.getSystemSchema(), INI.getSystemUser(), INI.getSystemPassword());

        if (!doesSchemaExist()) initSecurity();
    }


    //---------------------------------------------------------------------------------------------------------------
    // initSecurity() -- responsible for creating the security schema and the users table within the schema.  Also, 
    //                   this method will populate the default `admin` user into the users table.  It is important 
    //                   to note that the default `admin` user's password is passed over the database connection
    //                   in clear test, allowing the MySQL server to encrypt it before writing to the database.
    //---------------------------------------------------------------------------------------------------------------
    private void initSecurity() throws Exception {
        String sql;

        // -- create the schema
        LOGGER.debug("Creating the {} database", schema);

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "CREATE SCHEMA " + schema + 
        "   CHARACTER SET utf8 " + 
        "   COLLATE utf8_general_ci";
        //- SQL -----------------------------------------------------------------------------------------------------

        stmt.execute(sql);

        // -- create the users table
        LOGGER.debug("Creating the users table");

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "CREATE TABLE " + schema + ".users " + 
        "(" + 
        "   user_id         VARCHAR(25), " +
        "   user_name       VARCHAR(120), " +
        "   user_password   CHAR(32), " +
        "   user_email      VARCHAR(120), " +
        "   user_enabled    TINYINT(1), " +
        "" + 
        "PRIMARY KEY (user_id)" +
        ")";
        //- SQL -----------------------------------------------------------------------------------------------------

        stmt.execute(sql);

        // -- add the default admin user
        LOGGER.debug("Establishing the admin user");

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "INSERT INTO " + schema + ".users " +
        "    VALUES ('admin', 'Admin User', MD5('password'), 'email@example.com', TRUE)";
        //- SQL -----------------------------------------------------------------------------------------------------

        stmt.execute(sql);
    }


    //---------------------------------------------------------------------------------------------------------------
    // authenticateUser() -- given the user and password, authenticate the user to make sure they can log in.  A
    //                       user might be disabled and if so, the user is not allowed to log in (returns false).
    //                       It is important to note that the password is passed over the database connection in
    //                       clear text, allowing the MySQL server to encrypt it before querying the table.
    //---------------------------------------------------------------------------------------------------------------
    public boolean authenticateUser(String user, String pwd) throws Exception {
        String sql;

        user = user.trim();
        user = user.toLowerCase();

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "SELECT user_enabled " + 
        "   FROM " + schema + ".users " + 
        "   WHERE user_id = '" + user + "'" +
        "       AND user_password = MD5('" + pwd + "')";
        //- SQL -----------------------------------------------------------------------------------------------------

        rs = stmt.executeQuery(sql);

        // -- user name/password do not match
        if (!rs.first()) {
            LOGGER.warn("User name or password is not valid");
            return false;
        } else {
            // -- is the user enabled?
            if (rs.getBoolean(1)) return true;
            else {
                LOGGER.warn("User is noe enabled to log in");
                return false;
            }
        }
    }
}