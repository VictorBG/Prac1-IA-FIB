package IA.Azamon.functions.initial;

import IA.Azamon.AzamonState;


/**
 * 1 package to every offer available
 */
public class InitialSolutionFillOrderMaxCap implements InitialSolutionFunction
{
    private final double maxCapacity;

    public InitialSolutionFillOrderMaxCap(final double maxCapacity)
    {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void generateSolution(final AzamonState azamonState)
    {
        for (int i = 0; i < 3; i++)
        {
            assignPrio(azamonState, i);
        }
    }

    private void assignPrio(final AzamonState azamonState, int prio)
    {
        for (int i = 0; i < AzamonState.PACKAGES.size(); i++)
        {
            if (AzamonState.PACKAGES.get(i).getPrioridad() <= prio)
            {
                for (int j = 0; j < AzamonState.TRANSPORT.size(); j++)
                {
                    if (!isFull(azamonState, j) && azamonState.movePackage(i, j))
                    {
                        break;
                    }
                }
            }
        }
    }

    private boolean isFull(final AzamonState azamonState, final int transportNumber)
    {
        return (getPeso(transportNumber) - azamonState.getRemainingCapacity(transportNumber)) / getPeso(transportNumber) >= maxCapacity;
    }

    private double getPeso(final int transportNumber)
    {
        return AzamonState.TRANSPORT.get(transportNumber).getPesomax();
    }
}
