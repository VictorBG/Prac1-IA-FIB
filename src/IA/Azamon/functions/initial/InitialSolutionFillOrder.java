package IA.Azamon.functions.initial;

import IA.Azamon.AzamonState;


public class InitialSolutionFillOrder implements InitialSolutionFunction
{
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
                    if (azamonState.movePackage(i, j))
                    {
                        break;
                    }
                }
            }
        }
    }
}
