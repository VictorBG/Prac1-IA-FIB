package IA.Azamon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import IA.Azamon.experiments.ConcatenableExperiment;
import IA.Azamon.experiments.RunnableExperiment;
import IA.Azamon.experiments.RunnableExperiment1;
import IA.Azamon.experiments.RunnableExperiment2;
import IA.Azamon.experiments.RunnableExperiment3;
import IA.Azamon.experiments.RunnableExperiment4;
import IA.Azamon.experiments.RunnableExperiment5;
import IA.Azamon.experiments.RunnableExperiment6;
import IA.Azamon.experiments.RunnableExperiment9;
import IA.Azamon.functions.heuristic.AzamonHeuristicPrice;
import IA.Azamon.functions.heuristic.AzamonHeuristicPriceAndHappiness;
import IA.Azamon.functions.initial.InitialSolutionFillOrder;
import IA.Azamon.functions.initial.InitialSolutionFillOrderMaxCap;
import IA.Azamon.functions.initial.InitialSolutionFunction;
import aima.search.framework.HeuristicFunction;


public class Menu
{
    private final Scanner scanner;

    public Menu()
    {
        scanner = new Scanner(System.in);
    }

    public void show() throws Exception
    {
        int option;
        do
        {
            showOptions();
            option = scanner.nextInt();
            switch (option)
            {
                case 0:
                    generalOption(true);
                    break;
                case 1:
                    generalOption(false);
                    break;
                case 2:
                    experiments();
            }
        }
        while (option != 3);
    }

    private void showOptions()
    {
        final String builder = "\n"
            + "0. Hill Climbing\n"
            + "1. Simulated Annealing\n"
            + "2. Experiments\n"
            + "3. Exit";

        System.out.println(builder);
    }

    private void generalOption(final boolean useHC) throws Exception
    {
        System.out.println("Select seed (-1 = random)");
        int seed = scanner.nextInt();
        if (seed == -1)
        {
            seed = new Random().nextInt();
        }

        System.out.println("Select packages");
        int packages = scanner.nextInt();

        System.out.println("Select proportion (use , as decimal point)");
        double proportion = scanner.nextDouble();

        System.out.println("Show instrumentation? (0 = no, 1=yes)");
        boolean showInstrumentation = scanner.nextInt() == 1;

        System.out.println("Show actions? (0 = no, 1=yes)");
        boolean showActions = scanner.nextInt() == 1;

        //System.out.println("Show final state? (0 = no, 1=yes)");
        boolean showFinalState = false;

        System.out.println("Show info? (0 = no, 1=yes)");
        boolean showInfo = scanner.nextInt() == 1;

        final InitialSolutionFunction initialSolutionFunction = getInitialSolutionFunction();
        final HeuristicFunction heuristicFunction = getHeuristicFunction();

        final Experiment experiment = new Experiment(seed, packages, proportion, 10, showInstrumentation, showActions, showFinalState, showInfo);

        if (useHC)
        {
            experiment.runHC(heuristicFunction, initialSolutionFunction);
        }
        else
        {
            System.out.println("Select K");
            int k = scanner.nextInt();

            System.out.println("Select lambda");
            double lambda = scanner.nextDouble();

            System.out.println("Select steps");
            int steps = scanner.nextInt();

            System.out.println("Select iterations");
            int stiter = scanner.nextInt();

            experiment.runSA(steps, stiter, k, lambda, heuristicFunction, initialSolutionFunction);
        }
    }

    private InitialSolutionFunction getInitialSolutionFunction()
    {
        int option;
        do
        {
            System.out.println("Select initial solution generator:\n"
                + "\t0. Fill in order\n"
                + "\t1. Fill in order with max. capacity");

            option = scanner.nextInt();
        }
        while (option != 0 && option != 1);

        if (option == 0)
        {
            return new InitialSolutionFillOrder();
        }
        else
        {
            double capacity;
            do
            {
                System.out.println("Select maximum capacity ( (0-1] ):");
                capacity = scanner.nextDouble();
            }
            while (capacity <= 0 || capacity > 1);
            return new InitialSolutionFillOrderMaxCap(capacity);
        }
    }

    private HeuristicFunction getHeuristicFunction()
    {
        int option;
        do
        {
            System.out.println("Select heuristic function:\n"
                + "\t0. Price\n"
                + "\t1. Price & Happiness");

            option = scanner.nextInt();
        }
        while (option != 0 && option != 1);

        if (option == 0)
        {
            return new AzamonHeuristicPrice();
        }
        else
        {
            return new AzamonHeuristicPriceAndHappiness();
        }
    }

    private void experiments() throws Exception
    {
        final Workbook workbook = new XSSFWorkbook();
        final Map<Integer, RunnableExperiment> experiments = new HashMap<>();
        int option;
        do
        {
            switch (getExperiment(experiments.keySet()))
            {
                case 1:
                    experiments.put(1, new RunnableExperiment1(workbook));
                    break;
                case 2:
                    experiments.put(2, new RunnableExperiment2(workbook));
                    break;
                case 3:
                    experiments.put(3, new RunnableExperiment3(workbook));
                    break;
                case 4:
                    experiments.put(4, new RunnableExperiment4(workbook));
                    break;
                case 5:
                    experiments.put(5, new RunnableExperiment5(workbook));
                    break;
                case 6:
                    experiments.put(6, new RunnableExperiment6(workbook));
                    break;
                case 9:
                    new RunnableExperiment9().run();
                    return;
            }

            System.out.println("Choose action:\n"
                + (experiments.size() == 7 ? "" : "\t0.Concat another experiment\n")
                + "\t1. Run\n"
                + "\t2. Run & Save");

            option = scanner.nextInt();

            if (experiments.size() == 7)
            {
                break;
            }
        }
        while (option != 1 && option != 2);

        if (experiments.size() == 1)
        {
            System.out.println("Running experiment");
            RunnableExperiment runnableExperiment = new ArrayList<>(experiments.values()).get(0);
            runnableExperiment.run();
            if (option == 2)
            {
                runnableExperiment.save();
            }
        }
        else if (experiments.size() > 1)
        {
            System.out.println("Running experiments");
            ConcatenableExperiment concatenableExperiment = null;
            for (RunnableExperiment runnableExperiment : experiments.values())
            {
                if (concatenableExperiment == null)
                {
                    concatenableExperiment = runnableExperiment.createConcat();
                }
                else
                {
                    concatenableExperiment.andThen(runnableExperiment);
                }
            }
            concatenableExperiment.run();
            if (option == 2)
            {
                concatenableExperiment.save();
            }
        }
    }

    private int getExperiment(Set<Integer> forbiddenOptions)
    {
        int option;
        do
        {
            System.out.println("Choose experiment to run:\n"
                + (forbiddenOptions.contains(1) ? "" : "\t1. Choosing the best operators\n")
                + (forbiddenOptions.contains(2) ? "" : "\t2. Choosing the best initial strategy\n")
                + (forbiddenOptions.contains(3) ? "" : "\t3. Choosing the best parameters for SA\n")
                + (forbiddenOptions.contains(4) ? "" : "\t4. Studying packages and proportion number\n")
                + (forbiddenOptions.contains(5) ? "" : "\t5. Studying proportion number with cost\n")
                + (forbiddenOptions.contains(6) ? "" : "\t6. Studying happiness heuristic\n")
                + (forbiddenOptions.contains(7) ? "" : "\t7. 1,2,4,5 and 6 with SA\n")
                + (forbiddenOptions.contains(9) ? "" : "\t9. Special experiment\n")
            );
            option = scanner.nextInt();
        }
        while (option < 1 || option > 9 || option == 8 || forbiddenOptions.contains(option));

        return option;
    }

}
