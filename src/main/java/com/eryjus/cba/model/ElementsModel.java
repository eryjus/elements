//===================================================================================================================
// ElementsModel.java -- This class will handle the persistent storage of Elements in the database
// -----------------------------------------------------------------------------------------------------------------
//
// This class is the application responsible for interfacing with MySQL to read, write and update the Elements 
// data to the cba_metadata database.
//
// -----------------------------------------------------------------------------------------------------------------
//
//    Date     Programmer    Version    Comment
// ----------  ----------  -----------  ----------------------------------------------------------------------------
// 2018-03-14     adcl       v0.1.0     Initial version
//
//===================================================================================================================


package com.eryjus.cba.model;

import java.sql.SQLException;


//-------------------------------------------------------------------------------------------------------------------
// class ElementsModel -- this class is an extension of DatabaseModel, so it establishes and maintains a connection
//                        the the database that will hold the Elements tables.  It will be responsible for retrieving
//                        from and storing to the Elements database data store.
//-------------------------------------------------------------------------------------------------------------------
public class ElementsModel extends DatabaseModel {

    //---------------------------------------------------------------------------------------------------------------
    // ElementsModel constructor -- this constructor will establish a connection to the database and then check that
    //                              the required schema exists.  In the event the schema does not exist, then the 
    //                              constructor will attempt to create it and its required tables before continuing.
    //---------------------------------------------------------------------------------------------------------------
    public ElementsModel() throws Exception {
        super(INI.getElementSchema(), INI.getElementUser(), INI.getElementPassword());
        LOGGER.info("Initialization of Elements");

        if (!doesSchemaExist()) initElements();
    }


    //---------------------------------------------------------------------------------------------------------------
    // initElements() -- this function will create the schema and tables necessary for the Elements Application; 
    //                   specific to the Elements themselves.
    //---------------------------------------------------------------------------------------------------------------
    private void initElements() throws SQLException {
        String sql;

        // -- Create the schema
        LOGGER.debug("Creating the {} database", schema);

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "CREATE SCHEMA " + schema + 
        "   CHARACTER SET utf8 " + 
        "   COLLATE utf8_general_ci";
        //- SQL -----------------------------------------------------------------------------------------------------

        stmt.execute(sql);


        // -- Create the Elements table within the schema
        LOGGER.debug("Creating the users table");

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "CREATE TABLE " + schema + ".elements " +
        "( " +
        "   element_name                VARCHAR(64) NOT NULL, " +
        "   element_type                ENUM('BIT', 'TINYINT', 'SMALLINT', 'MEDIUMINT', 'INT', 'BIGINT', " +
        "                                   'DECIMAL', 'FLOAT', 'DOUBLE', 'DATE', 'DATETIME', 'TIMESTAMP', " +
        "                                   'TIME', 'YEAR', 'CHAR', 'VARCHAR', 'BINARY', 'VARBINARY', 'TINYBLOB', " +
        "                                   'TINYTEXT', 'BLOB', 'TEXT', 'MEDIUMBLOB', 'MEDIUMTEXT', 'LONGBLOB', " +
        "                                   'LONGTEXT', 'ENUM') NOT NULL, " +
        "   element_size                INT unsigned, " +
        "   element_decimals            TINYINT unsigned, " +
        "   element_unsigned            TINYINT(1), " +
        "   element_zero_fill           TINYINT(1), " +
        "   element_auto_increment      TINYINT(1), " +
        "   element_national_flag       TINYINT(1), " +
        "   element_char_set            VARCHAR(25), " +
        "   element_collate_name        VARCHAR(25), " +
        "   element_not_null            TINYINT(1), " +
        "   element_short_description   VARCHAR(120) NOT NULL, " +
        "   element_default_value       VARCHAR(1000) NOT NULL, " +
        "   element_create_id           VARCHAR(25) NOT NULL, " +
        "   element_create_date_time    TIMESTAMP NOT NULL, " +
        "   element_create_system       VARCHAR(120) NOT NULL, " +
        "   element_modify_id           VARCHAR(25), " +
        "   element_modify_date_time    TIMESTAMP, " +
        "   element_modify_system       VARCHAR(120), " +
        "" +
        "PRIMARY KEY (element_name) " +
        ")";
        //- SQL -----------------------------------------------------------------------------------------------------

        stmt.execute(sql);
    }
}