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
import com.eryjus.cba.model.SecurityModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/***
 * The FXML Login Controller class, responsble for authenticating the user
 * 
 * @author Adam Clark
 */
public class LoginController {
    @FXML private Text actiontarget;
    @FXML private TextField userField;
    @FXML private TextField passwordField;
    @FXML private Button btnSignIn;
    
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
            if (SecurityModel.authenticateUser(userField.getText(), passwordField.getText()) == false) {
                actiontarget.setText("Cannot log in");
                return;
            } else {
                authenticated = true;
            }
        } catch (Exception ex) { }

        if (authenticated) {
            ElementsApp.LOGGER.info("User " + userField.getText() + " has been authenticated and can log in.");
            Parent root;

            try {
                root = FXMLLoader.load(getClass().getResource("/view/elements.fxml"));
            } catch (Exception ex) {
                actiontarget.setText("Failed to load ELement form");
                ElementsApp.LOGGER.error(ex.getMessage(), ex);
                return;
            }

            Platform.setImplicitExit(false);
            Stage oldStage = (Stage)btnSignIn.getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1200, 800);

            stage.setTitle("cba Elements Maintenance");
            stage.initStyle(StageStyle.DECORATED);
            stage.sizeToScene();
            stage.setScene(scene);

            oldStage.hide();
            stage.show();
            Platform.setImplicitExit(true);
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