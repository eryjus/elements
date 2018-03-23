//===================================================================================================================
// ElementsApp.java -- CBA low-level data elements used to define individual bits of business data
// -----------------------------------------------------------------------------------------------------------------
//
// This class is the application responsible for interfacing with MySQL to read, write and update the Elements 
// table.  Upon establishing this connection, it will take care of checking for the table's existence and create it 
// if it does not exist.
//
// NOTE: this implementation will be table specific since it will be the first tool that can be used to 
//       build the cba application.  I fully expect that later in the process, a more generic implementation will
//       be used to read tables that will be based on the data that is in this table (and others).
//
// -----------------------------------------------------------------------------------------------------------------
//
//    Date     Programmer    Version    Comment
// ----------  ----------  -----------  ----------------------------------------------------------------------------
// 2018-03-03     adcl       v0.1.0     Initial version
//
//===================================================================================================================


package com.eryjus.cba;

import com.eryjus.cba.model.IniModel;
import com.eryjus.cba.model.SecurityModel;
import com.eryjus.cba.model.ElementsModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


//-------------------------------------------------------------------------------------------------------------------
// class ElementsApp -- this is the top level application and class for maintaining the elements application.  It 
//                      contains the main entry point for the application.
//-------------------------------------------------------------------------------------------------------------------
public class ElementsApp extends Application {
    public static Logger LOGGER = null;
    private static String user;


    //---------------------------------------------------------------------------------------------------------------
    // main() -- this is the main entry point for the application.
    //---------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) throws Exception {
        LOGGER = LogManager.getLogger();

        LOGGER.info("Welcome to 'cba Elements Maintenance'; (c) 2018 Adam Clark");
        LOGGER.info("   Licensed under the BEER-WARE License; revision 42");

        // -- create temporary instances to force initialization
        new IniModel();
        new SecurityModel();
        new ElementsModel();

        launch(args);
    }


    //---------------------------------------------------------------------------------------------------------------
    // start() -- this method is required my JavaFX to be overridden and is called when the application starts.  It
    //            handles all the initialization for the application and presents the Login screen for the users.
    //---------------------------------------------------------------------------------------------------------------
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(root, 375, 234);
        
        stage.setTitle("cba Elements Login");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.sizeToScene();
        stage.show();
    }


    //---------------------------------------------------------------------------------------------------------------
    // user access methods
    //---------------------------------------------------------------------------------------------------------------
    public static String getUser() { return user; }
    public static void setUser(String u) { user = u; }
}