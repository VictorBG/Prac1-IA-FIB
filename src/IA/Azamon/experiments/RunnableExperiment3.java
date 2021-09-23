package IA.Azamon.experiments;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import IA.Azamon.Experiment;
import IA.Azamon.functions.heuristic.AzamonHeuristicPrice;
import IA.Azamon.functions.initial.InitialSolutionFillOrderMaxCap;
import IA.Azamon.utils.Function3;
import IA.Azamon.utils.Utils;


public class RunnableExperiment3 extends RunnableExperiment
{
    public RunnableExperiment3(final Workbook workbook)
    {
        super(workbook, "RunnableExperiment3", "result");
    }

    public RunnableExperiment3(final Workbook workbook, final String title)
    {
        super(workbook, title, "result");
    }

    public RunnableExperiment3(final Workbook workbook, final String title, final String filename)
    {
        super(workbook, title, filename);
    }

    @Override
    public void run(final boolean useHC) throws Exception
    {
        System.out.println("K variable con steps, stiter y lambda fija");

        int ii = 0;
        for (double d : Stream.of(0.1, 0.01, 0.001, 0.0001).collect(Collectors.toList()))
        {
            _runExperiment3(ii,
                "K = 1, lamb = " + d + ", steps = 10000",
                DoubleStream.iterate(1000, i -> i + 100).limit(15).boxed().collect(Collectors.toList()),
                (experiment, value, consumer) -> {
                    experiment.setPriceResult(consumer);
                    try
                    {
                        experiment.runSA(10000, value.intValue(), 1, d, new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

            _runExperiment3(ii + 20,
                "K = 5, lamb = " + d + ", steps = 10000",
                DoubleStream.iterate(1000, i -> i + 100).limit(15).boxed().collect(Collectors.toList()),
                (experiment, value, consumer) -> {
                    experiment.setPriceResult(consumer);
                    try
                    {
                        experiment.runSA(10000, value.intValue(), 5, d, new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

            _runExperiment3(ii + 40,
                "K = 25, lamb = " + d + ", steps = 10000",
                DoubleStream.iterate(1000, i -> i + 100).limit(15).boxed().collect(Collectors.toList()),
                (experiment, value, consumer) -> {
                    experiment.setPriceResult(consumer);
                    try
                    {
                        experiment.runSA(10000, value.intValue(), 25, d, new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

            _runExperiment3(ii + 60,
                "K = 125, lamb = " + d + ", steps = 10000",
                DoubleStream.iterate(1000, i -> i + 100).limit(15).boxed().collect(Collectors.toList()),
                (experiment, value, consumer) -> {
                    experiment.setPriceResult(consumer);
                    try
                    {
                        experiment.runSA(10000, value.intValue(), 125, d, new AzamonHeuristicPrice(), new InitialSolutionFillOrderMaxCap(0.9));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

            ii += 80;
        }
        for (int i = 0; i < 17; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

    private void _runExperiment3(final int offset,
                                 final String title,
                                 final List<Double> values,
                                 final Function3<Experiment, Double, Consumer<Double>> function) throws Exception
    {
        final Row row0 = sheet.createRow(offset);
        final Cell cell = row0.createCell(0);
        cell.setCellValue(title);
        final Row row1 = sheet.createRow(offset + 1);

        int i = 0;
        for (Integer seed : Utils.getSeeds())
        {
            row1.createCell(i + 1).setCellValue(i);
            final Experiment experiment = new Experiment(seed, 100, 1.2, 1, false, false, false);
            int k = 2;
            for (final Double value : values)
            {
                Row row = sheet.getRow(k + offset);
                if (row == null)
                {
                    row = sheet.createRow(k + offset);
                }
                row.createCell(0).setCellValue(value);
                Cell resultCell = row.createCell(i + 1);
                function.apply(experiment, value, resultCell::setCellValue);
                k++;
            }
            i++;
        }
    }
}
