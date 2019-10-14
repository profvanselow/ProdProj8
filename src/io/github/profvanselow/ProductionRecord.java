package io.github.profvanselow;

import java.util.Date;

public class ProductionRecord {

  private int productionNum;
  private int productID;
  private String serialNum;
  private Date prodDate;

  public Date getProdDate() {
    return prodDate;
  }

  public void setProdDate(Date prodDate) {
    this.prodDate = prodDate;
  }

  public int getProductionNum() {
    return productionNum;
  }

  public void setProductionNum(int productionNum) {
    this.productionNum = productionNum;
  }

  public int getProductID() {
    return productID;
  }

  public void setProductID(int productID) {
    this.productID = productID;
  }

  public String getSerialNum() {
    return serialNum;
  }

  public void setSerialNum(String serialNum) {
    this.serialNum = serialNum;
  }

  // constructor used when adding from UI
  public ProductionRecord(int productID) {
    this.productionNum = 0; // set to 0 temporarily, allow database to autoincrement
    this.productID = productID;
    serialNum = "0"; // TODO: generate unique serial number by product

    prodDate = new Date();
  }

  // constructor used when populating from database
  public ProductionRecord(int prodNum, int productID, String sn, Date date) {
    this.productionNum = prodNum;
    this.productID = productID;
    serialNum = sn;
    prodDate = date;
  }

  public String toString() {
    return "Production Num: " + productionNum + " Product ID: " + productID + " Serial Num: "
        + serialNum +
        " Date: " + prodDate;
  }
}