package Objects;

import java.util.ArrayList;
import java.util.List;

public class RadSideForm extends Form {

    private double topRadius;
    private double height;
    private double bottomRadius;
    private double sideRad;
    private double xCoord;
    private double yCoord;
    private double topDiam;
    private int machineType;

    private double xIntersection_0; // topRad & TopLine
    private double yIntersection_0; // topRad & TopLine
    private double xIntersection_1; // topRad & radLine
    private double yIntersection_1; // topRad & radLine
    private double bLineC;
    private double xIntersection_2; // btmRad & radLine
    private double yIntersection_2; // btmRad & radLine
    private double alpha; // угол между найденной прямой проходящей через уентра rTop и rSide и осью Х. Острый угол !!! Внутренний !!!
    private double angleSide; //угол между осью У и прямой идущей из центра окружности боковой стороны до точки пересечения xIntersection_2
    private double shiftPoint; // значение для смещения горизонтальной оси вверх

    private int iterTop = 0;
    private int iterSide = 0;
    private int iterBot = 0;

    private List<Double[][]> pointArrayRight = new ArrayList<>();
    private List<Double[][]> pointArrayMain = new ArrayList<>();
    private List<Double[][]> pointArrayInverse = new ArrayList<>();


    public RadSideForm(double topRadius, double height, double bottomRadius, double sideRad, double xCoord, double yCoord, double topDiam, int machineType) {
        this.topRadius = topRadius;
        this.height = height;
        this.bottomRadius = bottomRadius;
        this.sideRad = sideRad;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.topDiam = topDiam;
        this.machineType = machineType;
    }

    @Override
    public void drawForm(List<Double[][]> pointsArray) {
        super.drawForm(pointsArray);
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

    @Override
    public void exportPoints(List<Double[][]> pointsArray) {
        super.exportPoints(pointsArray);
    }

    private void findIntersectionsPoints(int i) {
//        double b = 5.0/9.0;
//        double a = Math.toDegrees(Math.acos(b));
//        double t = Math.tan(Math.toRadians(2.6));
//        System.out.println(t);

        yIntersection_0 = topDiam / 2;
        double ugol = Math.toDegrees(Math.acos( (topRadius + yIntersection_0 + yCoord) / (sideRad + topRadius)));
        xIntersection_0 = xCoord - Math.sin(Math.toRadians(ugol)) * (sideRad + topRadius);






//        xIntersection_0 = width / 2;
//        yIntersection_0 = topDiam / 2;
//
        alpha = Math.toDegrees(Math.acos( (xCoord - xIntersection_0) / (sideRad + topRadius)));
        System.out.println(alpha + " alpha");
        bLineC = -Math.tan(Math.toRadians(180 - alpha)) * xCoord;
        System.out.println(bLineC + " bLineC");

        double deltaX = Math.cos(Math.toRadians(alpha)) * sideRad;
        double deltaY = Math.sin(Math.toRadians(alpha)) * sideRad;
        xIntersection_1 = xCoord - deltaX;
        yIntersection_1 = deltaY - yCoord;

        //TODO условие нахождения второй точки пересечения стороны и верхнего радиуса/так же это конечная точка профиля по высоте!!!
        yIntersection_2 = yIntersection_0 + height;
        xIntersection_2 = xCoord - Math.sqrt(Math.pow(sideRad, 2) - Math.pow(yIntersection_2 + yCoord, 2));

        System.out.println(xIntersection_0 + " xIntersection_0");
        System.out.println(yIntersection_0 + " yIntersection_0");
        System.out.println(xIntersection_1 + " xIntersection_1");
        System.out.println(yIntersection_1 + " yIntersection_1");
        System.out.println(xIntersection_2 + " xIntersection_2");
        System.out.println(yIntersection_2 + " yIntersection_2");

    }

    private void createPoints() {
        int topRadPointsVal = 20;
        int sidePointsVal = 20;

        double angleTop = Math.toDegrees(Math.asin((xIntersection_1 - xIntersection_0) / topRadius)); //угол между осью У и прямой идущей из центра окружности впадины до точки пересечения с боковой линией
        double angleTopStep = angleTop / topRadPointsVal; //шаг угла для построения очек пересечения на впадине профиля

        Double[][] pointCoordinate = new Double[1][2]; // массив одной точки х,у
        pointCoordinate[0][0] = 0.0;
        pointCoordinate[0][1] = yIntersection_0;

        shiftPoint = yIntersection_0 - topRadius;

        pointArrayRight.add(pointCoordinate); // нулевая точка 0,0
        System.out.println(pointCoordinate[0][0] + "," + pointCoordinate[0][1] + " стартовая точка");

        System.out.println(angleTopStep + " angleTopStep");
        System.out.println(angleTop + " angleTop");

        for (double i = 0; i < angleTop; i = i + angleTopStep) {
            double x = 0;
            double y = 0;

            x = Math.sin(Math.toRadians(i)) * topRadius + xIntersection_0;
            y = topRadius - Math.cos(Math.toRadians(i)) * topRadius + pointCoordinate[0][1];

            Double[][] pointCoordinateTop = new Double[1][2];
            pointCoordinateTop[0][0] = x;
            pointCoordinateTop[0][1] = y;
            pointArrayRight.add(pointCoordinateTop); // точки с вершины профиля
            System.out.println(pointCoordinateTop[0][0] + "," + pointCoordinateTop[0][1] + " точки с впадины профиля");

            iterTop++;

        }

        Double[][] pointCoordinateInterc_1 = new Double[1][2]; // массив одной точки х,у
        pointCoordinateInterc_1[0][0] = xIntersection_1;
        pointCoordinateInterc_1[0][1] = yIntersection_1;
        pointArrayRight.add(pointCoordinateInterc_1); // точки с пересечения впадины и боковой линии

//        double angleSide = Math.toDegrees(Math.acos(xIntersection_2 / sideRad)); //угол между осью У и прямой идущей из центра окружности боковой стороны до точки пересечения xIntersection_2
        angleSide = Math.toDegrees(Math.asin((yIntersection_2 + yCoord) / sideRad)); //угол между осью У и прямой идущей из центра окружности боковой стороны до точки пересечения xIntersection_2
        double angleSideStep = (angleSide - alpha) / sidePointsVal; //шаг угла для построения очек пересечения на боковой стороне профиля

        for (double i = alpha + angleSideStep; i < angleSide; i = i + angleSideStep) {
            double x = 0;
            double y = 0;

            y = Math.sin(Math.toRadians(i)) * sideRad - yCoord;
            x = xCoord - Math.cos(Math.toRadians(i)) * sideRad;

            Double[][] pointCoordinateSide = new Double[1][2];
            pointCoordinateSide[0][0] = x;
            pointCoordinateSide[0][1] = y;
            pointArrayRight.add(pointCoordinateSide); // точки со стороны профиля

            iterSide++;

        }

        Double[][] pointCoordinateInterc_2 = new Double[1][2]; // массив одной точки х,у
        pointCoordinateInterc_2[0][0] = xIntersection_2;
        pointCoordinateInterc_2[0][1] = yIntersection_2;
        pointArrayRight.add(pointCoordinateInterc_2); // конечная точка по высоте, она же начало для скругления

        if (machineType == 1) {

            int botRadPointsVal = 20;

            double angleBottom = angleSide;
            double angleBottomStep = (90 - angleBottom) / botRadPointsVal;
            double xDelta = Math.cos(Math.toRadians(angleBottom)) * bottomRadius; // xIntersection_2 + xDelta -> максимальная точка по Х
            double yDelta = Math.sin(Math.toRadians(angleBottom)) * bottomRadius;
            System.out.println(angleBottom + " anglebottom");
            System.out.println(angleBottomStep + " angleBottomStep");

            for (double a = angleBottom + angleBottomStep; a <= 90; a = a + angleBottomStep) {

                double x = 0;
                double y = 0;

                x = xDelta - Math.cos(Math.toRadians(a)) * bottomRadius + xIntersection_2;
                y = Math.sin(Math.toRadians(a)) * bottomRadius - yDelta + yIntersection_2;

                Double[][] pointCoordinateBottom = new Double[1][2]; // массив одной точки х,у
                pointCoordinateBottom[0][0] = x;
                pointCoordinateBottom[0][1] = y;
                pointArrayRight.add(pointCoordinateBottom);

                iterBot++;

            }

        }

//        if (width != 0.0){
//            for (int i = 0; i < pointArrayRight.size(); i++) {    // сдвиг на половину width
//                Double[][] buf = new Double[1][2];
//                buf[0][0] = pointArrayRight.get(i)[0][0] + width / 2;
//                buf[0][1] = pointArrayRight.get(i)[0][1];
//                pointArrayRight.set(i, buf);
//            }
//
//            Double[][] nullPoint = new Double[1][2];            // создание новой точки 0.0, 0.0
//            nullPoint[0][0] = 0.0;
//            nullPoint[0][1] = shiftPoint;
//            pointArrayRight.set(0, nullPoint);
//        }


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

        movePoints();

        pointArrayInverse = inverseXAxis(pointArrayMain);

        System.out.println(iterTop + " iterTop");
        System.out.println(iterSide + " iterSide");
        System.out.println(iterBot + " iterBot");

    }

    private void getPoints() {
        for (Double[][] aPointArrayMain : pointArrayMain) {
            System.out.println(aPointArrayMain[0][0] + ", " + aPointArrayMain[0][1]);
        }
    }

    private void movePoints() {

        for (Double[][] array : pointArrayMain) {
            double xBuf = array[0][0];
            double yBuf = array[0][1] - shiftPoint;

            array[0][0] = xBuf;
            array[0][1] = yBuf;

        }

    }

}
