package com.mycompany.cvToDb;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class ProcessGood implements Runnable {

    private BlockingQueue<String[]> goodRecordQueue;
    private SQLiteJDBC db ; 
    private boolean tableCreated = false;
    private String filename;
    
    ProcessGood(String filename, BlockingQueue<String[]> goodRecord) {
        this.goodRecordQueue = goodRecord;
        this.filename = getfileName(filename);
        this.db = new SQLiteJDBC(filename);
    }
    
    @Override
    public void run() {
        db.connect();
        db.createTable(filename);
        writeDataToDb();
    }

    private void writeDataToDb(){

        String[] record;
        /* Get rid of header */
        if (!goodRecordQueue.isEmpty())
            goodRecordQueue.remove();

        while( !goodRecordQueue.isEmpty()) {
            record = goodRecordQueue.remove();
            db.addToDB(filename, record);
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