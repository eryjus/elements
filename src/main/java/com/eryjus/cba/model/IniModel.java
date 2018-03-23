//===================================================================================================================
// IniModel.java -- This is the model that is responsible for the cba.ini values
// -----------------------------------------------------------------------------------------------------------------
//d
// This class is responsible for reading the values from the cba.ini file.  Each element that can be read will
// have its own method to ensure the default values are consistent.
//
// -----------------------------------------------------------------------------------------------------------------
//
//    Date     Programmer    Version    Comment
// ----------  ----------  -----------  ----------------------------------------------------------------------------
// 2018-03-03     adcl       v0.1.0     Initial version
//
//===================================================================================================================


package com.eryjus.cba.model;

import java.io.File;
import java.util.prefs.Preferences;

import com.eryjus.cba.ElementsApp;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;


//-------------------------------------------------------------------------------------------------------------------
// class IniModel -- this class is responsible for reading data from the cba.ini file, taking into account the 
//                   default values for each [stanza] key= combination.  The access methods are the only manner in 
//                   which a value can be retrieved to enforce consistent standards.  Additionally, this class is 
//                   declared as `final` to prevent the defaults from being overridden somewhere else.
//-------------------------------------------------------------------------------------------------------------------
public final class IniModel {
    private Ini ini;
    private Preferences prefs;


    //---------------------------------------------------------------------------------------------------------------
    // IniModel constructor -- this constructor will open an existing cba.ini file and will serve its configuration
    //                         options via access methods.  Note that the file must exist or an empty file will be 
    //                         created; there is no check for an existing file.
    //---------------------------------------------------------------------------------------------------------------
    public IniModel () {
        ElementsApp.LOGGER.trace("Initializing IniModel class");

        String fn = "cba.ini";
        File file = new File(fn);
        try {
            if (!file.exists()) file.createNewFile();

            ini = new Ini(file); 
            prefs = new IniPreferences(ini);
        } catch (Exception ex) { 
            ElementsApp.LOGGER.trace("Unable to load " + fn, ex);
            ini = null;
            prefs = null; 
        }
    }


    //---------------------------------------------------------------------------------------------------------------
    // getStringValue() -- This is the workhorse of the class, where all values will be read from the file as 
    //                     String and it will be up to the calling methods to convert the value as necessary.
    //---------------------------------------------------------------------------------------------------------------
    private String getStringValue(String stanza, String option, String dftVal) {
        ElementsApp.LOGGER.debug("Requested to read [" + stanza + "] and option \"" + option + "\"");
        return prefs.node(stanza).get(option, dftVal);
    }


    //---------------------------------------------------------------------------------------------------------------
    // getElement*() -- these functions are used to retrieve configuration options for the [elements] stanza.  The
    //                  default values are established in these methods.
    //---------------------------------------------------------------------------------------------------------------
    public String getElementSchema() { return getStringValue("elements", "schema", "cba_metadata"); }
    public String getElementUser() { return getStringValue("elements", "user", "cba"); }
    public String getElementPassword() { return getStringValue("elements", "password", "C00lApp$"); }


    //---------------------------------------------------------------------------------------------------------------
    // getSystem*() -- these functions are used to retrieve configuration options for the [system] stanza.  The
    //                 default values are established in these methods.
    //---------------------------------------------------------------------------------------------------------------
    public String getSystemSchema() { return getStringValue("system", "schema", "cba_system"); }
    public String getSystemUser() { return getStringValue("system", "user", "cba"); }
    public String getSystemPassword() { return getStringValue("system", "password", "C00lApp$"); }
}