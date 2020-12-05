
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class CircularGraph {
    private int V;
    private double[][] points;   // points around the circle
    private int radius;
    public double costMatrix[][] = new double[V][V];

    public CircularGraph(int numVertices, int r) {
        V = numVertices;
        radius = r;
        // read in and insert vertices
        points = new double[V][2];
        costMatrix = new double[V][V];
        double angleDegrees = 360 / V;
        double angleRadians = Math.toRadians(angleDegrees);

        for (int i = 0; i < V; i++) {
            double x = radius * Math.cos(angleRadians * i);
            double y = radius * Math.sin(angleRadians * i);
            x = round(x, 2);
            y = round(y, 2);
            points[i][0] = x;
            points[i][1] = y;
        }
        shuffle(points);
        generateCostMatrix();
    }

    private static double round(double value, int places) {
    //code taken from https://www.baeldung.com/java-round-decimal-number
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static void shuffle(double[][] points)
    {
    //taken from https://www.journaldev.com/32661/shuffle-array-java
        Random rand = new Random();
        for (int i = 0; i < points.length; i++) {
            int randomIndexToSwap = rand.nextInt(points.length);
            double temp[] = points[randomIndexToSwap];
            points[randomIndexToSwap] = points[i];
            points[i] = temp;
        }
    }
    private void generateCostMatrix()
    {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < i; j++) {
                double distance = calculateDistance(points[i][0], points[i][1], points[j][0], points[j][1]);
                costMatrix[i][j] = distance;
                costMatrix[j][i] = distance;
            }
        }
    }

    public double calculateDistance(double x1, double y1, double x2, double y2) {
    // taken from https://www.baeldung.com/java-distance-between-two-points
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public void printVertices(){
        for (int i = 0; i < V; i++)
        {
            System.out.println("V " + i + ": " + points[i][0] + ", " + points[i][1]);
        }
    }

    public void printCostMatrix(){
        System.out.format("%8s", "");
        for (int i = 0; i < V; i++) {
            System.out.format("%8d", i);
        }
        System.out.println();
        String dash = "-";
        dash = dash.repeat((V + 2) * 8);
        System.out.println(dash);
        for (int i = 0; i < V; i++) {
            System.out.format("%8d| ", i);
            for (int j = 0; j < V; j++) {
                System.out.format("%8.1f", costMatrix[i][j]);
            }
            System.out.println();
        }
    }
}
