package IA.Azamon.experiments;

import IA.Azamon.Experiment;
import IA.Azamon.functions.heuristic.AzamonHeuristicPrice;
import IA.Azamon.functions.initial.InitialSolutionFillOrderMaxCap;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionHC;


public class RunnableExperiment9 implements IRunnableExperiment
{
    @Override
    public void run(final boolean useHC) throws Exception
    {
        AzamonSuccessorFunctionHC.USE_MOVE = true;
        AzamonSuccessorFunctionHC.USE_SWAP = false;

        final Experiment experiment = new Experiment(1234, 100, 1.2, 10, false, false, false, true);
        experiment.runHC(new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
    }
}
