//===================================================================================================================
// ElementsApp.java -- CBA low-level data elements used to define individual bits of business data
// -----------------------------------------------------------------------------------------------------------------
//
// This class is responsible for interfacing with MySQL to read, write and update the Elements table.  Upon 
// establishing this connection, it will take care of checking for the table's existence and create it id it does
// not exist.
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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ElementsApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(root, 325, 275);
        
        stage.setTitle("cba Elements Login");
        stage.setScene(scene);
        stage.show();
    }
}