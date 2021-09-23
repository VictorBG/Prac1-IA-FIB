package IA.Azamon;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import IA.Azamon.functions.initial.InitialSolutionFunction;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionHC;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionSA;
import IA.Azamon.goal.AzamonGoalTest;
import IA.Azamon.utils.Utils;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;


public final class Experiment
{
    private final static int DEFAULT_ITERATIONS = 10;
    private final int seed;
    private final int packages;
    private final double proportionPackagesOffer;
    private final int iterations;

    private final boolean showInstrumentation;
    private final boolean showActions;
    private final boolean showFinalState;
    private final boolean showInfo;

    private Search search;

    private Consumer<Double> timeResult;
    private Consumer<Double> priceResult;
    private Consumer<Double> happinessResult;
    private Consumer<Double> initialPriceResult;
    private Consumer<Double> nodesResult;
    private Consumer<Double> storageResult;

    public Experiment(final int packages,
                      final double proportionPackagesOffer)
    {
        this(new Random().nextInt(), packages, proportionPackagesOffer, DEFAULT_ITERATIONS, false, false, false, false);
    }

    public Experiment(final int packages,
                      final double proportionPackagesOffer,
                      final int iterations)
    {
        this(new Random().nextInt(), packages, proportionPackagesOffer, iterations, false, false, false, false);
    }

    public Experiment(final int seed,
                      final int packages,
                      final double proportionPackagesOffer,
                      final int iterations)
    {
        this(seed, packages, proportionPackagesOffer, iterations, false, false, false, false);
    }

    public Experiment(final int seed,
                      final int packages,
                      final double proportionPackagesOffer,
                      final int iterations,
                      final boolean showInstrumentation,
                      final boolean showActions,
                      final boolean showFinalState
    )
    {
        this(seed, packages, proportionPackagesOffer, iterations, showInstrumentation, showActions, showFinalState, false);
    }

    public Experiment(final int seed,
                      final int packages,
                      final double proportionPackagesOffer,
                      final int iterations,
                      final boolean showInstrumentation,
                      final boolean showActions,
                      final boolean showFinalState,
                      final boolean showInfo
    )
    {
        this.seed = seed;
        this.packages = packages;
        this.proportionPackagesOffer = proportionPackagesOffer;
        this.iterations = iterations;
        this.showInstrumentation = showInstrumentation;
        this.showActions = showActions;
        this.showFinalState = showFinalState;
        this.showInfo = showInfo;
    }

    public void runHC(final HeuristicFunction heuristicFunction, final InitialSolutionFunction initialSolutionFunction) throws Exception
    {
        run(true, 0, 0, 0, 0, heuristicFunction, initialSolutionFunction);
    }

    public void runSA(int steps,
                      int stiter,
                      int k,
                      double lamb,
                      final HeuristicFunction heuristicFunction,
                      final InitialSolutionFunction initialSolutionFunction) throws Exception
    {
        run(false, steps, stiter, k, lamb, heuristicFunction, initialSolutionFunction);
    }

    static int NODOS = 0;

    private void run(boolean useHC,
                     int steps,
                     int stiter,
                     int k,
                     double lamb,
                     final HeuristicFunction heuristicFunction,
                     final InitialSolutionFunction initialSolutionFunction) throws Exception
    {
        long totalTime = 0;
        final AtomicReference<Double> totalCost = new AtomicReference<>((double) 0);
        final AtomicReference<Double> totalHappiness = new AtomicReference<>((double) 0);
        final AtomicReference<Double> totalStorage = new AtomicReference<>((double) 0);
        final AtomicReference<Double> initialCost = new AtomicReference<>((double) 0);

        final AzamonState azamonState = new AzamonState(packages, seed, proportionPackagesOffer, seed);

        NODOS = 0;
        for (int i = 0; i < iterations; i++)
        {
            totalTime += Utils.countExecutionTime(() -> {
                azamonState.generateSolution(initialSolutionFunction);
                initialCost.updateAndGet(v -> v + azamonState.getTotalPrice());
                search = useHC ? getHC(azamonState, heuristicFunction) : getSA(azamonState, steps, stiter, k, lamb, heuristicFunction);
                return null;
            });

            final AzamonState goalState = (AzamonState) search.getGoalState();
            totalCost.updateAndGet(v -> v + goalState.getTotalPrice());
            totalHappiness.updateAndGet(v -> v + goalState.getHappiness());
            totalStorage.updateAndGet(v -> v + goalState.getItemsInStorage());
        }

        final double time = totalTime / (double) iterations;
        final double price = totalCost.get() / (double) iterations;
        final double happiness = totalHappiness.get() / (double) iterations;
        final double storage = totalStorage.get() / (double) iterations;
        final double nodes = NODOS / (double) iterations;

        if (showInfo)
        {
            System.out.println("   time          cost         nodos       almacen      happiness");
            System.out.printf("%f    %f    %f  %f  %f%n",
                time,
                price,
                NODOS / (double) iterations,
                storage,
                happiness);
        }

        returnResult(timeResult, time);
        returnResult(priceResult, price);
        returnResult(happinessResult, happiness);
        returnResult(nodesResult, nodes);
        returnResult(storageResult, storage);
        returnResult(initialPriceResult, initialCost.get() / (double) iterations);
    }

    private Search getHC(final AzamonState azamonState, final HeuristicFunction heuristicFunction) throws Exception
    {
        Problem problem = new Problem(azamonState, new AzamonSuccessorFunctionHC(), new AzamonGoalTest(), heuristicFunction);
        Search search = new HillClimbingSearch();
        SearchAgent searchAgent = new SearchAgent(problem, search);
        printActions(searchAgent.getActions());
        printInstrumentation(searchAgent.getInstrumentation());
        return search;
    }

    private Search getSA(final AzamonState azamonState, int steps, int stiter, int k, double lamb, final HeuristicFunction heuristicFunction)
        throws Exception
    {
        final Problem problem = new Problem(azamonState, new AzamonSuccessorFunctionSA(), new AzamonGoalTest(), heuristicFunction);
        final Search search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
        new SearchAgent(problem, search);
        return search;
    }

    private void printInstrumentation(Properties properties)
    {
        for (final Object o : properties.keySet())
        {
            String key = (String) o;
            String property = properties.getProperty(key);
            if (showInstrumentation)
            {
                System.out.println(key + " : " + property);
            }
            NODOS += Integer.parseInt(property);

        }
    }

    private void printActions(List actions)
    {
        if (showActions)
        {
            for (Object o : actions)
            {
                String action = (String) o;
                System.out.println(action);
            }
        }
    }

    public void setTimeResult(final Consumer<Double> timeResult)
    {
        this.timeResult = timeResult;
    }

    public void setPriceResult(final Consumer<Double> priceResult)
    {
        this.priceResult = priceResult;
    }

    public void setHappinessResult(final Consumer<Double> happinessResult)
    {
        this.happinessResult = happinessResult;
    }

    public void setInitialPriceResult(final Consumer<Double> initialPriceResult)
    {
        this.initialPriceResult = initialPriceResult;
    }

    public void setNodesResult(final Consumer<Double> nodesResult)
    {
        this.nodesResult = nodesResult;
    }

    public void setStorageResult(final Consumer<Double> storageResult)
    {
        this.storageResult = storageResult;
    }

    private void returnResult(final Consumer<Double> r, final Double result)
    {
        if (r != null)
        {
            r.accept(result);
        }
    }
}
