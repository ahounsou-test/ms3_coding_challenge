package com.mycompany.cvToDb;

import java.io.File;
import java.sql.*;

public class SQLiteJDBC {

   private Connection conn = null;
   private final String PATHTODB = "." + File.separator + "db" + File.separator;
   private String dbname;
   private PreparedStatement pstmt;
   private int batchUpperLimit = 1;
   private int currentBatch = 0;

   SQLiteJDBC(String dbname) {

      this.dbname = dbname;
   }

   public void connect() {

      try {

         String dbPath = PATHTODB + dbname + ".db";
         Class.forName("org.sqlite.JDBC");

         File file = new File(dbPath);
         file.getParentFile().mkdirs();
         conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

         conn.setAutoCommit(false);

      } catch (SQLException | ClassNotFoundException e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
   }

   public Boolean createTable(String tablename) {

      String sql = "CREATE TABLE IF NOT EXISTS " + tablename + "(\n" + "    id integer PRIMARY KEY,\n"
            + "    A text NOT NULL,\n" + "    B text NOT NULL,\n" + "    C text NOT NULL,\n" + "    D text NOT NULL,\n"
            + "    E Blob NOT NULL,\n" + "    F text NOT NULL,\n" + "    G text NOT NULL,\n" + "    H text NOT NULL,\n"
            + "    I text NOT NULL,\n" + "    J text NOT NULL\n" + ");";

      try (Statement stmt = conn.createStatement()) {
         // create a new table
         stmt.execute(sql);
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return false;
      }
      return true;
   }

   /**
    * Create a prepare statement for batch insertion into database
    and set auto commit to false
    * 
    * @param tablename name of table
    * 
    */
   public void prepareForTable(String tablename) {
      String sql = "INSERT INTO " + tablename + " (A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";
      try {
         pstmt = conn.prepareStatement(sql);

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void batchInsertion(String[] data, int limit) {

      if (batchUpperLimit < limit)
         batchUpperLimit = limit;

      try {
         pstmt.setString(1,  data[0]);
         pstmt.setString(2,  data[1]);
         pstmt.setString(3,  data[2]);
         pstmt.setString(4,  data[3]);
         pstmt.setString(5,  data[4]);
         pstmt.setString(6,  data[5]);
         pstmt.setString(7,  data[6]);
         pstmt.setString(8,  data[7]);
         pstmt.setString(9,  data[8]);
         pstmt.setString(10, data[9]);

         pstmt.addBatch();

         currentBatch++;

         /* add to db if bactch upper limit is reach and reset currentBatch */
         if (currentBatch == batchUpperLimit) {
            pstmt.executeBatch();
            conn.commit();
            currentBatch = 0;
         }

      } catch (SQLException e) {
         System.err.println(e.getMessage());
    
      }
   }

   /**
    * Insert the rest of the data that 
      are still in the batch
    */
   public void flushBatch(){

      try {
         pstmt.executeBatch();
         conn.commit();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
   }

   public void closeConn(){
      try {
         conn.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}