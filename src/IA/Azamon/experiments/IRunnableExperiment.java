package IA.Azamon.experiments;

public interface IRunnableExperiment
{
    void run(boolean useHC) throws Exception;

    default void run() throws Exception
    {
        run(true);
    }
}
