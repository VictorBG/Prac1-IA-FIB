package IA.Azamon.experiments;

import org.apache.poi.ss.usermodel.Workbook;


public class RunnableExperiment7 implements IRunnableExperiment
{
    final Workbook workbook;

    public RunnableExperiment7(final Workbook workbook)
    {
        this.workbook = workbook;
    }

    @Override
    public void run(final boolean useHC) throws Exception
    {
        new RunnableExperiment1(workbook, "Experiment 1 SA")
            .andThen(new RunnableExperiment2(workbook, "Experiment 2 SA"))
            .andThen(new RunnableExperiment4(workbook, "Experiment 4 SA"))
            .andThen(new RunnableExperiment5(workbook, "Experiment 5 SA"))
            .andThen(new RunnableExperiment6(workbook, "Experiment 6 SA", "result_final_sa"))
            .run(false)
            .save();
    }
}
