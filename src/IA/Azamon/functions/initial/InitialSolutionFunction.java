package IA.Azamon.functions.initial;

import IA.Azamon.AzamonState;


@FunctionalInterface
public interface InitialSolutionFunction
{
    void generateSolution(final AzamonState azamonState);
}
