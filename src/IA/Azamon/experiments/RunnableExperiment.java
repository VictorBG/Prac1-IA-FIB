package IA.Azamon.experiments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public abstract class RunnableExperiment implements IRunnableExperiment
{
    protected final Workbook workbook;
    private final String filename;

    protected final Sheet sheet;

    public RunnableExperiment(final Workbook workbook, final String title, final String filename)
    {
        this.filename = filename;
        this.workbook = workbook;
        this.sheet = workbook.createSheet(title);
    }

    public void save() throws IOException
    {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + filename.toLowerCase() + ".xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

    public ConcatenableExperiment andThen(final RunnableExperiment runnableExperiment)
    {
        return createConcat().andThen(runnableExperiment);
    }

    public ConcatenableExperiment createConcat()
    {
        return ConcatenableExperiment.create(this);
    }
}
