package com.mycompany.cvToDb;

public class DataContainer {
  private boolean poison;
  private String[] data;

  /**
   * 
   * @param data The data to stroe
   * @param poison bool indicating if it should be use as poison
   */
  DataContainer(String[] data, boolean poison) {
    this.data = data;
    this.poison = poison;
  }

  /**
   * 
   * @return  the Data
   */
  String[] getData() {
    return data;
  }

  /**
   * 
   * @return boolean describing data as poison or not
   */
  boolean isPoison() {
    return poison;
  }

}