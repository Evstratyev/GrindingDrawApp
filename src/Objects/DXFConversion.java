package Objects;

import javafx.scene.control.Alert;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class DXFConversion {
    private int points;
    private double maxY;
    private double leftX;
    private double rightX;
    private List<Double[][]> pointsArray;
    private String file = "profile.dxf";

    public DXFConversion(int points, double maxY, double rightX, double leftX, List<Double[][]> pointsArray) {
        this.points = points;
        this.maxY = maxY;
        this.leftX = leftX;
        this.rightX = rightX;
        this.pointsArray = pointsArray;
    }

    public void createDxf() {

        checkFileExist();

        loadPart(1);
        writeString(Double.toString(leftX));
        loadPart(2);
        writeString(Double.toString(rightX));
        writeString("20");
        writeString(Double.toString(maxY));
        loadPart(3);
        writeString(Integer.toString(points));
        loadPart(4);

        writeArray(pointsArray);

        loadPart(5);
    }


    private void loadPart(int i) {


        try {
            InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/shablon/" +i + ".txt"), "UTF-8");

//            FileReader reader = new FileReader(f.getAbsolutePath());
//            FileReader reader = new FileReader(pathPath);
            FileWriter writer = new FileWriter(file, true);
            // читаем посимвольно
            int c;
            while ((c = reader.read()) != -1) {

                writer.write(c);
                writer.flush();
                System.out.print((char) c);
            }
            writer.write("\r\n");
            writer.flush();
            reader.close();
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void writeString(String s) {
        try {
            FileWriter writer = new FileWriter(file, true);
            // читаем посимвольно

            writer.write(s);
            writer.flush();

            writer.write("\r\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeArray(List<Double[][]> array) {

        try {
            FileWriter writer = new FileWriter(file, true);

            for (Double[][] arr : array) {

                writer.write("10");
                writer.flush();

                writer.write("\r\n");
                writer.flush();

                writer.write(Double.toString(arr[0][0]));
                writer.flush();

                writer.write("\r\n");
                writer.flush();

                writer.write("20");
                writer.flush();

                writer.write("\r\n");
                writer.flush();

                writer.write(Double.toString(arr[0][1]));
                writer.flush();

                writer.write("\r\n");
                writer.flush();

            }

            writer.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void checkFileExist (){
        int count = 0;
        while (true){
            File fileE = new File(file);
            if (fileE.exists()){
                file = "profile_" +count +".dxf";
                count++;
            } else break;
        }
    }

    private void showAlertWithoutHeaderText(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("!!!");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
