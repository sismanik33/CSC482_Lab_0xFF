import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class RunTests {

    public static void runTests()
    {
//        exactAlgoCorrectnessTest(15);
        heuristicAlgoCorrectnessTest(25);
//        performanceTestExact(15);
//        performanceTestHeuristic(20000);
//        calculateQualityOfSolution(15);
    }


    public static void exactAlgoCorrectnessTest(int maxVertices)
    {
        for (int N = 4; N < maxVertices; N++)
        {
            System.out.print("N = " + N + "\t | ");
            CircularGraph testGraph = new CircularGraph(N,100);
            testGraph.printOptimalPath();
            TSP.mctBruteForce(testGraph.costMatrix);
            System.out.println("Best path found on randomized nodes:");
            testGraph.printPath(TSP.bestTour);
            System.out.println("Calculated best tour distance: " + TSP.bestTourLength);
            testGraph.printCostMatrix();
            System.out.println();
        }
    }

    public static void heuristicAlgoCorrectnessTest(int maxVertices)
    {
        for (int N = 4; N < maxVertices; N++)
        {
            System.out.print("N = " + N + "\t | ");
            CircularGraph testGraph = new CircularGraph(N,100);
            testGraph.printOptimalPath();
            int[] calculatedPath = TSP.mctGreedy(testGraph.costMatrix, 0);
            System.out.println("Best path found on randomized nodes:");
            testGraph.printPath(calculatedPath);
            System.out.println("Calculated best tour distance: " + TSP.greedyBestTour);
            testGraph.printCostMatrix();
            System.out.println();

        }
    }

    public static void performanceTestExact(int maxVertices)
    {
        //Test N = 4 to ~mid teens
        System.out.format("%4s |%15s |%15s |%15s |\n", "", "", "Growth", "Expected");
        System.out.format("%4s |%15s |%15s |%15s |\n", "N", "Time", "Ratio", "Growth");
        String dash = "-";
        dash = dash.repeat(60);
        System.out.println(dash);
        long prevTime = -1;
        for (int N = 4; N < maxVertices; N++) {
            double[][] testMatrix = new double[N][N];
            TSP.generateRandomCostMatrix(testMatrix, N, 50);
            long elapsedTime = timeTest(testMatrix, N, 1);
            if(prevTime > -1)
                System.out.format("%4d |%15d |%15.2f |%15.1f |\n", N, elapsedTime, (double)elapsedTime/prevTime, (double)N);
            else
                System.out.format("%4d |%15d |%15s |%15s |\n", N, elapsedTime, "-", "-");
            prevTime = elapsedTime;
        }
    }

    public static void performanceTestHeuristic(int maxVertices)
    {
        System.out.format("%4s |%15s |%15s |%15s |\n", "", "", "Doubling", "Expected");
        System.out.format("%4s |%15s |%15s |%15s |\n", "N", "Time", "Ratio", "Doubling Ratio");
        String dash = "-";
        dash = dash.repeat(60);
        System.out.println(dash);
        long prevTime = -1;
        for (int N = 4; N < maxVertices; N *= 2) {
            double[][] testMatrix = new double[N][N];
            TSP.generateRandomCostMatrix(testMatrix, N, 50);
            long elapsedTime = timeTest(testMatrix, N, 2);
            double expDoubling = ((N*N) * (Math.log(N) ) / ( (N/2)*(N/2) * ( Math.log(N/2)  ) ) );
            if(prevTime > -1)
                System.out.format("%4d |%15d |%15.2f |%15.3f |\n", N, elapsedTime, (double)elapsedTime/prevTime, expDoubling);
            else
                System.out.format("%4d |%15d |%15s |%15s |\n", N, elapsedTime, "-", "-");
            prevTime = elapsedTime;
        }
    }

    public static long timeTest(double[][] testMatrix, int N, int testToRun)
    {
        long timeCalc = 0;
        if (testToRun == 1)
        {
            int testFactor = 200;
            if (N < 10)
            {
                int testsToRun = testFactor/N;
                long before = getCpuTime();
                for (int i = 0; i < testsToRun; i++) {
                    TSP.mctBruteForce(testMatrix);
                }
                long after = getCpuTime();
                timeCalc = (after - before)/testsToRun;
            }
            else
            {
                long before = getCpuTime();
                TSP.mctBruteForce(testMatrix);
                long after = getCpuTime();
                timeCalc = after - before;
            }
        }
        else
        {
            if (N < 500)
            {
                int testsToRun = 20;
                long before = getCpuTime();
                for (int i = 0; i < testsToRun; i++) {
                    TSP.mctGreedy(testMatrix, 0);
                }
                long after = getCpuTime();
                timeCalc = (after - before) / testsToRun;
            }
            else
            {
                long before = getCpuTime();
                TSP.mctGreedy(testMatrix, 0);
                long after = getCpuTime();
                timeCalc = after - before;
            }
        }
        return timeCalc;
    }

    public static void calculateQualityOfSolution(int maxVertices)
    {
        System.out.format("%4s |%15s |%15s |%15s |\n", "", "Avg Greedy", "Avg Exact", "Average");
        System.out.format("%4s |%15s |%15s |%15s |\n", "N", "Solution:", "Solution:", "SQR:");
        String dash = "-";
        dash = dash.repeat(60);
        System.out.println(dash);
        for (int V = 4; V < maxVertices; V++) {
            int testsToRun = (maxVertices - V) * 10;
            double bruteCumulativeSolution = 0;
            double greedyCumulativeSolution = 0;
            for (int i = 0; i < testsToRun; i++) {
                //generate random euclidean cost matrix
                EuclideanGraph g = new EuclideanGraph(V);
                TSP.bestTourLength = Integer.MAX_VALUE;
                TSP.greedyBestTour = Integer.MAX_VALUE;
                TSP.mctBruteForce(g.costMatrix);
                TSP.mctGreedy(g.costMatrix, 0);
                bruteCumulativeSolution += TSP.bestTourLength;
                greedyCumulativeSolution += TSP.greedyBestTour;
            }
            double avgGreedy = greedyCumulativeSolution / testsToRun;
            double avgBrute = bruteCumulativeSolution / testsToRun;
            double avgSQR = greedyCumulativeSolution / bruteCumulativeSolution;
            System.out.format("%4s |%15.2f |%15.2f |%15.2f |\n", V, avgGreedy, avgBrute, avgSQR);
        }
    }

    public static long getCpuTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;
    }

}
