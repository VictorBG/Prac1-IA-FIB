package IA.Azamon.experiments;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import IA.Azamon.Experiment;
import IA.Azamon.functions.heuristic.AzamonHeuristicPriceAndHappiness;
import IA.Azamon.functions.initial.InitialSolutionFillOrder;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionHC;
import IA.Azamon.utils.Utils;


public class RunnableExperiment6 extends RunnableExperiment
{
    public RunnableExperiment6(final Workbook workbook)
    {
        super(workbook, "RunnableExperiment6", "result");
    }

    public RunnableExperiment6(final Workbook workbook, final String title)
    {
        super(workbook, title, "result");
    }

    public RunnableExperiment6(final Workbook workbook, final String title, final String filename)
    {
        super(workbook, title, filename);
    }

    @Override
    public void run(final boolean useHC) throws Exception
    {
        AzamonSuccessorFunctionHC.USE_MOVE = true;
        AzamonSuccessorFunctionHC.USE_SWAP = false;

        final Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Experimento 6");
        final Row row1 = sheet.createRow(1);
        final Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Ponderación");

        int i = 0;
        for (Integer seed : Utils.getSeeds())
        {
            row1.createCell((i * 3) + 1).setCellValue(i);
            row2.createCell((i * 3) + 1).setCellValue("Initial Price");
            row2.createCell((i * 3) + 2).setCellValue("Price");
            row2.createCell((i * 3) + 3).setCellValue("Happiness");

            for (int j = 1; j < 30; j++)
            {
                AzamonHeuristicPriceAndHappiness.WEIGHT = j;
                Row row = sheet.getRow(j + 2);
                if (row == null)
                {
                    row = sheet.createRow(j + 2);
                }
                row.createCell(0).setCellValue(j);
                Cell c1 = row.createCell((i * 3) + 1);
                Cell c2 = row.createCell((i * 3) + 2);
                Cell c3 = row.createCell((i * 3) + 3);
                final Experiment experiment = new Experiment(seed, 100, 1.2, 10, false, false, false);
                experiment.setHappinessResult(c3::setCellValue);
                experiment.setPriceResult(c2::setCellValue);
                experiment.setInitialPriceResult(c1::setCellValue);
                _run(experiment, useHC);
            }
            i++;
        }
    }

    private void _run(final Experiment experiment, final boolean useHC) throws Exception
    {
        if (useHC)
        {
            experiment.runHC(new AzamonHeuristicPriceAndHappiness(), new InitialSolutionFillOrder());
        }
        else
        {
            experiment.runSA(10000, 1000, 5, 0.001, new AzamonHeuristicPriceAndHappiness(), new InitialSolutionFillOrder());
        }
    }
}
