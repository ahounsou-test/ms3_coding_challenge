package com.mycompany.cvToDb;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class ProcessGood implements Runnable {

    private BlockingQueue<String[]> goodRecordQueue;
    private SQLiteJDBC db;
    private String filename;
    private Control control = null;

    ProcessGood(String filename, BlockingQueue<String[]> goodRecord, Control control) {
        this.goodRecordQueue = goodRecord;
        this.filename = getfileName(filename);
        this.db = new SQLiteJDBC(this.filename);
        this.control = control;
    }

    @Override
    public void run() {
        db.connect();
        db.createTable(filename);
        writeDataToDb();
    }

    private void writeDataToDb() {

        String[] record;
        /* Get rid of header */
        try {
            if (!goodRecordQueue.isEmpty())
                goodRecordQueue.take();

            while (!goodRecordQueue.isEmpty() || !control.finishedProcessing) {
                record = goodRecordQueue.take();
                db.addToDB(filename, record);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Finished processing good record");
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