package com.mycompany.cvToDb;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteJDBC {

   Connection conn = null;

   SQLiteJDBC() {

      try {
         Class.forName("org.sqlite.JDBC");
         conn = DriverManager.getConnection("jdbc:sqlite:test.db");
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
      System.out.println("Opened database successfully");

   }

   public Boolean createTable(String tablename) {

      String sql = "CREATE TABLE IF NOT EXISTS " + tablename + "(\n" + "    id integer PRIMARY KEY,\n"
            + "    A text NOT NULL,\n" + "    B text NOT NULL,\n" + "    C text NOT NULL,\n" + "    D text NOT NULL,\n"
            + "    E Blob NOT NULL,\n" + "    F text NOT NULL,\n" + "    G text NOT NULL,\n" + "    H text NOT NULL,\n"
            + "    I text NOT NULL,\n" + "    J text NOT NULL,\n" + ");";

      try (Statement stmt = conn.createStatement()) {
         // create a new table
         stmt.execute(sql);
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return false;
      }
      return true;
   }

   public Boolean addToDB(String tablename,  String[] data) {

      String sql = "INSERT INTO " + tablename + " (A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";

      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, data[0]);
         pstmt.setString(2, data[1]);
         pstmt.setString(3, data[2]);
         pstmt.setString(4, data[3]);
         pstmt.setString(5, data[4]);
         pstmt.setString(6, data[5]);
         pstmt.setString(7, data[6]);
         pstmt.setString(8, data[7]);
         pstmt.setString(9, data[8]);
         pstmt.setString(10, data[9]);

         pstmt.executeUpdate();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return false;
      }
      return true;

   }
}