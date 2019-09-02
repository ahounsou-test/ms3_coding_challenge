package com.mycompany.cvToDb;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {



        try {
            processFile("/mnt/c/Users/AxelE/Desktop/ms3_coding_challenge/doc/ms3Interview.csv");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @param data The string to check. return true is string is not equal ""
     */
    public boolean checkNotEmpty(String data) {

        return data != "";
    }

    /**
     * 
     * @param file The path or name of csv file to check.
     * 
     *             read csv file and save data in a db name <csvfilename>.db
     */
    public static void processFile(String file) throws IOException {

        FileReader filereader = null;
        CSVReader csvReader = null;
        String filename; 
        SQLiteJDBC db;
        long goodRecord = 0;
        long badRecord = 0;
        long receivedRecord = 0;

        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            filereader = new FileReader(file);
            filename = getfileName(file);

            // create csvReader object passing
            // file reader as a parameter
            csvReader = new CSVReader(filereader);

            db = new SQLiteJDBC();
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                receivedRecord++;
                System.out.print(receivedRecord);
                System.out.print(" ");
                System.out.print(nextRecord[0]);
                System.out.print(" ");
                System.out.print(nextRecord[1]);
                System.out.print(" ");
                System.out.print(nextRecord[2]);
                System.out.print(" ");
                System.out.print(" ");
                System.out.print(nextRecord[3]);
                System.out.print(" ");
                System.out.print(nextRecord[4]);
                System.out.print(" ");
                System.out.print(nextRecord[5]);
                System.out.print(" ");
                System.out.print(nextRecord[6]);
                System.out.print(" ");
                System.out.print(nextRecord[7]);
                System.out.print(" ");
                System.out.print(nextRecord[8]);
                System.out.print(" ");
                System.out.print(nextRecord[9]);
                System.out.println();

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
     * @param path The path to the file ""
     * 
     * @return   the  name of the file without extention
     */

    public static String getfileName(String path) {

        File userFile = new File(path);
        String filename = userFile.getName();

        int pos = filename.lastIndexOf(".");
        if (pos > 0) {
            filename = filename.substring(0, pos);
        }

        return filename;
    }
}
