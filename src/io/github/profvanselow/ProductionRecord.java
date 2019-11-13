package io.github.profvanselow;

import java.util.Date;

public class ProductionRecord {

  private int productionNum;
  private int productID;
  private String serialNum;
  private Date prodDate;

  public Date getProdDate() {
    return new Date(prodDate.getTime());
  }

  public void setProdDate(Date prodDate) {
    this.prodDate = new Date(prodDate.getTime());
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

  // temporary constructor used when adding from UI that sets serial num to 0
  public ProductionRecord(int productID) {
    this.productionNum = 0; // set to 0 temporarily, allow database to autoincrement
    this.productID = productID;
    serialNum = "0";
    prodDate = new Date();
  }

  // enhanced constructor used when adding from UI that builds serial num
  public ProductionRecord(Product product, int typeCount) {
    this.productionNum = 0; // set to 0 temporarily, allow database to autoincrement
    this.productID = product.getId();
    // Sets the serialNumber to start with the first three letters of the Manufacturer name,
    // then the two letter ItemType code,
    // then five digits (with leading 0s if necessary) that are unique and sequential for the item type.
    serialNum = product.getManufacturer().substring(0, 3) + product.getType().code +
        String.format("%05d", typeCount);
    prodDate = new Date();
  }

  // constructor used when populating from database
  public ProductionRecord(int prodNum, int productID, String sn, Date date) {
    this.productionNum = prodNum;
    this.productID = productID;
    serialNum = sn;
    prodDate = new Date(date.getTime());
  }

  public String toString() {
    return "Prod. Num: " + productionNum + " Product ID: " + productID + " Serial Num: "
        + serialNum + " Date: " + prodDate;
  }
}