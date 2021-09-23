package IA.Azamon.experiments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ConcatenableExperiment
{
    final List<RunnableExperiment> experiments;

    public static ConcatenableExperiment create(final RunnableExperiment runnableExperiment)
    {
        return new ConcatenableExperiment(runnableExperiment);
    }

    private ConcatenableExperiment(final RunnableExperiment runnableExperiment)
    {
        this.experiments = new ArrayList<RunnableExperiment>()
        {{
            add(runnableExperiment);
        }};
    }

    public ConcatenableExperiment andThen(final RunnableExperiment runnableExperiment)
    {
        this.experiments.add(runnableExperiment);
        return this;
    }

    public ConcatenableExperiment run() throws Exception
    {
        run(true);
        return this;
    }

    public ConcatenableExperiment run(final boolean useHC) throws Exception
    {
        int i = 0;
        for (RunnableExperiment runnableExperiment : this.experiments)
        {
            System.out.println("Running experiment: "+ i++);
            runnableExperiment.run(useHC);
        }
        return this;
    }

    public ConcatenableExperiment save() throws IOException
    {
        if (!this.experiments.isEmpty())
        {
            this.experiments.get(experiments.size() - 1).save();
        }
        return this;
    }
}
