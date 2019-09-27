package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TriangleForm extends Form {

    private double topRadius;
    private double height;
    private double angle;
    private double bottomRadius;
    private int machineType;

    private double xIntersection_1; // topRad & Line
    private double yIntersection_1; // topRad & Line
    private double bLineC;
    private double xIntersection_2; // Line & outRad
    private double yIntersection_2; // Line & outRad
    private double x_O_BottomRadCoord; // bottom rad coordinate
    private double y_O_BottomRadCoord; // bottom rad coordinate

    private List<Double[][]> pointArrayRight = new ArrayList<>();
    private List<Double[][]> pointArrayMain = new ArrayList<>();
    private List<Double[][]> pointArrayInverse = new ArrayList<>();

    public TriangleForm(double topRadius, double height, double angle, double bottomRadius, int machineType) {
        this.topRadius = topRadius;
        this.height = height;
        this.angle = angle;
        this.bottomRadius = bottomRadius;
        this.machineType = machineType;
    }


//    @Override
//    void drawForm() {
//        draw(pointArrayMain);
//    }

    @Override
    public void drawForm(List<Double[][]> pointsArray) {
        super.drawForm(pointsArray);
    }


    @Override
    public void exportPoints(List<Double[][]> pointsArray) {
        super.exportPoints(pointsArray);
    }

    //    @Override
//    void exportDataPoints(List<Double[][]> pointsArray) {
//        {
//            try(FileWriter writer = new FileWriter("points.txt", false))
//            {
//                // запись всей строки
//                for (int i = 0; i < pointsArray.size(); i++) {
//                    String text = pointsArray.get(i)[0][0] + ", " +pointsArray.get(i)[0][1];
//                    writer.write(text);
//                    writer.flush();
//                }
//            }
//            catch(IOException ex){
//                System.out.println(ex.getMessage());
//            }
//        }
//    }

    @Override
    public void create() {
        findIntersectionsPoints(machineType);
        createPoints();
        getPoints();
        drawForm(pointArrayInverse);
        exportPoints(pointArrayInverse);
        exportDXF(pointArrayInverse.size(),
                pointArrayInverse.get(0)[0][1],
                pointArrayInverse.get(0)[0][0],
                pointArrayInverse.get(pointArrayInverse.size() - 1)[0][1],
                pointArrayInverse
        );
    }

//    private void draw(List<Double[][]> mainArray) {
//
//        Stage primaryStage = new Stage();
//        Group root = new Group();
//        primaryStage.setScene(new Scene(root));
//        NumberAxis xAxis = new NumberAxis("Values for X-Axis", mainArray.get(mainArray.size()-1)[0][0] - 0.1, mainArray.get(0)[0][0] + 0.1, 0.5);
//        NumberAxis yAxis = new NumberAxis("Values for Y-Axis", -0.1, mainArray.get(0)[0][1] + 0.1, 0.5);
//        XYChart.Series series = new XYChart.Series();
//        series.setName("1");
//        for (int i = 0; i < mainArray.size(); i++) {
//            series.getData().add(new XYChart.Data(mainArray.get(i)[0][0], mainArray.get(i)[0][1]));
//        }
//        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList(series);
//        XYChart chart = new LineChart(xAxis, yAxis, lineChartData);
//        root.getChildren().add(chart);
//        primaryStage.show();
//    }

    public void findIntersectionsPoints(int i) {

        //R2 = X2 + Y2
        //Y = KX
        xIntersection_1 = Math.sqrt(Math.pow(topRadius, 2) / (1 + Math.pow((Math.tan(Math.toRadians(90 + angle))), 2))); //перепендикуляр к прямой проходящей через центр
        yIntersection_1 = Math.tan(Math.toRadians(90 + angle)) * xIntersection_1;
        yIntersection_1 = yIntersection_1 + topRadius; //перенос СК на нижнюю точку впадины

        System.out.println(xIntersection_1);
        System.out.println(yIntersection_1);

        bLineC = yIntersection_1 - Math.tan(Math.toRadians(angle)) * xIntersection_1;

        System.out.println(bLineC + " bLineC");

        if (i != 0) {

//            yIntersection_2 = Math.sqrt(Math.pow(bottomRadius,2) - Math.pow(((height - bLineC)/Math.tan(Math.toRadians(angle))),2));
//            xIntersection_2 = Math.sqrt(Math.pow(bottomRadius,2) - Math.pow(yIntersection_2,2));
            xIntersection_2 = (height - bLineC) / Math.tan(Math.toRadians(angle));
            yIntersection_2 = height;

            System.out.println(xIntersection_2 + "e");
            System.out.println(yIntersection_2 + "e");

        }


    }

    private void createPoints() {
        int topRadPointsVal = 20;
        int sidePointsVal = 20;
//        double xBotRad = 0;
//        double yBotRad = 0;

        double angleTop = Math.toDegrees(Math.asin(xIntersection_1 / topRadius)); //угол между осью У и прямой идущей из центра окружности впадины до точки пересечения с боковой линией
        double angleTopStep = angleTop / topRadPointsVal; //шаг угла для построения очек пересечения на впадине профиля

//        System.out.println(angleTop);
//        System.out.println(angleTopStep);

        Double[][] pointCoordinate = new Double[1][2]; // массив одной точки х,у

        pointCoordinate[0][0] = 0.0;
        pointCoordinate[0][1] = 0.0;
        pointArrayRight.add(pointCoordinate); // нулевая точка 0,0

        for (double i = angleTopStep; i < angleTop; i = i + angleTopStep) {
            double x = 0;
            double y = 0;

            x = Math.sin(Math.toRadians(i)) * topRadius;
//            y = topRadius - Math.sqrt(Math.pow(topRadius, 2) - Math.pow(x, 2)); // инверсия точек по у , иначе выгибается в обратную сторону
            y = topRadius - Math.cos(Math.toRadians(i)) * topRadius;

            Double[][] pointCoordinateTop = new Double[1][2];
            pointCoordinateTop[0][0] = x;
            pointCoordinateTop[0][1] = y;
            pointArrayRight.add(pointCoordinateTop); // точки с вершины профиля

        }

        Double[][] pointCoordinateInterc_1 = new Double[1][2]; // массив одной точки х,у
        pointCoordinateInterc_1[0][0] = xIntersection_1;
        pointCoordinateInterc_1[0][1] = yIntersection_1;
        pointArrayRight.add(pointCoordinateInterc_1); // точки с пересечения впадины и боковой линии

        double sideStep = height / sidePointsVal; //шаг угла для построения очек на боковой поверхности
        for (double i = xIntersection_1; i < height; i = i + sideStep) {
            double x = 0;
            double y = 0;

            y = i;
            x = (y - bLineC) / Math.tan(Math.toRadians(angle));

            Double[][] pointCoordinateSideLine = new Double[1][2]; // массив одной точки х,у
            pointCoordinateSideLine[0][0] = x;
            pointCoordinateSideLine[0][1] = y;
            pointArrayRight.add(pointCoordinateSideLine); // точки с образующей профиля

        }


        if (machineType != 0) {

            Double[][] pointCoordinateInterc_2 = new Double[1][2]; // массив одной точки х,у
            pointCoordinateInterc_2[0][0] = xIntersection_2;
            pointCoordinateInterc_2[0][1] = yIntersection_2;
            pointArrayRight.add(pointCoordinateInterc_2); // добавлениее в массив точки пересечения с верхним радиусом

            double angleBottomRad = 180 - 90 - angle; //угол треугольника для получения координат Х0 и У0
            double xBotBuf = Math.cos(Math.toRadians(angleBottomRad)) * bottomRadius;
            x_O_BottomRadCoord = xIntersection_2 + xBotBuf;
            double yBotBuf = Math.sin(Math.toRadians(angleBottomRad)) * bottomRadius;
            y_O_BottomRadCoord = yIntersection_2 - yBotBuf;

//            System.out.println(angleBottomRad + " angleBottomRad");
            System.out.println(xBotBuf + " xBotBuf");
            System.out.println(yBotBuf + " yBotBuf");
            System.out.println(x_O_BottomRadCoord + " X_O");
            System.out.println(y_O_BottomRadCoord + " y_O");


            /*
            // Общий вид уравнения окружности (Х-x_O_BottomRadCoord)^2 + (Y - y_O_BottomRadCoord)^2 = Rbot^2

            //нахождение центральной точки на дуге , координата по Х -> (-xBotBuf/2 -x_O_BottomRadCoord)^2 -> xResult
            //(Y - y_O_BottomRadCoord)^2 = Rbot^2 - xResult
            // B = 2*y_O_BottomRadCoord
            //Y^2 - B*Y + y_O_BottomRadCoord^2 = Rbot^2 - xResult
            // C = y_O_BottomRadCoord^2 - Rbot^2 + xResult
            //Y^2 - B*Y + C = 0
            //D = (B)^2 - 4 * 1 * C
            //Y = (-B - sqrt(D))/2

//            double xResult = Math.pow((-xBotBuf/2 - x_O_BottomRadCoord),2);
//            double b = 2 * y_O_BottomRadCoord;
//            double c = Math.pow(y_O_BottomRadCoord,2) - Math.pow(bottomRadius,2) + xResult;
//            double dis = Math.pow(b,2) - 4 * 1 * c;
//            double yResult = (-b - Math.sqrt(dis)) / 2;
//
//            System.out.println(xResult);
//            System.out.println(dis);
//            System.out.println(yResult);
            //X^2 + Y^2 = R^2
            //Y = sqrt(R^2 - X^2)
            //Y = sqrt(R^2 - xBotBuf/2^2)
            //yResult = y_O_BottomRadCoord + Y
            //xResult = x_O_BottomRadCoord - xBotBuf/2^2
            */


            /*
            double y = Math.sqrt(Math.pow(bottomRadius,2) - Math.pow(xBotBuf/2,2));
            double yResult = y_O_BottomRadCoord + y;
            double xResult = x_O_BottomRadCoord - xBotBuf/2;
//            System.out.println(xResult);
//            System.out.println(yResult);

//            Double[][] pointCoordinateInterc_BotRad = new Double[1][2]; // массив одной точки х,у
//            pointCoordinateInterc_BotRad[0][0] = xResult;
//            pointCoordinateInterc_BotRad[0][1] = yResult;
//            pointArrayRight.add(pointCoordinateInterc_BotRad); // добавлениее в массив средней точки между первой точкой верхнего радиуса и крайней
//
//            Double[][] pointCoordinateInterc_BotRadEnd = new Double[1][2]; // массив одной точки х,у
//            pointCoordinateInterc_BotRadEnd[0][0] = x_O_BottomRadCoord;
//            pointCoordinateInterc_BotRadEnd[0][1] = y_O_BottomRadCoord + bottomRadius;
//            pointArrayRight.add(pointCoordinateInterc_BotRadEnd); // добавлениее в массив крайней точки верхнего радиуса

            */

            double botRadAccuracy = 20;
            double stepBotRad = (x_O_BottomRadCoord - xIntersection_2) / botRadAccuracy;

            for (double d = xIntersection_2 + stepBotRad; d < x_O_BottomRadCoord; d = d + stepBotRad) {
                Double[][] pointCoordinateInterc_BotRad = new Double[1][2];
                double xDelta = x_O_BottomRadCoord - d; // расчетная координата по Х без учета смещения окружности от 0.0 очи координат -> расчитываем У для простой формулы Х2 + У2 = Р2
                double yDelta = Math.sqrt(Math.pow(bottomRadius, 2) - Math.pow(xDelta, 2));
                double yResult = y_O_BottomRadCoord + yDelta;  // фактическая координата Y с учетом смещения от начала оси координат
                double xResult = d; // фактическая координата Х с учетом смещения от начала оси координат
                pointCoordinateInterc_BotRad[0][0] = xResult;
                pointCoordinateInterc_BotRad[0][1] = yResult;
                pointArrayRight.add(pointCoordinateInterc_BotRad);
            }

        }


        for (int i = pointArrayRight.size() - 1; i > -pointArrayRight.size(); i--) {
            double x = 0;
            double y = 0;

            if (i >= 0) {
                x = pointArrayRight.get(i)[0][0];
                y = pointArrayRight.get(i)[0][1];

                Double[][] bufArray = new Double[1][2]; // массив одной точки х,у
                bufArray[0][0] = x;
                bufArray[0][1] = y;

                pointArrayMain.add(bufArray); // точки с образующей профиля
            } else {

                x = pointArrayRight.get(-i)[0][0];
                y = pointArrayRight.get(-i)[0][1];

                Double[][] bufArray = new Double[1][2]; // массив одной точки х,у
                bufArray[0][0] = -x;
                bufArray[0][1] = y;

                pointArrayMain.add(bufArray); // точки с образующей профиля
            }

        }


        pointArrayInverse = inverseXAxis(pointArrayMain);

    }

    private void getPoints() {
        for (Double[][] aPointArrayMain : pointArrayMain) {
            System.out.println(aPointArrayMain[0][0] + ", " + aPointArrayMain[0][1]);
        }
    }


}
