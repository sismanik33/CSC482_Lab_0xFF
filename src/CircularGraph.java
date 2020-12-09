
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class CircularGraph {
    private int V;
    public double[][] optimalPath;   // points around the circle before shuffling
    private double[][] points;   // points around the circle
    private int radius;
    public double costMatrix[][] = new double[V][V];

    public CircularGraph(int numVertices, int r) {
        V = numVertices;
        radius = r;
        // read in and insert vertices
        points = new double[V][2];
        costMatrix = new double[V][V];
        generatePointsOnCircle();
        optimalPath = Arrays.copyOf(points, points.length); //keep a copy of unshuffled circle.
        shuffle(points);

        generateCostMatrix();
//        printCostMatrix();
    }

    private void generatePointsOnCircle()
    {
        double angleDegrees = 360.0 / (double)V;
        double angleRadians = Math.toRadians(angleDegrees);
        for (int i = 0; i < V; i++) {
            double x = radius * Math.cos(angleRadians * i);
            double y = radius * Math.sin(angleRadians * i);
            points[i][0] = x;
            points[i][1] = y;
        }
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
            System.out.format("V%d: (%4.2f, %4.2f)\n", i, points[i][0], points[i][1]);
        }
    }

    public void printOptimalPath()
    {
        int startVertex = 0;
        System.out.println("Shortest path on un-shuffled nodes: ");
        for (int i = 0; i < optimalPath.length; i++) {
            double currX = optimalPath[i][0];
            double currY = optimalPath[i][1];
            System.out.format("V%d(%4.2f, %4.2f) to ", i, currX, currY);
            if(i > 0 && i % 8 == 0)
                System.out.println();
        }
        //from final node back to node 0:
        System.out.format("V%d(%4.2f, %4.2f)\n", startVertex, optimalPath[startVertex][0], optimalPath[startVertex][1]);
        double shortestEdge = calculateOptimalEdge();
        System.out.println("|  Min-Edge: " + shortestEdge + "  |  Expected distance: " + shortestEdge * optimalPath.length + "  |");
    }

    public void printPath(int[] path)
    {
        int startVertex = 0;
        for (int i = 0; i < path.length; i++) {
            int currVertex = path[i];
            double currX = points[currVertex][0];
            double currY = points[currVertex][1];
            System.out.format("V%d(%4.2f, %4.2f) to ", currVertex, currX, currY);
            if(i > 0 && i % 9 == 0)
                System.out.println();
        }
        //from final node back to node 0:
        System.out.format("V%d(%4.2f, %4.2f)\n", startVertex, points[0][0], points[0][1]);
    }

    public double calculateOptimalEdge()
    {
        return this.calculateDistance(optimalPath[0][0], optimalPath[0][1], optimalPath[1][0], optimalPath[1][1]);
    }

    public void printCostMatrix(){
        System.out.format("%2sN = %2d", " ", V);
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
