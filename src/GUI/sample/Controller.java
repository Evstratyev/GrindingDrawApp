package GUI.sample;

import Objects.RadSideForm;
import Objects.TrapForm;
import Objects.TriangleForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    @FXML
    Button Trian, Trap, Rad;
    @FXML
    CheckBox Doi1000_275, Rent;
    @FXML
    TextField angleText, heightText, topRadiusText, bottomRadiusText, widthText, xText, yText, topDiamText, sideRadText;


    @FXML
    private void clickTrian(ActionEvent event) {
        newScene("TriangDrMenu.fxml");
    }

    @FXML
    private void clickTrap(ActionEvent event) {
        newScene("TrapDrMenu.fxml");
    }

    @FXML
    private void clickRad(ActionEvent event) {
        newScene("RadDrMenu.fxml");
    }

    @FXML
    private void clickDrawTrian() {
        int macType = 0;
        if (Doi1000_275.isSelected()) {
            macType = 1;
        }

        try {
            TriangleForm triangleForm = new TriangleForm(
                    Double.parseDouble(topRadiusText.getText()),
                    Double.parseDouble(heightText.getText()),
                    Double.parseDouble(angleText.getText()),
                    Double.parseDouble(bottomRadiusText.getText()),
                    macType
            );
            if (checkMachineChoice()) {
                triangleForm.create();
            } else {
                showAlertWithoutHeaderText("Choose machine type!");
            }
        } catch (NumberFormatException e) {
            showAlertWithoutHeaderText("Error input data!");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void clickDrawTrap() {
        int macType = 0;
        if (Doi1000_275.isSelected()) {
            macType = 1;
        }

        try {
            TrapForm trapForm = new TrapForm(
                    Double.parseDouble(topRadiusText.getText()),
                    Double.parseDouble(heightText.getText()),
                    Double.parseDouble(angleText.getText()),
                    Double.parseDouble(bottomRadiusText.getText()),
                    Double.parseDouble(widthText.getText()),
                    macType
            );
            if (checkMachineChoice()) {
                trapForm.create();
            } else {
                showAlertWithoutHeaderText("Choose machine type!");
            }
        } catch (NumberFormatException e) {
            showAlertWithoutHeaderText("Error input data!");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void clickDrawRad() {
        int macType = 0;
        if (Doi1000_275.isSelected()) {
            macType = 1;
        }

        try {
//            RadSideForm radSideForm = new RadSideForm(
//                    Double.parseDouble(topRadiusText.getText()),
//                    Double.parseDouble(heightText.getText()),
//                    Double.parseDouble(bottomRadiusText.getText()),
//                    Double.parseDouble(widthText.getText()),
//                    Double.parseDouble(sideRadText.getText()),
//                    Double.parseDouble(xText.getText()),
//                    macType
//            );
            RadSideForm radSideForm = new RadSideForm(
                    Double.parseDouble(topRadiusText.getText()),
                    Double.parseDouble(heightText.getText()),
                    Double.parseDouble(bottomRadiusText.getText()),
                    Double.parseDouble(sideRadText.getText()),
                    Double.parseDouble(xText.getText()),
                    Double.parseDouble(yText.getText()),
                    Double.parseDouble(topDiamText.getText()),
                    macType
            );
            if (checkMachineChoice()) {

                radSideForm.create();
//                showAlertWithoutHeaderText("Coming soon");

            } else {
                showAlertWithoutHeaderText("Choose machine type!");
            }
        } catch (NumberFormatException e) {
            showAlertWithoutHeaderText("Error input data!");
            System.out.println(e.getMessage());
        }
    }

    private void newScene(String fxml) {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            primaryStage.setTitle("Grinding schedule");
            Scene scene = new Scene(root, 645, 240);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {

        }
    }

    private boolean checkMachineChoice() {
        boolean value = true;
        if (Doi1000_275.isSelected() & Rent.isSelected() ||
                !Doi1000_275.isSelected() & !Rent.isSelected()) {
            value = false;
        }
        return value;
    }

    private void showAlertWithoutHeaderText(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Check input values");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }


}
