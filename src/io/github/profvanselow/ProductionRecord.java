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

  /**
   * Temporary constructor used when adding from UI that sets serial num to 0.
   *
   * @param productID The id of the Product selected.
   */
  public ProductionRecord(int productID) {
    this.productionNum = 0; // set to 0 temporarily, allow database to autoincrement
    this.productID = productID;
    serialNum = "0";
    prodDate = new Date();
  }

  /**
   * Enhanced constructor used when adding from UI that builds serial num.
   *
   * @param product The selected product.
   * @param typeCount The count of the type of that product, used for building serial number.
   */
  public ProductionRecord(Product product, int typeCount) {
    this.productionNum = 0; // set to 0 temporarily, allow database to autoincrement
    this.productID = product.getId();
    // Sets the serialNumber to start with the first three letters of the Manufacturer name,
    // then the two letter ItemType code,
    // then five digits (with leading 0s if necessary) that are unique and sequential for the item
    // type.
    serialNum = product.getManufacturer().substring(0, 3) + product.getType().code
        + String.format("%05d", typeCount);
    prodDate = new Date();
  }

  /**
   * Constructor used when populating from database.
   *
   * @param prodNum Product number.
   * @param productID Product id.
   * @param sn Serial number.
   * @param date Date.
   */
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