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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.eryjus.cba.controller.IniController;
import com.eryjus.cba.controller.SecurityController;


/**
 * This is the main application class, which will orchestrate the UI
 * 
 * @author Adam Clark
 */
public class ElementsApp extends Application {
    public static Logger LOGGER = LogManager.getLogger(ElementsApp.class);

    /**
     * The main application entry point
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info("Welcome to 'cba Elements Maintenance'; (c) 2018 Adam Clark");
        LOGGER.info("   Licensed under the BEER-WARE License; revision 42");
        IniController.initialize();
        SecurityController.initialize();
        launch(args);
    }


    /**
     * start the application UI.  This means displaying the login screen and authenticating the user.
     * 
     * @param stage
     */
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
}