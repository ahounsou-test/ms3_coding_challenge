package com.mycompany.cvToDb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import com.opencsv.CSVWriter;

public class ProcessBad implements Runnable {

    private BlockingQueue<String[]> badRecordQueue;
    private String filename;

    private Control control = null;

    private final String PATHTOBADRECORD = "." + File.separator + "output" + File.separator + "bad" + File.separator;

    ProcessBad(String filename, BlockingQueue<String[]> BadRecord, Control control) {
        this.badRecordQueue = BadRecord;
        this.filename = getfileName(filename);
        this.control = control;
    }

    @Override
    public void run() {
        try {
            writeBadRecordToFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void writeBadRecordToFile() throws IOException {
        String pathToFile = PATHTOBADRECORD + filename + "-bad" + ".csv";
        File file = null;

        CSVWriter csvWriter = null;
        BufferedWriter writer = null;

        String[] record;
        try {

            file = new File(pathToFile);
            file.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(file));
            csvWriter = new CSVWriter(writer);
            try {
                while (!badRecordQueue.isEmpty() || !control.finishedProcessing) {
                    record = badRecordQueue.take();
                    csvWriter.writeNext(record);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (NullPointerException | IOException e) {
            System.err.println(e.getMessage());
        } finally {

            if (csvWriter != null)
                csvWriter.close();
        }
        System.out.println("Finished processing bad record");
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