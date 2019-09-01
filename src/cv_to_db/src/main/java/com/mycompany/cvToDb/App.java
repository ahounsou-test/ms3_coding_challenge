package com.mycompany.cvToDb;

import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

import java.sql.*;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        try {
            readDataLineByLine("/home/axel/Desktop/ms3_coding_challenge/doc/ms3Interview.csv");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readDataLineByLine(String file) throws IOException {

        FileReader filereader = null;
        CSVReader csvReader = null;

        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            filereader = new FileReader(file);

            // create csvReader object passing
            // file reader as a parameter
            csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                // for (String cell : nextRecord) {
                System.out.print(nextRecord[0]);
                // }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (filereader != null)
                filereader.close();

            if (csvReader != null)
                csvReader.close();

        }
    }

    public class SQLiteJDBC {

        Connection c = null;

        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
        }catch(
        Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }System.out.println("Opened database successfully");
    }
}}
