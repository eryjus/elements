//===================================================================================================================
// LoginController.java -- This is the controller that manages the user login
// -----------------------------------------------------------------------------------------------------------------
//
// This class is responsible for obtaining the user ID and password and validating whether it exists in the 
// user database; and throwing an effor if it does not.
//
// -----------------------------------------------------------------------------------------------------------------
//
//    Date     Programmer    Version    Comment
// ----------  ----------  -----------  ----------------------------------------------------------------------------
// 2018-03-03     adcl       v0.1.0     Initial version
//
//===================================================================================================================


package com.eryjus.cba.controller;
 
import com.eryjus.cba.ElementsApp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/***
 * The FXML Login Controller class, responsble for authenticating the user
 * 
 * @author Adam Clark
 */
public class LoginController {
    @FXML private Text actiontarget;
    @FXML private TextField userField;
    @FXML private TextField passwordField;
    
    /**
     * Handle the SignIn button click event, validating the user and password against the database
     * 
     * @param event
     */
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        boolean authenticated = false;
        actiontarget.setText("");
        ElementsApp.LOGGER.info("User " + userField.getText() + " requested to log in");
        
        try {
            if (SecurityController.authenticateUser(userField.getText(), passwordField.getText()) == false) {
                actiontarget.setText("Cannot log in");
            } else {
                authenticated = true;
            }
        } catch (Exception ex) { }

        if (authenticated) {
            ElementsApp.LOGGER.info("User " + userField.getText() + " has been authenticated and can log in.");
        }
    }


    /**
     * Handle the Exit button click event, which will exit the application; does not return
     * 
     * @param event
     */
    @FXML protected void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }
}