package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TrapForm extends Form {

    private double topRadius;
    private double height;
    private double angle;
    private double bottomRadius;
    private double width;
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

    public TrapForm(double topRadius, double height, double angle, double bottomRadius, double width, int machineType) {
        this.topRadius = topRadius;
        this.height = height;
        this.angle = angle;
        this.bottomRadius = bottomRadius;
        this.width = width;
        this.machineType = machineType;
    }


    @Override
    public void drawForm(List<Double[][]> pointsArray) {
        super.drawForm(pointsArray);
    }

    @Override
    public void exportPoints(List<Double[][]> pointsArray) {
        super.exportPoints(pointsArray);
    }

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

//    private void draw() {
//        Stage primaryStage = new Stage();
//        Group root = new Group();
//        primaryStage.setScene(new Scene(root));
//        NumberAxis xAxis = new NumberAxis("Values for X-Axis", pointArrayMain.get(pointArrayMain.size() - 1)[0][0] - 0.1, pointArrayMain.get(0)[0][0] + 0.1, 0.5);
//        NumberAxis yAxis = new NumberAxis("Values for Y-Axis", -0.1, pointArrayMain.get(0)[0][1] + 0.1, 0.5);
//        XYChart.Series series = new XYChart.Series();
//        series.setName("1");
//        for (int i = 0; i < pointArrayMain.size(); i++) {
//            series.getData().add(new XYChart.Data(pointArrayMain.get(i)[0][0], pointArrayMain.get(i)[0][1]));
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

            xIntersection_2 = (height - bLineC) / Math.tan(Math.toRadians(angle));
            yIntersection_2 = height;

            System.out.println(xIntersection_2 + "e");
            System.out.println(yIntersection_2 + "e");

        }

    }

    private void createPoints() {
        int topRadPointsVal = 20;
        int sidePointsVal = 20;

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

            double botRadAccuracy = 20;
            double stepBotRad = (x_O_BottomRadCoord - xIntersection_2) / botRadAccuracy;

            for (double d = xIntersection_2 + stepBotRad; d < x_O_BottomRadCoord; d = d + stepBotRad) {
                Double[][] pointCoordinateInterc_BotRad = new Double[1][2];
                double xDelta = x_O_BottomRadCoord - d; // расчетная координата по Х без учета смещения окружности от 0.0 очи координат -> расчитываем У для простой формулы Х2 + У2 = Р2
                double yDelta = Math.sqrt(Math.pow(bottomRadius,2) - Math.pow(xDelta,2));
                double yResult = y_O_BottomRadCoord + yDelta;  // фактическая координата Y с учетом смещения от начала оси координат
                double xResult = d; // фактическая координата Х с учетом смещения от начала оси координат
                pointCoordinateInterc_BotRad[0][0] = xResult;
                pointCoordinateInterc_BotRad[0][1] = yResult;
                pointArrayRight.add(pointCoordinateInterc_BotRad);
            }

//            double y = Math.sqrt(Math.pow(bottomRadius,2) - Math.pow(xBotBuf/2,2));// нахождение средней точки между первой точкой верхнего радиуса и крайней
//            double yResult = y_O_BottomRadCoord + y;
//            double xResult = x_O_BottomRadCoord - xBotBuf/2;
//
//            Double[][] pointCoordinateInterc_BotRad = new Double[1][2]; // массив одной точки х,у
//            pointCoordinateInterc_BotRad[0][0] = xResult;
//            pointCoordinateInterc_BotRad[0][1] = yResult;
//            pointArrayRight.add(pointCoordinateInterc_BotRad); // добавлениее в массив средней точки между первой точкой верхнего радиуса и крайней

//            Double[][] pointCoordinateInterc_BotRadEnd = new Double[1][2]; // массив одной точки х,у
//            pointCoordinateInterc_BotRadEnd[0][0] = x_O_BottomRadCoord;
//            pointCoordinateInterc_BotRadEnd[0][1] = y_O_BottomRadCoord + bottomRadius;
//            pointArrayRight.add(pointCoordinateInterc_BotRadEnd); // добавлениее в массив крайней точки верхнего радиуса

        }

        for (int i = 0; i < pointArrayRight.size(); i++) {    // сдвиг на половину width
            Double[][] buf = new Double[1][2];
            buf[0][0] = pointArrayRight.get(i)[0][0] + width / 2;
            buf[0][1] = pointArrayRight.get(i)[0][1];
            pointArrayRight.set(i, buf);
        }

        Double[][] nullPoint = new Double[1][2];            // создание новой точки 0.0, 0.0
        nullPoint[0][0] = 0.0;
        nullPoint[0][1] = 0.0;
        pointArrayRight.set(0, nullPoint);

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
