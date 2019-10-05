package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Form implements exportable {
    private double mouseClickX;
    private double mouseClickY;

    public abstract void create();

    public void drawForm(List<Double[][]> pointsArray) {
        {
            Stage primaryStage = new Stage();
            Group root = new Group();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
//            NumberAxis xAxis = new NumberAxis("Values for X-Axis", pointsArray.get(pointsArray.size()-1)[0][0] - 0.1, pointsArray.get(0)[0][0] + 0.1, 0.5);
//            NumberAxis yAxis = new NumberAxis("Values for Y-Axis", +0.1, pointsArray.get(0)[0][1] - 0.1, 0.5);

            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setAutoRanging(true);
            yAxis.setAutoRanging(true);

            XYChart.Series series = new XYChart.Series();
            series.setName("1");
            for (int i = 0; i < pointsArray.size(); i++) {
                series.getData().add(new XYChart.Data(pointsArray.get(i)[0][0], pointsArray.get(i)[0][1]));
            }
            ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList(series);
            XYChart chart = new LineChart(xAxis, yAxis, lineChartData);
            root.getChildren().add(chart);
            primaryStage.show();

            System.out.println(pointsArray.size());

            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("mouse click detected! " + event.getSceneX() + ", " + event.getSceneY());
                    mouseClickX = event.getSceneX();
                    mouseClickY = event.getSceneY();
                }
            });
            scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    System.out.println("setOnMouseReleased! " + event.getSceneX() + ", " + event.getSceneY());

                    if (mouseClickX > event.getSceneX()) {
                        chart.setLayoutX(chart.getLayoutX() - (mouseClickX - event.getSceneX()));
                    } else {
                        chart.setLayoutX(chart.getLayoutX() + (-mouseClickX + event.getSceneX()));
                    }
                    if (mouseClickY > event.getSceneY()) {
                        chart.setLayoutY(chart.getLayoutY() - (mouseClickY - event.getSceneY()));
                    } else {
                        chart.setLayoutY(chart.getLayoutY() + (-mouseClickY + event.getSceneY()));
                    }


                }
            });

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    System.out.println(event.toString());
                    if (event.getCode().toString().equals("EQUALS")) {
                        chart.setScaleX(chart.getScaleX() + 0.1);
                        chart.setScaleY(chart.getScaleY() + 0.1);
                    }
                    if (event.getCode().toString().equals("MINUS")) {
                        chart.setScaleX(chart.getScaleX() - 0.1);
                        chart.setScaleY(chart.getScaleY() - 0.1);
                    }
                    if (event.getCode().toString().equals("R")) {
                        chart.setLayoutX(0.0);
                        chart.setLayoutY(0.0);
                        chart.setScaleX(1.0);
                        chart.setScaleY(1.0);
                    }
                }
            });

            scene.setOnScroll(new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent event) {
                    System.out.println("scrol " + event.toString());
                    if (event.getDeltaY() > 0){
                        chart.setScaleX(chart.getScaleX() + 0.5);
                        chart.setScaleY(chart.getScaleY() + 0.5);
                    }else{
                        chart.setScaleX(chart.getScaleX() - 0.5);
                        chart.setScaleY(chart.getScaleY() - 0.5);
                    }
                }
            });
        }
    }

    @Override
    public void exportDXF(int points, double maxY, double leftX, double rightX, List<Double[][]> pointsArray) {
        DXFConversion dxfConversion = new DXFConversion(points, maxY, leftX, rightX,  pointsArray);
        dxfConversion.createDxf();
    }

    @Override
    public void exportPoints(List<Double[][]> pointsArray) {
        try {
            File filePath = new File("points.txt");
            System.out.println(filePath.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
            for (int i = 0; i < pointsArray.size(); i++) {
                String text = pointsArray.get(i)[0][0] + "," +pointsArray.get(i)[0][1];
                writer.write(text);
                writer.newLine();
            }
            writer.close();
            showAlertWithoutHeaderText("Points saved" +"\n\n" + filePath.getAbsolutePath());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        try {
            File filePath = new File("points_pline.scr");
            System.out.println(filePath.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
            writer.write("_pline");
            writer.newLine();
            for (int i = 0; i < pointsArray.size(); i++) {
                String text = pointsArray.get(i)[0][0] + "," +pointsArray.get(i)[0][1];
                writer.write(text);
                writer.newLine();
            }
            writer.close();
            showAlertWithoutHeaderText("Points saved" +"\n\n" + filePath.getAbsolutePath());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        try {
            File filePath = new File("points_line.scr");
            System.out.println(filePath.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
            writer.write("_line");
            writer.newLine();
            for (int i = 0; i < pointsArray.size(); i++) {
                String text = pointsArray.get(i)[0][0] + "," +pointsArray.get(i)[0][1];
                writer.write(text);
                writer.newLine();
            }
            writer.close();
            showAlertWithoutHeaderText("Points saved" +"\n\n" + filePath.getAbsolutePath());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void showAlertWithoutHeaderText(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public List<Double[][]> inverseXAxis (List<Double[][]> pointsArray){
        List<Double[][]> inversionArray = new ArrayList<>();

        for (Double[][] arr : pointsArray) {
            Double[][] buf = new Double[1][2];
            buf[0][0] = arr[0][0];
            buf[0][1] = - arr[0][1];
            inversionArray.add(buf);
        }

        return inversionArray;
    }
}
