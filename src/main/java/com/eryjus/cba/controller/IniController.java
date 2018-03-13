//===================================================================================================================
// IniController.java -- This is the controller that is responsible for the cba.ini values
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


package com.eryjus.cba.controller;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import java.util.prefs.Preferences;
import java.io.File;

import com.eryjus.cba.ElementsApp;


/**
 * The IniControler class is used to interface with the cba.ini configuration file.
 */
public class IniController {
    private static Ini ini;
    private static Preferences prefs;

    /**
     * Initialize the class to be able to read the class
     */
    public static void initialize() throws Exception {
        ElementsApp.LOGGER.trace("Initializing IniController class");

        String fn = "cba.ini";
        IniController.ini = new Ini(new File(fn));
        IniController.prefs = new IniPreferences(IniController.ini);
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