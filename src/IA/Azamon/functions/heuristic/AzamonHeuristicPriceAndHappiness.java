package IA.Azamon.functions.heuristic;

import IA.Azamon.AzamonState;
import aima.search.framework.HeuristicFunction;


public class AzamonHeuristicPriceAndHappiness implements HeuristicFunction
{
    public static int WEIGHT = 1;

    @Override
    public double getHeuristicValue(final Object o)
    {
        if (o instanceof AzamonState)
        {
            final AzamonState state = (AzamonState) o;
            return state.getTotalPrice() - WEIGHT * state.getHappiness();
        }
        return 0D;
    }
}
