package IA.Azamon.experiments;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import IA.Azamon.Experiment;
import IA.Azamon.functions.heuristic.AzamonHeuristicPrice;
import IA.Azamon.functions.initial.InitialSolutionFillOrder;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionHC;
import IA.Azamon.utils.Utils;


public class RunnableExperiment1 extends RunnableExperiment
{
    public RunnableExperiment1(final Workbook workbook)
    {
        super(workbook, "RunnableExperiment1", "result");
    }

    public RunnableExperiment1(final Workbook workbook, final String title)
    {
        super(workbook, title, "result");
    }

    public RunnableExperiment1(final Workbook workbook, final String title, final String filename)
    {
        super(workbook, title, filename);
    }

    @Override
    public void run(final boolean useHC) throws Exception
    {
        final Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Seed");
        row0.createCell(1).setCellValue("Move y swap");
        row0.createCell(3).setCellValue("Move");
        row0.createCell(5).setCellValue("Swap");

        final Row row1 = sheet.createRow(1);
        for (int i = 1; i <= 5; i += 2)
        {
            row1.createCell(i).setCellValue("Tiempo (ms)");
            row1.createCell(i + 1).setCellValue("Coste (€)");
        }

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); //seed
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2)); //move y swap
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4)); //move
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6)); //swap

        int i = 0;
        for (Integer seed : Utils.getSeeds())
        {
            System.out.println(i);
            Row row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(i + 1);

            final Experiment experiment = new Experiment(seed, 100, 1.2, 1, false, false, false);

            AzamonSuccessorFunctionHC.USE_MOVE = true;
            AzamonSuccessorFunctionHC.USE_SWAP = true;
            Cell timeCell1 = row.createCell(1);
            Cell priceCell1 = row.createCell(2);
            experiment.setTimeResult(timeCell1::setCellValue);
            experiment.setPriceResult(priceCell1::setCellValue);
            _run(experiment, useHC);

            AzamonSuccessorFunctionHC.USE_MOVE = true;
            AzamonSuccessorFunctionHC.USE_SWAP = false;
            timeCell1 = row.createCell(3);
            priceCell1 = row.createCell(4);
            experiment.setTimeResult(timeCell1::setCellValue);
            experiment.setPriceResult(priceCell1::setCellValue);
            _run(experiment, useHC);

            AzamonSuccessorFunctionHC.USE_MOVE = false;
            AzamonSuccessorFunctionHC.USE_SWAP = true;
            timeCell1 = row.createCell(5);
            priceCell1 = row.createCell(6);
            experiment.setTimeResult(timeCell1::setCellValue);
            experiment.setPriceResult(priceCell1::setCellValue);
            _run(experiment, useHC);
            i++;
        }

        Row finalRow = sheet.createRow(17);
        finalRow.createCell(0).setCellValue("Media");
        for (i = 1; i < 6; i++)
        {
            finalRow.createCell(i).setCellValue(String.format("=AVERAGE(%c3:%c17)", (char) i + 'A', (char) i + 'A'));
        }

        for (i = 0; i < 7; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

    private void _run(final Experiment experiment, final boolean useHC) throws Exception
    {
        if (useHC)
        {
            experiment.runHC(new AzamonHeuristicPrice(), new InitialSolutionFillOrder());
        }
        else
        {
            experiment.runSA(10000, 1000, 5, 0.001, new AzamonHeuristicPrice(), new InitialSolutionFillOrder());
        }
    }
}
