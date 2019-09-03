package com.mycompany.cvToDb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import com.opencsv.CSVWriter;

public class ProcessBad implements Runnable {

    private BlockingQueue<DataContainer> badRecordQueue;
    private String filename;
    private final String PATHTOBADRECORD = "." + File.separator + "output" + File.separator + "bad" + File.separator;

    ProcessBad(String filename, BlockingQueue<DataContainer> BadRecord) {
        this.badRecordQueue = BadRecord;
        this.filename = getfileName(filename);
    }

    @Override
    public void run() {
        writeBadRecordToFile();
    }

    private void writeBadRecordToFile() {
        String pathToFile = PATHTOBADRECORD + filename + "-bad" + ".csv";
        File file = null;

        CSVWriter csvWriter = null;
        BufferedWriter writer = null;

        DataContainer record;

        boolean isPoison = false;

        try {

            file = new File(pathToFile);
            file.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(file));
            csvWriter = new CSVWriter(writer);

            while (!isPoison) {
                record = badRecordQueue.take();
                isPoison = record.isPoison();
             
                if (!isPoison)
                    csvWriter.writeNext(record.getData());
            }

            csvWriter.close();

        } catch (NullPointerException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * 
     * @param path The path to the file ""
     * @return the name of the file without extention
     */

    private static String getfileName(String path) {

        File userFile = new File(path);
        String filename = userFile.getName();

        int pos = filename.lastIndexOf(".");
        if (pos > 0) {
            filename = filename.substring(0, pos);
        }

        return filename;
    }

}