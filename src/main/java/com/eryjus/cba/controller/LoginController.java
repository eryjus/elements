//===================================================================================================================
// LoginController.java -- This is the controller that manages the user login
// -----------------------------------------------------------------------------------------------------------------
//
// This class is responsible for obtaining the user ID and password and validating whether it exists in the 
// user database; and throwing an error if it does not.
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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


//-------------------------------------------------------------------------------------------------------------------
// LoginController class -- this class is responsible for orchestrating the interactions between the view and the 
//                          controller on the Login form.  The view calls methods in this form and these methods 
//                          will hand requests off to the controller to service.  Several of the attributes of this
//                          class actually belong to the view and therefore are decorated with the @FXML tag.
//-------------------------------------------------------------------------------------------------------------------
public class LoginController {
    @FXML private Text actionTarget;
    @FXML private TextField userField;
    @FXML private TextField passwordField;
    @FXML private Button btnSignIn;
    SecurityModel securityModel;
    static Logger LOGGER = null;


    //---------------------------------------------------------------------------------------------------------------
    // LoginController constructor -- the constructor initializes the attributes that are used internally.  Notice
    //                                that the static attribute LOGGER is checked before re-initializing.
    //---------------------------------------------------------------------------------------------------------------
    public LoginController() throws Exception {
        if (null == LOGGER) LOGGER = LogManager.getLogger();
        securityModel = new SecurityModel();
    }

    
    //---------------------------------------------------------------------------------------------------------------
    // handleSubmitButtonAction() -- this method is called with the submit button is clicked.  The submit button is
    //                               the button called Sign In.
    //---------------------------------------------------------------------------------------------------------------
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        boolean authenticated = false;
        actionTarget.setText("");
        ElementsApp.LOGGER.info("User " + userField.getText() + " requested to log in");
        
        // -- go and authenticate the user
        try {
            if (securityModel.authenticateUser(userField.getText(), passwordField.getText()) == false) {
                actionTarget.setText("Cannot log in");
                return;
            } else {
                authenticated = true;
            }
        } catch (Exception ex) { }

        // -- are we allowed to log in?  If so, present the main screen.
        if (authenticated) {
            ElementsApp.LOGGER.info("User " + userField.getText() + " has been authenticated and can log in.");
            Parent root;

            try {
                root = FXMLLoader.load(getClass().getResource("/view/elements.fxml"));
            } catch (Exception ex) {
                actionTarget.setText("Failed to load Element form");
                ElementsApp.LOGGER.error(ex.getMessage(), ex);
                return;
            }

            // -- prepare to close down the login frame
            Platform.setImplicitExit(false);
            Stage oldStage = (Stage)btnSignIn.getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1200, 800);

            // -- prepare to load up the main frame
            stage.setTitle("cba Elements Maintenance");
            stage.initStyle(StageStyle.DECORATED);
            stage.sizeToScene();
            stage.setScene(scene);

            // -- perform the swap and prepare to exit the app when the main frame is closed
            oldStage.hide();
            stage.show();
            Platform.setImplicitExit(true);
        }
    }


    //---------------------------------------------------------------------------------------------------------------
    // handleExitButtonAction() -- when the exit button is clicked, exit the application appropriate to the platform
    //                             on which we are running.
    //---------------------------------------------------------------------------------------------------------------
    @FXML protected void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }
}