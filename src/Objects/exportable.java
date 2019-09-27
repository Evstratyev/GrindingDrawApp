package Objects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface exportable {
    void exportPoints(List<Double[][]> pointsArray);
    void exportDXF(int points, double maxY, double leftX, double rightX, List<Double[][]> pointsArray);
}
