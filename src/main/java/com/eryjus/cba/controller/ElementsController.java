
package com.eryjus.cba.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class ElementsController {
    @FXML private AnchorPane idMainAnchor;
    @FXML private Label idOpenApp;
    static Logger LOGGER = null;


    public ElementsController() {
        if (null == LOGGER) LOGGER = LogManager.getLogger();

        LOGGER.trace("Create an ElementsController instance");
    }


    @FXML 
    protected void handleAddElement(ActionEvent event) {
        LOGGER.trace("Opening the pane to add an element");
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        try {
            fxmlLoader.load(getClass().getResource("/view/elemview.fxml").openStream());
        } catch (Exception ex) {
            LOGGER.fatal("Unable to open the AddElement pane: ", ex);
            return;
        }

        AnchorPane acPane = fxmlLoader.getRoot();
        idMainAnchor.getChildren().clear();
        idMainAnchor.getChildren().add(acPane);

        idOpenApp.setText("Add cba Business Data Element");
    }
}