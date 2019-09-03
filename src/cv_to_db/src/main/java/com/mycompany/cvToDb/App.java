package com.mycompany.cvToDb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }
    /**
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        BlockingQueue<String[]> goodRecordQueue = new LinkedBlockingQueue<String[]>(5000);
        BlockingQueue<String[]> badRecordQueue = new LinkedBlockingQueue<String[]>(1000);
        Control control = new Control();
        String filename = "/mnt/c/Users/AxelE/Desktop/ms3_coding_challenge/doc/ms3Interview.csv";

        ProcessAll processAllRecord = new ProcessAll(filename, goodRecordQueue, badRecordQueue, control);
        ProcessGood processGoodRecord = new ProcessGood(filename, goodRecordQueue, control);
        ProcessBad processBadRecord = new ProcessBad(filename, badRecordQueue, control);

        Thread allRecordProcessingThread = new Thread(processAllRecord, "All Record");
        Thread goodRecordProcessingThread = new Thread(processGoodRecord, "Good Record");
        Thread badRecordProcessingThread = new Thread(processBadRecord, "Bad Record");

        allRecordProcessingThread.start();
        goodRecordProcessingThread.start();
        badRecordProcessingThread.start();

        try {
            allRecordProcessingThread.join();
            goodRecordProcessingThread.join();
            badRecordProcessingThread.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
