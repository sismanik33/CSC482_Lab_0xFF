import java.util.Arrays;
import java.util.Random;

public class TSP {
    public static double bestTourLength;
    public static int[] bestTour;
    public static int pathCount = 0;
    public static double greedyBestTour;

    public TSP (int numVertices)
    {
        bestTourLength = Integer.MAX_VALUE;
        bestTour = new int[numVertices];
        greedyBestTour = Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        RunTests.runTests();

//        int numV = 10;
//        double[][] rGraph = new double[numV][numV];
//        generateRandomCostMatrix(rGraph, numV, 50);
//        printAdjacencyMatrix(rGraph);
//        mctBruteForce(rGraph);
//        EuclideanGraph euclidGraph = new EuclideanGraph(numVertices);
//        euclidGraph.printVertices();
//        euclidGraph.printCostMatrix();
//        double[][] euclidCM = euclidGraph.costMatrix;


//        int n = 16;
//        CircularGraph cg = new CircularGraph(n, 6);
//        cg.printVertices();
//        cg.printCostMatrix();
//        double[][] costMatrix = cg.costMatrix;
//        greedyBestTour(costMatrix, 0);

//        int n = 10;
//        int max = 20;
//        int[][] costMatrix = new int[n][n];
//        generateRandomCostMatrix(costMatrix, n, max);
//        mctBruteForce(costMatrix);
//
//        printAdjacencyMatrix(costMatrix);
//        greedyBestTour(costMatrix, 0);
//
//        System.out.println("Best tour: " + bestTourLength);
//        for (int j : bestTour)
//            System.out.print(j + " ");
//        System.out.println();
    }
    static void generateRandomCostMatrix(double[][] g, int n, int max){
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int value = rand.nextInt(max) + 1;
                g[i][j] = value;
                g[j][i] = value;
            }
        }
    }
    static void printAdjacencyMatrix(double[][] g){
        int cellWidth = 6;
        int len = g[0].length;
        System.out.format("%8s", "");
        for (int i = 0; i < len; i++) {
            System.out.format("%6d", i);
        }
        System.out.println();
        String dash = "-";
        dash = dash.repeat((g.length + 2) * cellWidth);
        System.out.println(dash);
        for (int i = 0; i < len; i++) {
            System.out.format("%6d| ", i);
            for (int j = 0; j < len; j++) {
                System.out.format("%6.1f", g[i][j]);
            }
            System.out.println();
        }
    }

    public static void mctBruteForce(double[][] costMatrix)
    {
        int n = costMatrix[0].length;
        int[] path = new int[n];
        int startNode = 0;
        //initialize path to sequence 0 -> n-1
        for (int i = 0; i < n; i++)
            path[i] = i;

        TSP pathFinder = new TSP(n);
        pathFinder.permute(costMatrix, path, startNode + 1, n - 1); //startNode+1 because only want to permute nodes after start.
    }

    private void permute(double[][] costMatrix, int[] path, int l, int r)
    {
        if (l == r)
        {
            double pathCost = findPathCost(costMatrix, path);
            pathCount++;
            if(pathCost < bestTourLength){
                bestTourLength = pathCost;
                bestTour = Arrays.copyOf(path, path.length);
            }
        }
        else
        {
            for (int i = l; i <= r; i++)
            {
                path = swap(path,l,i);
                permute(costMatrix, path, l+1, r);
                path = swap(path,l,i);
            }
        }
    }

    public int[] swap(int[] path, int i, int j)
    {
        int temp;
        temp = path[i] ;
        path[i] = path[j];
        path[j] = temp;
        return path;
    }

    public double findPathCost(double[][] costMatrix, int[] path)
    {
        int n = path.length;
        double totalCost = 0;
        for (int i = 0; i < n; i++) {
            if (i < n - 1)//find distance to next node
            {
                int currVert = path[i];
                int nextVert = path[i+1];
                totalCost += costMatrix[currVert][nextVert];
            }
            else //find distance from last node back to 0
            {
                int finalVert = path[i];
                totalCost += costMatrix[finalVert][0];
            }

        }
        return totalCost;
    }



    public static int[] mctGreedy(double[][] g, int startV)
    {
        int numV = g[0].length;
        double pathCost = 0;
        boolean[] visited = new boolean[numV];
        Arrays.fill(visited, false);
        visited[0] = true;
        int[] greedyPath = new int[numV + 1];
        greedyPath[0] = startV;
        int currVertex = startV;
        int nodesVisited = 1;
        double[] costsFromCurrVertex = new double[numV];
        while(nodesVisited < numV)
        {
            costsFromCurrVertex = g[currVertex]; //edge costs to all vertices from current vertex
            int bestNextStep = -1; //index of node to go to next
            double minCost = Integer.MAX_VALUE; //should initiate to inf, MAX_VAL is closest.
            for (int i = 0; i < numV; i++) {
                if (costsFromCurrVertex[i] < minCost && !visited[i] && i != currVertex)
                {
                    minCost = costsFromCurrVertex[i];
                    bestNextStep = i;
                }
            }
            greedyPath[nodesVisited] = bestNextStep;
            visited[bestNextStep] = true;
            pathCost += costsFromCurrVertex[bestNextStep];
            nodesVisited++;
            currVertex = bestNextStep;
        }
        greedyPath[numV] = startV;
        int finalVertexBeforeReturn = greedyPath[numV - 1];
        pathCost += g[finalVertexBeforeReturn][0];
        greedyBestTour = pathCost;

        return greedyPath;
    }

    //adapted from https://www.geeksforgeeks.org/binary-search/
    public static boolean arrayContains(int[] list, int valueToFind)
    {
        int l = 0, r = list.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (list[m] == valueToFind)
                return true;
            if (list[m] < valueToFind)
                l = m + 1;
            else
                r = m - 1;
        }

        return false;
    }

    public static void mctDynamicProg(int[][] costMatrix)
    {
        int n = costMatrix[0].length;
        int subSolutionSize = (int)Math.pow(2, n) - 1; // debug: do i need -1 at end?
        int[] subSolutionTable = new int[subSolutionSize];
        int[] path = new int[n];
        int startNode = 0;
        //initialize path to sequence 0 -> n-1
        for (int i = 0; i < n; i++)
            path[i] = i;

    }

    public int genPathMask(int[] remNodes)
    {
        int nodeMask = 0;
        for (int node : remNodes){
            nodeMask += Math.pow(2, node);
        }
        return nodeMask;
    }

}
