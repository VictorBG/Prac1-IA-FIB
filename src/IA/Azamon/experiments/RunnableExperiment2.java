package IA.Azamon.experiments;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import IA.Azamon.Experiment;
import IA.Azamon.functions.heuristic.AzamonHeuristicPrice;
import IA.Azamon.functions.initial.InitialSolutionFillOrder;
import IA.Azamon.functions.initial.InitialSolutionFillOrderMaxCap;
import IA.Azamon.functions.initial.InitialSolutionFunction;
import IA.Azamon.functions.successors.AzamonSuccessorFunctionHC;
import IA.Azamon.utils.Utils;


public class RunnableExperiment2 extends RunnableExperiment
{

    public RunnableExperiment2(final Workbook workbook)
    {
        super(workbook, "RunnableExperiment2", "result");
    }

    public RunnableExperiment2(final Workbook workbook, final String title)
    {
        super(workbook, title, "result");
    }

    public RunnableExperiment2(final Workbook workbook, final String title, final String filename)
    {
        super(workbook, title, filename);
    }

    @Override
    public void run(final boolean useHC) throws Exception
    {
        AzamonSuccessorFunctionHC.USE_MOVE = true;
        AzamonSuccessorFunctionHC.USE_SWAP = false;

        final Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Seed");
        row0.createCell(1).setCellValue("Estado inicial 1 (Estado sin máximos)");
        row0.createCell(5).setCellValue("Estado inicial 2 (máxima capacidad 0,75)");
        row0.createCell(9).setCellValue("Estado inicial 3 (máxima capacidad 0,5)");
        row0.createCell(13).setCellValue("Estado inicial 4 (máxima capacidad 0,9)");

        final Row row1 = sheet.createRow(1);
        for (int i = 1; i <= 13; i += 4)
        {
            row1.createCell(i).setCellValue("Tiempo (ms)");
            row1.createCell(i + 1).setCellValue("Coste (€)");
            row1.createCell(i + 2).setCellValue("Nodos expandidos");
            row1.createCell(i + 3).setCellValue("Almacén");
        }

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); //seed
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4)); //estado inicial 1
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 8)); //estado inicial 2
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 12)); //estado inicial 3
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 16)); //estado inicial 3

        int i = 0;
        for (Integer seed : Utils.getSeeds())
        {

            Row row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(i + 1);

            final Experiment experiment = new Experiment(seed, 100, 1.2, 10, false, false, false);

            Cell timeCell = row.createCell(1);
            Cell priceCell = row.createCell(2);
            Cell nodesCell = row.createCell(3);
            Cell storageCell = row.createCell(4);
            experiment.setTimeResult(timeCell::setCellValue);
            experiment.setPriceResult(priceCell::setCellValue);
            experiment.setNodesResult(nodesCell::setCellValue);
            experiment.setStorageResult(storageCell::setCellValue);

            _run(experiment, new InitialSolutionFillOrder(), useHC);

            timeCell = row.createCell(5);
            priceCell = row.createCell(6);
            nodesCell = row.createCell(7);
            storageCell = row.createCell(8);
            experiment.setTimeResult(timeCell::setCellValue);
            experiment.setPriceResult(priceCell::setCellValue);
            experiment.setNodesResult(nodesCell::setCellValue);
            experiment.setStorageResult(storageCell::setCellValue);

            _run(experiment, new InitialSolutionFillOrderMaxCap(0.75), useHC);

            timeCell = row.createCell(9);
            priceCell = row.createCell(10);
            nodesCell = row.createCell(11);
            storageCell = row.createCell(12);
            experiment.setTimeResult(timeCell::setCellValue);
            experiment.setPriceResult(priceCell::setCellValue);
            experiment.setNodesResult(nodesCell::setCellValue);
            experiment.setStorageResult(storageCell::setCellValue);

            _run(experiment, new InitialSolutionFillOrderMaxCap(0.5), useHC);

            timeCell = row.createCell(13);
            priceCell = row.createCell(14);
            nodesCell = row.createCell(15);
            storageCell = row.createCell(16);
            experiment.setTimeResult(timeCell::setCellValue);
            experiment.setPriceResult(priceCell::setCellValue);
            experiment.setNodesResult(nodesCell::setCellValue);
            experiment.setStorageResult(storageCell::setCellValue);

            _run(experiment, new InitialSolutionFillOrderMaxCap(0.9), useHC);
            i++;
        }

        Row finalRow = sheet.createRow(17);
        finalRow.createCell(0).setCellValue("Media");
        for (i = 1; i < 17; i++)
        {
            finalRow.createCell(i).setCellValue(String.format("=AVERAGE(%c3:%c17)", (char) i + 'A', (char) i + 'A'));
        }

        for (i = 0; i < 17; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

    private void _run(final Experiment experiment, final InitialSolutionFunction initialSolutionFunction, final boolean useHC) throws Exception
    {
        if (useHC)
        {
            experiment.runHC(new AzamonHeuristicPrice(), initialSolutionFunction);
        }
        else
        {
            experiment.runSA(10000, 1000, 5, 0.001, new AzamonHeuristicPrice(), initialSolutionFunction);
        }
    }
}
