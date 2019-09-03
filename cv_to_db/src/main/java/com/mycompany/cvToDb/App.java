package com.mycompany.cvToDb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * csv to db 
 */
public final class App {
    private App() {
    }
    /**
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        BlockingQueue<DataContainer> goodRecordQueue = new ArrayBlockingQueue<DataContainer>(4000);
        BlockingQueue<DataContainer> badRecordQueue = new ArrayBlockingQueue<DataContainer>(2000);
        String filename = args[0];

        ProcessAll processAllRecord = new ProcessAll(filename, goodRecordQueue, badRecordQueue);
        ProcessGood processGoodRecord = new ProcessGood(filename, goodRecordQueue);
        ProcessBad processBadRecord = new ProcessBad(filename, badRecordQueue);

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
