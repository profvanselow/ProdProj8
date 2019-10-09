package io.github.profvanselow;

import java.util.List;

// Step 3
// Create an abstract type called Product that will implement the Item interface.
public abstract class Product implements Item, Comparable<Product> {

  private int id;
  private String name;
  private ItemType type;
  private String manufacturer;

  // Takes in the name of the product and manufacturer and sets the fields.
  public Product(int id, String n, String m, ItemType type) {
    this.id = id;
    name = n;
    this.type = type;
    manufacturer = m;
  }

  // Takes in the name of the product and manufacturer and sets the fields.
  public Product(String n, String m, ItemType type) {
    this.id = 0;
    name = n;
    this.type = type;
    manufacturer = m;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public ItemType getType() {
    return type;
  }

  public void setType(ItemType type) {
    this.type = type;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String toString() {
    return "Name: " + name + "\n"
        + "Manufacturer: " + manufacturer + "\n"
        + "Type: " + type;
  }

  // Step 14
  @Override
  public int compareTo(Product o) {
    return name.compareTo(o.getName());
  }

  // Step 17
  // Create a static method called printType in Product that will iterate through your Collection
  // and print all the classes of a particular type.
  // Example – print only AudioPlayer classes in the collection.
  // For an extra bonus you could modify it so that it would accept the Class that you want to print
  // in the parameter list. This way we could use it against classes that we have not yet built.
  // Limit the collection to only use subclasses of Product.
  public static void printType(List<? extends Product> list, Class<?> cls) {
    for (Product listItem : list) {
      if (listItem.getClass().equals(cls)) {
        System.out.println(listItem);
      }
    }
  }
}

/*
A temporary class to test Product.
 */
class Widget extends Product {

  // for existing widget coming in from db
  public Widget(int id, String name, String manufacturer, ItemType type) {
    super(id, name, manufacturer, type);
  }

  // for added widget that will get id from db
  public Widget(String name, String manufacturer, ItemType type) {
    super(name, manufacturer, type);
  }
}