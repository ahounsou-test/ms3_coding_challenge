package com.mycompany.cvToDb;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class ProcessGood implements Runnable {

    private BlockingQueue<DataContainer> goodRecordQueue;
    private SQLiteJDBC db;
    private String filename;

    ProcessGood(String filename, BlockingQueue<DataContainer> goodRecord) {
        this.goodRecordQueue = goodRecord;
        this.filename = getfileName(filename);
        this.db = new SQLiteJDBC(this.filename);
    }

    @Override
    public void run() {
        db.connect();
        db.createTable(filename);
        db.prepareForTable(filename);
        writeDataToDb();
    }

    private void writeDataToDb() {

        DataContainer record;
        boolean isPoison = false;

        try {
            /* Get rid of header */
            if (!goodRecordQueue.isEmpty())
                goodRecordQueue.take();

            while (!isPoison) {
                record = goodRecordQueue.take();
                isPoison = record.isPoison();

                if (!isPoison)
                    db.batchInsertion(record.getData(), 1000);
            }

            db.flushBatch();
            db.closeConn();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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