package com.mycompany.cvToDb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import com.opencsv.CSVReader;

public class ProcessAll implements Runnable {

    private BlockingQueue<String[]> goodRecordQueue;
    private BlockingQueue<String[]> badRecordQueue;
    private String filename;

    private long goodRecordProcessed = 0;
    private long badRecordProcessed = 0;
    private long receivedRecordProcessed = 0;

    private Control control = null;

    private final int GOODRECORDBOOL = 0x3FF;

    private final String PATHTOLOG = "." + File.separator + "output" + File.separator + "log" + File.separator;

    ProcessAll(String filename, BlockingQueue<String[]> goodRecord, BlockingQueue<String[]> BadRecord,
            Control control) {
        this.badRecordQueue = BadRecord;
        this.goodRecordQueue = goodRecord;
        this.filename = filename;
        this.control = control;
    }

    @Override
    public void run() {

        try {
            String name;
            processFile();
            name = getfileName(this.filename);
            writeLog(name);
        } catch (IOException e) {

            System.err.println(e.getMessage());
        }
    }

    /**
     * Write to a log the number of bad record, good record and the total processed
     * 
     * @param filename name of the file to write
     * @throws IOException
     * 
     */
    private void writeLog(String filename) throws IOException {

        String pathToFile = PATHTOLOG + filename + ".log";
        try {
            File file = new File(pathToFile);
            file.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            PrintWriter printWriter = new PrintWriter(writer);

            printWriter.printf("Record received: %d\n", receivedRecordProcessed);
            printWriter.printf("Good record received: %d\n", goodRecordProcessed);
            printWriter.printf("Bad record received: %d\n", badRecordProcessed);

            printWriter.close();

        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Finished processing write to log");
    }

    private void processFile() throws IOException {

        FileReader filereader = null;
        CSVReader csvReader = null;
        BufferedReader reader = null;

        int boolCheckRecord = 0x0;

        String[] nextRecord;

        filereader = new FileReader(filename);
        reader = new BufferedReader(filereader);

        csvReader = new CSVReader(reader);

        // we are going to read data line by line
        try {
            while ((nextRecord = csvReader.readNext()) != null && nextRecord.length != 0) {
                receivedRecordProcessed++;
                boolCheckRecord = 0x0000;
                
                if (checkNotEmpty(nextRecord[0]))
                    boolCheckRecord = boolCheckRecord | 0x0001;
                if (checkNotEmpty(nextRecord[1]))
                    boolCheckRecord = boolCheckRecord | 0x0002;
                if (checkNotEmpty(nextRecord[2]))
                    boolCheckRecord = boolCheckRecord | 0x0004;
                if (checkNotEmpty(nextRecord[3]))
                    boolCheckRecord = boolCheckRecord | 0x0008;
                if (checkNotEmpty(nextRecord[4]))
                    boolCheckRecord = boolCheckRecord | 0x0010;
                if (checkNotEmpty(nextRecord[5]))
                    boolCheckRecord = boolCheckRecord | 0x0020;
                if (checkNotEmpty(nextRecord[6]))
                    boolCheckRecord = boolCheckRecord | 0x0040;
                if (checkNotEmpty(nextRecord[7]))
                    boolCheckRecord = boolCheckRecord | 0x0080;
                if (checkNotEmpty(nextRecord[8]))
                    boolCheckRecord = boolCheckRecord | 0x0100;
                if (checkNotEmpty(nextRecord[9]))
                    boolCheckRecord = boolCheckRecord | 0x0200;

                if(receivedRecordProcessed == 6001)
                    System.out.println();
                try {

                    if (boolCheckRecord == GOODRECORDBOOL) {
                        goodRecordProcessed++;
                        goodRecordQueue.put(nextRecord);
                    } else {
                        badRecordProcessed++;
                        badRecordQueue.put(nextRecord);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
           // System.err.println(e.getClass().getName() + "\n" + e.getMessage());
           e.printStackTrace();
        } finally {

            if (csvReader != null)
                csvReader.close();
        }
        /* set finished processing to true */
        control.finishedProcessing = true;
        System.out.println("Finished processing record");
    }

    /**
     * 
     * @param data The string to check. return true is string is not equal ""
     */
    private boolean checkNotEmpty(String data) {
       
        return !data.isBlank();
    }

    /**
     * 
     * @param path The path to the file ""
     * 
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