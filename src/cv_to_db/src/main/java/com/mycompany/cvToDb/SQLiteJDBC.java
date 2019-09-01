package com.mycompany.cvToDb;

import java.sql.*;

public class SQLiteJDBC {

    Connection c = null;

    SQLiteJDBC(){
       
    }
    
    public static void main( String args[] ) {
     
        
        try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
        System.out.println("Opened database successfully");
     }
  }