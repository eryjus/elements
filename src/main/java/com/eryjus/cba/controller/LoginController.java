package com.eryjus.cba.controller;
 
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
 

public class LoginController {
    @FXML private Text actiontarget;
    @FXML private TextField userField;
    @FXML private TextField passwordField;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        System.out.println("User Name requested: " + userField.getText());
        System.out.println("Password supplied: " + passwordField.getText());
        actiontarget.setText("Sign in button pressed");
    }

    @FXML protected void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }
}