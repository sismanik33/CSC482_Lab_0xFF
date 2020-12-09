import java.awt.*;
import java.util.Random;

//adapted from code found @ https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjJ5KK4hrDtAhXbElkFHTY6DvsQFjAAegQIBBAC&url=ftp%3A%2F%2Fftp.cs.princeton.edu%2Fpub%2Fcs226%2Fmap%2FEuclideanGraph.java&usg=AOvVaw1vwr1CTDo_s-8-s-qubBjf
public class EuclideanGraph {
    private int V;
    private Point[] points;   // points in the plane
    private int maxCoord = 100;
    public double costMatrix[][] = new double[V][V];

    public EuclideanGraph(int numVertices) {
        V = numVertices;

        // read in and insert vertices
        points = new Point[V];
        costMatrix = new double[V][V];
        Random rand = new Random();
        for (int i = 0; i < V; i++) {
            int v = i;
            int x = rand.nextInt(maxCoord + 1);
            int y = rand.nextInt(maxCoord + 1);
            points[v] = new Point(x, y);
        }
        generateCostMatrix();
    }

    public void generateCostMatrix()
    {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < i; j++) {
                double distance = points[i].distance(points[j]);
                costMatrix[i][j] = distance;
                costMatrix[j][i] = distance;
            }
        }
    }

    public void printVertices(){
        for (int i = 0; i < V; i++)
        {
            System.out.println("V " + i + ": " + points[i].x + ", " + points[i].y);
        }
    }

    public void printCostMatrix(){
        System.out.format("%6s", "");
        for (int i = 0; i < V; i++) {
            System.out.format("%6d", i);
        }
        System.out.println();
        String dash = "-";
        dash = dash.repeat((V + 2) * 6);
        System.out.println(dash);
        for (int i = 0; i < V; i++) {
            System.out.format("%6d| ", i);
            for (int j = 0; j < V; j++) {
                System.out.format("%6.1f", costMatrix[i][j]);
            }
            System.out.println();
        }
    }

}
