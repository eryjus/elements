
package com.eryjus.cba.controller;

import com.eryjus.cba.model.ElementsModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class ElemViewController {
    @FXML private AnchorPane idRoot;
    @FXML private TextField idElementName;
    @FXML private TextField idElementShortDescription;
    @FXML private ComboBox<String> idElementType;
    @FXML private TextField idElementSize;
    @FXML private CheckBox idElementNotNull;
    @FXML private TextField idElementDefaultValue;
    @FXML private TextField idElementDecimals;
    @FXML private CheckBox idElementZeroFill;
    @FXML private CheckBox idElementAutoIncrement;
    @FXML private CheckBox idElementUnsigned;
    @FXML private TextField idElementCharacterSet;
    @FXML private TextField idElementCollationName;
    @FXML private TextArea idElementBusinessDescription;
    @FXML private Label idSizeLabel;
    @FXML private Label idDecimalsLabel;
    @FXML private Label idZeroLabel;
    @FXML private Label idIncLabel;
    @FXML private Label idCharSetLabel;
    @FXML private Label idUnsignedLabel;
    @FXML private Label idCollationLabel;
    @FXML private Label idDefaultLabel;
    final private PseudoClass ERROR = PseudoClass.getPseudoClass("error");
    final private PseudoClass HIDDEN = PseudoClass.getPseudoClass("hidden");
    private ElementsModel elements = null;
    static Logger LOGGER = null;


    public ElemViewController() throws Exception {
        if (null == LOGGER) LOGGER = LogManager.getLogger();
        elements = new ElementsModel();
        LOGGER.trace("Constructing the ElemViewController");
    }


    @FXML
    protected void initialize() {
        LOGGER.trace("Initializing the ElemViewController");

        // -- set up a listener for the idElementName text field to check for a lost focus event
        idElementName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean hasFocus) {
                if (false == hasFocus) {
                    LOGGER.trace("Checking for ElementName uniqueness");
                    checkUniqueElement();
                }
            }
        });

        // -- populate the possible values for the Element Type
        idElementType.getItems().addAll(
            "BIT", 
            "TINYINT", 
            "SMALLINT", 
            "MEDIUMINT", 
            "INT", 
            "BIGINT", 
            "DECIMAL", 
            "FLOAT", 
            "DOUBLE", 
            "DATE", 
            "DATETIME", 
            "TIMESTAMP",
            "TIME", 
            "YEAR", 
            "CHAR", 
            "VARCHAR", 
            "BINARY", 
            "VARBINARY", 
            "TINYBLOB",
            "TINYTEXT", 
            "BLOB", 
            "TEXT", 
            "MEDIUMBLOB", 
            "MEDIUMTEXT", 
            "LONGBLOB",
            "LONGTEXT"
        );

        // -- set up a listener for the idElementType text field to check for a change in the value
        idElementType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String selection) {
                LOGGER.trace("Updating form based on ElementType");
                changeElementType(selection);
            }
        });

        // -- set up a listener for the idElementSize text field to check for a lost focus event
        idElementSize.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean hasFocus) {
                if (false == hasFocus) {
                    LOGGER.trace("Auditing numeric value for ElementSize");
                    auditNumericValue(idElementSize);
                }
            }
        });

        // -- set up a listener for the idElementDecimals text field to check for a lost focus event
        idElementDecimals.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean hasFocus) {
                if (false == hasFocus) {
                    LOGGER.trace("Auditing numeric value for ElementDecimals");
                    auditNumericValue(idElementDecimals);
                }
            }
        });
    }


    @FXML 
    protected void handleCancelButtonAction(ActionEvent action) {
        LOGGER.trace("Cancel clicked");
        closePane();
    }


    @FXML 
    protected void handleSubmitButtonAction(ActionEvent action) {
        LOGGER.trace("OK clicked");

        elements.setElementName(idElementName.getText());
        elements.setElementDescription(idElementBusinessDescription.getText());
        elements.setElementType(idElementType.getValue());
        elements.setElementSize(idElementSize.getText());
        elements.setElementDecimals(idElementDecimals.getText());
        elements.setElementUnsigned(idElementUnsigned.isSelected());
        elements.setElementZeroFill(idElementZeroFill.isSelected());
        elements.setElementAutoIncrement(idElementAutoIncrement.isSelected());
        elements.setElementCharSet(idElementCharacterSet.getText());
        elements.setElementCollateName(idElementCollationName.getText());
        elements.setElementNotNull(idElementNotNull.isSelected());
        elements.setElementShortDescription(idElementShortDescription.getText());
        elements.setElementDefaultValue(idElementDefaultValue.getText());

        try {
            elements.insertElement();
        } catch (Exception ex) {
            LOGGER.error("Error writing record: ", ex);
        }

        closePane();
    }


    private void checkUniqueElement() {
        idElementName.pseudoClassStateChanged(ERROR, false);

        idElementName.setTooltip(null);
        if (elements.checkExistence(idElementName.getText())) {
            idElementName.pseudoClassStateChanged(ERROR, true);
            LOGGER.warn("Element '" + idElementName.getText() + "' already exists");
        }
    }


    private void changeElementType(String selection) {
        String value = idElementType.getValue();
        idElementType.pseudoClassStateChanged(ERROR, false);

        switch (value) {
        case "BIT":
            setBitType();
            break;

        case "TINYINT":
        case "SMALLINT":
        case "MEDIUMINT":
        case "INT":
        case "BIGINT":
            setIntegerType();
            break;

        case "DECIMAL":
        case "FLOAT":
        case "DOUBLE":
            setDecimalType();
            break;

        case "DATE":
        case "YEAR":
            setDateType();
            break;    

        case "TIME":
        case "DATETIME":
        case "TIMESTAMP":
            setTimeType();
            break;

        case "CHAR":
        case "VARCHAR":
            setCharType();
            break;

        case "BINARY":
        case "VARBINARY":
            setBinType();
            break;

        case "TEXT":
            setTextType();
            break;

        case "BLOB":
            setLobType();
            break;
        
          
        case "TINYTEXT":
        case "MEDIUMTEXT":
        case "LONGTEXT":
            setOtherTextType();
            break;

        case "TINYBLOB":
        case "MEDIUMBLOB":
        case "LONGBLOB":
            setOtherLobType();
            break;
        }
    }


    private void auditNumericValue(TextField field) {
        field.pseudoClassStateChanged(ERROR, false);
        Integer i = Integer.valueOf(field.getText());
        String s = i.toString();

        if (!field.getText().equals(s)) {
            field.pseudoClassStateChanged(ERROR, true);
        }
    }


    private void closePane() {
        AnchorPane pane = (AnchorPane)idRoot.getParent();
        pane.getChildren().clear();
    }


    private void setBitType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setIntegerType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setDecimalType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, false);

        idZeroLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setDateType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementSize.pseudoClassStateChanged(HIDDEN, true);
        idElementSize.setText("");

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setTimeType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setCharType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, false);

        idCollationLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, false);
    }


    private void setBinType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, false);

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setTextType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.setText("");

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, false);

        idCollationLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, false);
    }


    private void setLobType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementSize.pseudoClassStateChanged(HIDDEN, false);

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.setText("");

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }


    private void setOtherTextType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementSize.pseudoClassStateChanged(HIDDEN, true);
        idElementSize.setText("");

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.setText("");

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, false);

        idCollationLabel.pseudoClassStateChanged(HIDDEN, false);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, false);
    }


    private void setOtherLobType() {
        // -- Show and hide elements as needed; clear hidden values
        idSizeLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementSize.pseudoClassStateChanged(HIDDEN, true);
        idElementSize.setText("");

        idDefaultLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.pseudoClassStateChanged(HIDDEN, true);
        idElementDefaultValue.setText("");

        idDecimalsLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.pseudoClassStateChanged(HIDDEN, true);
        idElementDecimals.setText("");

        idZeroLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.pseudoClassStateChanged(HIDDEN, true);
        idElementZeroFill.setSelected(false);

        idIncLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.pseudoClassStateChanged(HIDDEN, true);
        idElementAutoIncrement.setSelected(false);

        idUnsignedLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.pseudoClassStateChanged(HIDDEN, true);
        idElementUnsigned.setSelected(false);

        idCharSetLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.pseudoClassStateChanged(HIDDEN, true);
        idElementCharacterSet.setText("");

        idCollationLabel.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.pseudoClassStateChanged(HIDDEN, true);
        idElementCollationName.setText("");
    }
}