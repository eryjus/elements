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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.eryjus.cba.ElementsApp;

//-------------------------------------------------------------------------------------------------------------------
// class ElementsModel -- this class is an extension of DatabaseModel, so it establishes and maintains a connection
//                        the the database that will hold the Elements tables.  It will be responsible for retrieving
//                        from and storing to the Elements database data store.
//-------------------------------------------------------------------------------------------------------------------
public class ElementsModel extends DatabaseModel {
    private String elementName = new String("");
    private String elementDescription = null;
    private String elementType = new String("VARCHAR");
    private Integer elementSize = null;
    private Integer elementDecimals = null;
    private Boolean elementUnsigned = null;
    private Boolean elementZeroFill = null;
    private Boolean elementAutoIncrement = null;
    private String elementCharSet = null;
    private String elementCollateName = null;
    private Boolean elementNotNull = null;
    private String elementShortDescription = new String("");
    private String elementDefaultValue = null;
    private String elementCreateUser = ElementsApp.getUser();
    private Timestamp elementCreateDateTime = Timestamp.valueOf(LocalDateTime.now());
    private String elementCreateSystem = "localhost";
    private String elementModifyUser = null;
    private Timestamp elementModifyDateTime = null;
    private String elementModifySystem = null;

    private final String VALID_TYPES[] = { 
        "BIT", 
        "TINYINT", 
        "SMALLINT", 
        "MEDIUMINT", 
        "INT", 
        "BIGINT", 
        "DECIMAL", 
        "FLOAT", 
        "DOUBLE", 
        "DATE", 
        "DATETIME", 
        "TIMESTAMP",
        "TIME", 
        "YEAR", 
        "CHAR", 
        "VARCHAR", 
        "BINARY", 
        "VARBINARY", 
        "TINYBLOB",
        "TINYTEXT", 
        "BLOB", 
        "TEXT", 
        "MEDIUMBLOB", 
        "MEDIUMTEXT", 
        "LONGBLOB",
        "LONGTEXT"
    };



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
    // checkExistence() -- checks the existence of an element in the Elements table.  Returns true if it is known
    //                     to exist and false if some error occurred or it does not exist.
    //---------------------------------------------------------------------------------------------------------------
    public boolean checkExistence(String name) {
        name = name.toLowerCase().trim();
        String sql;

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "SELECT element_name " +
        "FROM " + schema + ".elements " +
        "WHERE element_name = '" + name + "'";
        //- SQL -----------------------------------------------------------------------------------------------------

        try {
            rs = stmt.executeQuery(sql);
            if (rs.first()) return true;
        } catch (Exception ex) {
            LOGGER.error("Unable to check existence of ''" + name + "':", ex);
        }

        return false;
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
        LOGGER.debug("Creating the business data elements table");

        sql = 
        //- SQL -----------------------------------------------------------------------------------------------------
        "CREATE TABLE " + schema + ".elements " +
        "( " +
        "   element_name                VARCHAR(64) NOT NULL, " +
        "   element_short_description   VARCHAR(120) NOT NULL, " +
        "   element_type                VARCHAR(10) NOT NULL, " +
        "   element_not_null            TINYINT(1) NOT NULL, " +
        "   element_size                INT unsigned NULL, " +
        "   element_decimals            TINYINT unsigned NULL, " +
        "   element_unsigned            TINYINT(1) NULL, " +
        "   element_zero_fill           TINYINT(1) NULL, " +
        "   element_auto_increment      TINYINT(1) NULL, " +
        "   element_char_set            VARCHAR(25) NULL, " +
        "   element_collate_name        VARCHAR(25) NULL, " +
        "   element_default_value       VARCHAR(1000) NULL, " +
        "   element_description         TINYTEXT NULL, " +
        "   element_create_id           VARCHAR(25) NOT NULL, " +
        "   element_create_date_time    TIMESTAMP(6) NOT NULL, " +
        "   element_create_system       VARCHAR(120) NOT NULL, " +
        "   element_modify_id           VARCHAR(25) NULL, " +
        "   element_modify_date_time    TIMESTAMP(6) NULL, " +
        "   element_modify_system       VARCHAR(120) NULL, " +
        "" +
        "PRIMARY KEY (element_name) " +
        ")";
        //- SQL -----------------------------------------------------------------------------------------------------

        stmt.execute(sql);
    }


    //---------------------------------------------------------------------------------------------------------------
    // elementName access methods; cannot be null so no null access methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementName() { return this.elementName; }
    public void setElementName(String n) { 
        if (null == n) n = "";
        this.elementName = n; 
    }


    //---------------------------------------------------------------------------------------------------------------
    // elementDescription access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementDescription() { 
        if (null == this.elementDescription) return "";
        else return this.elementDescription; 
    }
    public String getElementDescriptionUnsafe() { return this.elementDescription; }
    public boolean isElementDescriptionNull() { return (null == this.elementDescription); }
    public void setElementDescriptionNull() { this.elementDescription = null; }
    public void setElementDescription(String d) { 
        if (null == d) d = "";
        this.elementDescription = d; 
    }


    //---------------------------------------------------------------------------------------------------------------
    // elementType access methods; cannot be null so no null access methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementType() { return this.elementType; }
    public void setElementType(String t) { 
        if (null == t) t = "";
        if (!Arrays.asList(VALID_TYPES).contains(t)) t = "VARCHAR";
        this.elementType = t; 
    }


    //---------------------------------------------------------------------------------------------------------------
    // elementSize access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public Integer getElementSize() { 
        if (null == this.elementSize) return 0;
        else return this.elementSize; 
    }
    public Integer getElementSizeUnsafe() { return this.elementSize; }
    public void setElementSize(Integer s) { 
        if (s < 0) s = 0;
        this.elementSize = s; 
    }
    public void setElementSize(int s) { 
        if (s < 0) s = 0;
        this.elementSize = Integer.valueOf(s); 
    }
    public void setElementSize(String s) { 
        if (null == s) s = "0";
        try { this.elementSize = Integer.valueOf(s); } catch (Exception ex) { this.elementSize = 0; }
    }
    public boolean isElementSizeNull() { return (null == this.elementSize); }
    public void setElementSizeNull() { this.elementSize = null; }


    //---------------------------------------------------------------------------------------------------------------
    // elementDecimals access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public Integer getElementDecimals() { 
        if (null == this.elementDecimals) return 0;
        else return this.elementDecimals; 
    }
    public Integer getElementDecimalsUnsafe() { return this.elementDecimals; }
    public void setElementDecimals(Integer d) { 
        if (d < 0) d = 0;
        this.elementDecimals = d; 
    }
    public void setElementDecimals(int d) { 
        if (d < 0) d = 0;
        this.elementDecimals = Integer.valueOf(d); 
    }
    public void setElementDecimals(String d) { 
        if (null == d) d = "0";
        try { this.elementDecimals = Integer.valueOf(d); } catch(Exception ex) { this.elementDecimals = 0; }
    }
    public boolean isElementDecimalsNull() { return (null == this.elementDecimals); }
    public void setElementDecimalsNull() { this.elementDecimals = null; }
    

    //---------------------------------------------------------------------------------------------------------------
    // elementUnsigned access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public Boolean getElementUnsigned() { 
        if (null == this.elementUnsigned) return false;
        else return this.elementUnsigned; 
    }
    public Boolean getElementUnsignedUnsafe() { return this.elementUnsigned; }
    public void setElementUnsigned(Boolean u) { 
        if (null == u) u = false;
        this.elementUnsigned = u; 
    }
    public void setElementUnsigned(boolean u) { this.elementUnsigned = Boolean.valueOf(u); }
    public void setElementUnsigned(String u) { 
        if (null == u) u = "false";
        try { this.elementUnsigned = Boolean.valueOf(u); } catch(Exception ex) { this.elementUnsigned = false; }
    }
    public boolean isElementUnsignedNull() { return (null == this.elementUnsigned); }
    public void setElementUnsignedNull() { this.elementUnsigned = null; }


    //---------------------------------------------------------------------------------------------------------------
    // elementZeroFill access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public Boolean getElementZeroFill() { 
        if (null == this.elementZeroFill) return false;
        else return this.elementZeroFill; 
    }
    public Boolean getElementZeroFillUnsafe() { return this.elementZeroFill; }
    public void setElementZeroFill(Boolean z) { 
        if (null == z) z = false;
        this.elementZeroFill = z; 
    }
    public void setElementZeroFill(boolean z) { this.elementZeroFill = Boolean.valueOf(z); }
    public void setElementZeroFill(String z) { 
        if (null == z) z = "false";
        try { this.elementZeroFill = Boolean.valueOf(z); } catch(Exception ex) { this.elementZeroFill = false; }
    }
    public boolean isElementZeroFillNull() { return (null == this.elementZeroFill); }
    public void setElementZeroFillNull() { this.elementZeroFill = null; }


    //---------------------------------------------------------------------------------------------------------------
    // elementAutoIncrement access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public Boolean getElementAutoIncrement() { 
        if (null == this.elementAutoIncrement) return false;
        else return this.elementAutoIncrement; 
    }
    public Boolean getElementAutoIncrementUnsafe() { return this.elementAutoIncrement; }
    public void setElementAutoIncrement(Boolean a) { 
        if (null == a) a = false;
        this.elementAutoIncrement = a; 
    }
    public void setElementAutoIncrement(boolean a) { this.elementAutoIncrement = Boolean.valueOf(a); }
    public void setElementAutoIncrement(String a) { 
        if (null == a) a = "false";

        try { 
            this.elementAutoIncrement = Boolean.valueOf(a); 
        } catch(Exception ex) { 
            this.elementAutoIncrement = false;
        }
    }
    public boolean isElementAutoIncrementNull() { return (null == this.elementAutoIncrement); }
    public void setElementAutoIncrementNull() { this.elementAutoIncrement = null; }


    //---------------------------------------------------------------------------------------------------------------
    // elementCharSet access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementCharSet() { 
        if (null == this.elementCharSet) return "";
        else return this.elementCharSet; 
    }
    public String getElementCharSetUnsafe() { return this.elementCharSet; }
    public void setElementCharSet(String s) { 
        if (null == s) s = "";
        this.elementCharSet = s; 
    }
    public boolean isElementCharSetNull() { return (null == this.elementCharSet); }
    public void setElementCharSetNull() { this.elementCharSet = null; }


    //---------------------------------------------------------------------------------------------------------------
    // elementCollateName access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementCollateName() { 
        if (null == this.elementCollateName) return "";
        else return this.elementCollateName; 
    }
    public String getElementCollateNameUnsafe() { return this.elementCollateName; }
    public void setElementCollateName(String c) { 
        if (null == c) c = "";
        this.elementCollateName = c; 
    }
    public boolean isElementCollateNameNull() { return (null == this.elementCollateName); }
    public void setElementCollateNameNull() { this.elementCollateName = null; }


    //---------------------------------------------------------------------------------------------------------------
    // elementNotNull access methods; cannot be null so no null access methods
    //---------------------------------------------------------------------------------------------------------------
    public Boolean getElementNotNull() { return this.elementNotNull; }
    public void setElementNotNull(Boolean n) { 
        if (null == n) n = false;
        this.elementNotNull = n; 
    }
    public void setElementNotNull(boolean n) { this.elementNotNull = Boolean.valueOf(n); }
    public void setElementNotNull(String n) { 
        if (null == n) n = "false";
        try { this.elementNotNull = Boolean.valueOf(n); } catch(Exception ex) { this.elementNotNull = false; }
    }


    //---------------------------------------------------------------------------------------------------------------
    // elementShortDescription access methods; cannot be null so no null access methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementShortDescription() { return this.elementShortDescription; }
    public void setElementShortDescription(String d) { 
        if (null == d) d = "";
        this.elementShortDescription = d; 
    }


    //---------------------------------------------------------------------------------------------------------------
    // elementDefaultValue access methods; null is allowed, so include safe and null methods
    //---------------------------------------------------------------------------------------------------------------
    public String getElementDefaultValue() { 
        if (null == this.elementDefaultValue) return "";
        else return this.elementDefaultValue; 
    }
    public String getElementDefaultValueUnsafe() { return this.elementDefaultValue; }
    public void setElementDefaultValue(String v) { 
        if (null == v) v = "";
        this.elementDefaultValue = v; 
    }
    public boolean isElementDefaultValueNull() { return (null == this.elementDefaultValue); }
    public void setElementDefaultValueNull() { this.elementDefaultValue = null; }


    //---------------------------------------------------------------------------------------------------------------
    // insertElement() write a record to the database
    //---------------------------------------------------------------------------------------------------------------
    public boolean insertElement() throws Exception {
        String sql =
        //- SQL -----------------------------------------------------------------------------------------------------
        "INSERT INTO " + schema + ".elements " +
        "(" +
        "   element_name, " +
        "   element_short_description, " +
        "   element_type, " +
        "   element_not_null, " +
        "   element_size, " +
        "   element_decimals, " +
        "   element_unsigned, " +
        "   element_zero_fill, " +
        "   element_auto_increment, " +
        "   element_char_set, " +
        "   element_collate_name, " +
        "   element_default_value, " +
        "   element_description, " +
        "   element_create_id, " +
        "   element_create_date_time, " +
        "   element_create_system, " +
        "   element_modify_id, " +
        "   element_modify_date_time, " +
        "   element_modify_system" +
        ") " +
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(6),substring_index(user(),'@',-1),null,null,null)";
        //- SQL -----------------------------------------------------------------------------------------------------

        PreparedStatement pStmt = conn.prepareStatement(sql);

        pStmt.setString(1, getElementName());
        pStmt.setString(2, getElementShortDescription());
        pStmt.setString(3, getElementType());
        pStmt.setBoolean(4, getElementNotNull());

        if (isElementSizeNull()) {
            LOGGER.debug("Setting size to `null`");
            pStmt.setNull(5, Types.INTEGER); 
        } else {
            LOGGER.debug("Size has a value");
            pStmt.setInt(5, getElementSize());
        }

        if (isElementDecimalsNull()) pStmt.setNull(6, Types.TINYINT); 
        else pStmt.setInt(6, getElementDecimals());

        if (isElementUnsignedNull()) pStmt.setNull(7, Types.TINYINT); 
        else pStmt.setBoolean(7, getElementUnsigned());

        if (isElementZeroFillNull()) pStmt.setNull(8, Types.TINYINT); 
        else pStmt.setBoolean(8, getElementZeroFill());

        if (isElementAutoIncrementNull()) pStmt.setNull(9, Types.TINYINT); 
        else pStmt.setBoolean(9, getElementAutoIncrement());

        if (isElementCharSetNull()) pStmt.setNull(10, Types.VARCHAR); 
        else pStmt.setString(10, getElementCharSet());

        if (isElementCollateNameNull()) pStmt.setNull(11, Types.VARCHAR); 
        else pStmt.setString(11, getElementCollateName());

        if (isElementDefaultValueNull()) pStmt.setNull(12, Types.VARCHAR); 
        else pStmt.setString(12, getElementDefaultValue());

        if (isElementDescriptionNull()) pStmt.setNull(13, Types.VARCHAR); 
        else pStmt.setString(13, getElementDescription());

        pStmt.setString(14, ElementsApp.getUser());

        return pStmt.execute();
    }
}