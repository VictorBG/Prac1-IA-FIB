package IA.Azamon.experiments;

import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import IA.Azamon.Experiment;
import IA.Azamon.functions.heuristic.AzamonHeuristicPrice;
import IA.Azamon.functions.initial.InitialSolutionFillOrderMaxCap;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionHC;
import IA.Azamon.utils.Utils;


public class RunnableExperiment5 extends RunnableExperiment
{
    public RunnableExperiment5(final Workbook workbook)
    {
        super(workbook, "RunnableExperiment5", "result");
    }

    public RunnableExperiment5(final Workbook workbook, final String title)
    {
        super(workbook, title, "result");
    }

    public RunnableExperiment5(final Workbook workbook, final String title, final String filename)
    {
        super(workbook, title, filename);
    }

    @Override
    public void run(final boolean useHC) throws Exception
    {
        AzamonSuccessorFunctionHC.USE_MOVE = true;
        AzamonSuccessorFunctionHC.USE_SWAP = false;

        final DecimalFormat df2 = new DecimalFormat("#.##");

        final Row row0 = sheet.createRow(0);
        final Row row1 = sheet.createRow(1);
        row0.createCell(0).setCellValue("Seed");
        row0.createCell(1).setCellValue("Porporción");

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); //seed
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 10));

        int k = 0;
        for (Integer seed : Utils.getSeeds())
        {
            Row row = sheet.createRow(k + 2);
            row.createCell(0).setCellValue(k + 1);

            for (double i = 1.2, j = 1; i < 3; i += 0.2, j++)
            {
                final Experiment experiment = new Experiment(seed, 100, i, 1, false, false, false);
                row1.createCell((int) j).setCellValue(i);
                Cell cell = row.createCell((int) j);

                experiment.setPriceResult(value -> cell.setCellValue(df2.format(value)));
                _run(experiment, useHC);
            }
            k++;
        }

        for (int i = 0; i < 17; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

    private void _run(final Experiment experiment, final boolean useHC) throws Exception
    {
        if (useHC)
        {
            experiment.runHC(new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
        }
        else
        {
            experiment.runSA(10000, 1000, 5, 0.001, new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
        }
    }
}
