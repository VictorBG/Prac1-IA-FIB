package IA.Azamon.functions.heuristic;

import IA.Azamon.AzamonState;
import aima.search.framework.HeuristicFunction;


public class AzamonHeuristicPrice implements HeuristicFunction
{
    @Override
    public double getHeuristicValue(final Object o)
    {
        if (o instanceof AzamonState)
        {
            return ((AzamonState) o).getTotalPrice();
        }
        return 0D;
    }
}
