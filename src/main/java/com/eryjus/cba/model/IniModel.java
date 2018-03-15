//===================================================================================================================
// IniModel.java -- This is the model that is responsible for the cba.ini values
// -----------------------------------------------------------------------------------------------------------------
//
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


/**
 * The IniControler class is used to interface with the cba.ini configuration file.
 */
public class IniModel {
    private static Ini ini;
    private static Preferences prefs;

    /**
     * Initialize the class to be able to read the class
     */
    public static void initialize() throws Exception {
        ElementsApp.LOGGER.trace("Initializing IniModel class");

        String fn = "cba.ini";
        IniModel.ini = new Ini(new File(fn));
        IniModel.prefs = new IniPreferences(IniModel.ini);
    }


    /**
     * This is the workhorse of the class to read a string value.  All other types will be converted
     * from a String.
     */
    private static String getStringValue(String stanza, String option, String dftVal) {
        ElementsApp.LOGGER.debug("Requested to read [" + stanza + "] and option \"" + option + "\"");
        return prefs.node(stanza).get(option, dftVal);
    }


    /**
     * Get the inividual configuration settings for the Elements database
     */
    public static String getElementSchema() { return getStringValue("elements", "schema", "cba_elements"); }
    public static String getElementUser() { return getStringValue("elements", "user", "cba"); }
    public static String getElementPassword() { return getStringValue("elements", "password", "C00lApp$"); }


    /**
     * Get the inividual configuration settings for the System database
     */
    public static String getSystemSchema() { return getStringValue("system", "schema", "cba_system"); }
    public static String getSystemUser() { return getStringValue("system", "user", "cba"); }
    public static String getSystemPassword() { return getStringValue("system", "password", "C00lApp$"); }
}