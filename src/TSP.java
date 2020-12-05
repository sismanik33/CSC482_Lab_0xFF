import java.util.Arrays;
import java.util.Random;

public class TSP {
    public static int bestTourLength = Integer.MAX_VALUE;
    public static int[] bestTour;
    public static int pathCount = 0;

    public static void main(String[] args) {
//        int numVertices = 10;
//        EuclideanGraph euclidGraph = new EuclideanGraph(numVertices);
//        euclidGraph.printVertices();
//        euclidGraph.printCostMatrix();
//        double[][] euclidCM = euclidGraph.costMatrix;
        int n = 4;
        CircularGraph cg = new CircularGraph(n, 3);
        cg.printVertices();
        cg.printCostMatrix();
        double[][] costMatrix = cg.costMatrix;
        greedyBestTour(costMatrix, 0);
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
    static void generateRandomCostMatrix(int[][] g, int n, int max){
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int value = rand.nextInt(max) + 1;
                g[i][j] = value;
                g[j][i] = value;
            }
        }
    }
    static void printAdjacencyMatrix(int[][] g){
        int len = g[0].length;
        System.out.format("%6s", "");
        for (int i = 0; i < len; i++) {
            System.out.format("%4d", i);
        }
        System.out.println();
        String dash = "-";
        dash = dash.repeat((g.length + 2) * 4);
        System.out.println(dash);
        for (int i = 0; i < len; i++) {
            System.out.format("%4d| ", i);
            for (int j = 0; j < len; j++) {
                System.out.format("%4d", g[i][j]);
            }
            System.out.println();
        }
    }

    public static void mctBruteForce(int[][] costMatrix)
    {
        int n = costMatrix[0].length;
        int[] path = new int[n];
        int startNode = 0;
        //initialize path to sequence 0 -> n-1
        for (int i = 0; i < n; i++)
            path[i] = i;

        TSP pathFinder = new TSP();
        pathFinder.permute(costMatrix, path, startNode + 1, n - 1); //startNode+1 because only want to permute nodes after start.
    }

    private void permute(int[][] costMatrix, int[] path, int l, int r)
    {
        if (l == r)
        {
            int pathCost = findPathCost(costMatrix, path);
            for(int p : path)
                System.out.print(p);
            System.out.println();
            System.out.println("Path cost: " + pathCost);
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

    public int findPathCost(int[][] costMatrix, int[] path)
    {
        int n = path.length;
        int totalCost = 0;
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



    public static void greedyBestTour(double[][] g, int startV)
    {
        int endV = 0;
        int numV = g[0].length;
        double pathCost = 0;
        int[] visited = new int[numV + 1];
        Arrays.fill(visited, -1);
        visited[0] = startV;
        int currVertex = startV;
        int nodesVisited = 1;

        while(nodesVisited < numV)
        {
            double[] costsFromCurrVertex = g[currVertex]; //edge costs to all vertices from current vertex
            int bestNextStep = -1;
            double minCost = Integer.MAX_VALUE; //should initiate to inf, MAX_VAL is closest.
            for (int i = 0; i < numV; i++) {
                //determine best next vertex that hasn't been visited
                if (costsFromCurrVertex[i] < minCost && !arrayContains(visited, i) && i != currVertex)
                {
                    minCost = costsFromCurrVertex[i];
                    bestNextStep = i;
                }
            }
            visited[nodesVisited] = bestNextStep;
            pathCost += costsFromCurrVertex[bestNextStep];
            nodesVisited++;
            currVertex = bestNextStep;
        }

        visited[numV] = startV;
        int finalVertexBeforeReturn = visited[numV - 1];
        pathCost += g[finalVertexBeforeReturn][0];

        System.out.print("greedy [");
        for (int i = 0; i < numV + 1; i++) {
            if(i < numV)
                System.out.print(visited[i] + ", ");
            else
                System.out.println(visited[i] + "]");
        }
        System.out.println("Path cost: " + pathCost);

    }

    public static boolean arrayContains(int[] list, int key)
    {
        for (int l : list) {
            if (l == key)
                return true;
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
