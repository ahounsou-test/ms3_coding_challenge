package com.mycompany.cvToDb;

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

    private final int GOODRECORDBOOL = 0x3FF;

    ProcessAll(String filename, BlockingQueue<String[]> goodRecord, BlockingQueue<String[]> BadRecord) {
        this.badRecordQueue = BadRecord;
        this.goodRecordQueue = goodRecord;
        this.filename = filename;
    }

    @Override
    public void run() {

        try {
            String name;
            processFile();
            name = getfileName(this.filename);
            writeLog(name);
        } catch (IOException e) {

            e.printStackTrace();
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

        String path = "." + File.separator +  "output" + File.separator +" log" + File.separator;

        String pathToFile = path + filename + ".log";

        File file = new File(pathToFile);
        file.getParentFile().mkdirs();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.printf("Record received: %l\n", receivedRecordProcessed);
        printWriter.printf("Good record received: %l\n", goodRecordProcessed);
        printWriter.printf("Bad record received: %l\n", badRecordProcessed);

        printWriter.close();

    }

    private void processFile() throws IOException {

        FileReader filereader = null;
        CSVReader csvReader = null;

        int boolCheckRecord = 0x0;

        try {

            String[] nextRecord;

            filereader = new FileReader(filename);
            csvReader = new CSVReader(filereader);

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                receivedRecordProcessed++;

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
                if (checkNotEmpty(nextRecord[8]))
                    boolCheckRecord = boolCheckRecord | 0x0200;

                if (boolCheckRecord == GOODRECORDBOOL) {
                    goodRecordProcessed++;
                    goodRecordQueue.add(nextRecord);
                } else {
                    badRecordProcessed++;
                    badRecordQueue.add(nextRecord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (filereader != null)
                filereader.close();

            if (csvReader != null)
                csvReader.close();

        }
    }

    /**
     * 
     * @param data The string to check. return true is string is not equal ""
     */
    private boolean checkNotEmpty(String data) {

        return data != "";
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