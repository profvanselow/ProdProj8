package io.github.profvanselow;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductionTabsController {

  private Connection conn = null;
  private Statement stmt = null;

  private ArrayList<Product> productLine = new ArrayList<>();
  private ObservableList<Product> oProductLine;

  private ArrayList<ProductionRecord> productionLog = new ArrayList<>();

  @FXML
  private TextField txtProductName;

  @FXML
  private TextField txtManufacturer;

  @FXML
  private Button btnAddProduct;

  @FXML
  private Button btnRecordProduction;

  @FXML
  private TextArea taProductLine;

  @FXML
  private TextArea taViewProd;

  @FXML
  private ComboBox<String> cbQuantity;
  // although the combobox contains integers it becomes a textbox that returns a string once made
  // editable so it was easiest just to make it String

  @FXML
  private ChoiceBox<ItemType> cbType;

  @FXML
  private TableView<Product> tvProductLine;

  @FXML
  private ListView<Product> lvProductLine;


  public void initialize() {

    // test classes
    playerDriver();

    // populate quantity combobox
    for (int i = 1; i <= 10; i++) {
      cbQuantity.getItems().add(String.valueOf(i));
    }
    // make it possible for user to enter a different value
    cbQuantity.setEditable(true);
    // set the default value
    cbQuantity.getSelectionModel().selectFirst();

    // populate type choicebox
    for (ItemType type : ItemType.values()) {
      cbType.getItems().add(type);
    }
    // OR cbType.getItems().addAll(ItemType.values());
    // set the default value
    cbType.getSelectionModel().selectFirst();

    // connect to database
    initializeDB();

    // Convert the productLine ArrayList to an ObservableList to use with the TableView
    oProductLine = FXCollections.observableArrayList(productLine); // using ArrayList

    setupProductLineTable();
    lvProductLine.setItems(oProductLine);
    loadProductList();
    loadProductionLog();
  }

  private void initializeDB() {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/ProdDB";

    //  Database credentials
    final String USER = "";
    final String PASS = "dbpw";

    System.out.println("Attempting to connect to database");
    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 2: Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      //STEP 3: Execute a query
      stmt = conn.createStatement();
      System.out.println("Successfully connected to database!");
    } catch (Exception e) {
      e.printStackTrace();

      Alert a = new Alert(Alert.AlertType.ERROR);

      // show the dialog
      a.show();
    }

  }

  private static void playerDriver() {
    AudioPlayer newAudioProduct = new AudioPlayer("DP-X1A", "Onkyo",
        "DSD/FLAC/ALAC/WAV/AIFF/MQA/Ogg-Vorbis/MP3/AAC", "M3U/PLS/WPL");
    //Screen newScreen = new Screen("720x480", 40, 22);
    MoviePlayer newMovieProduct = new MoviePlayer("DBPOWER MK101", "OracleProduction",
        new Screen("720x480", 40, 22),
        MonitorType.LCD);
    ArrayList<MultimediaControl> productList = new ArrayList<>();
    productList.add(newAudioProduct);
    productList.add(newMovieProduct);
    for (MultimediaControl p : productList) {
      System.out.println(p);
      p.play();
      p.stop();
      p.next();
      p.previous();
    }
  }

  //<editor-fold desc="Product">
  // Product **************************************************************************************
  @FXML
  void addProduct(ActionEvent event) {

    System.out.println("Adding Product");
    //String pType = cbType.getValue().toString();
    ItemType iType = cbType.getValue();
    String pMan = txtManufacturer.getText();
    String pName = txtProductName.getText();
    Product newProduct = new Widget(pName, pMan, iType);
    try {

      final String sql = "INSERT INTO Product(type, manufacturer, name) VALUES (?, ?, ?)";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, iType.code);
      ps.setString(2, pMan);
      ps.setString(3, pName);

      ps.executeUpdate();
      System.out.println("Product added to database.");
      taProductLine.appendText(newProduct.toString() + "\n");
      loadProductList();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void setupProductLineTable() {

    TableColumn productIdCol = new TableColumn("Product ID");
    productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Product, String> productNameCol = new TableColumn<>("Product Name");
    productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

    // proper gets required for this to work
    // http://tutorials.jenkov.com/javafx/tableview.html
    TableColumn<Product, String> manufacturerCol = new TableColumn<>("Manufacturer");
    manufacturerCol.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

    TableColumn<Product, String> itemTypeCol = new TableColumn<>("Item Type");
    itemTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

    tvProductLine.setItems(oProductLine);
    tvProductLine.getColumns().addAll(productIdCol, productNameCol, manufacturerCol, itemTypeCol);

  }

  // not seeing this called
  public void stop() {
    System.out.println("stop called");
  }

  private void loadProductList() {

    try {

      String sql = "SELECT id, name, type, manufacturer FROM Product";
      ResultSet rs = stmt.executeQuery(sql);

      oProductLine.clear();

      // STEP 4: Extract data from result set
      while (rs.next()) {
        // Retrieve by column name
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String typeCode = rs.getString("type");
        ItemType type;
        switch (typeCode) {
          case "AU":
            type = ItemType.AUDIO;
            break;
          case "VI":
            type = ItemType.VISUAL;
            break;
          case "AM":
            type = ItemType.AUDIO_MOBILE;
            break;
          case "VM":
            type = ItemType.VISUAL_MOBILE;
            break;
          default:
            type = ItemType.AUDIO;
            System.out.println("Bad Type");
        }
        String manufacturer = rs.getString("manufacturer");

        Product addedProduct = new Widget(id, name, manufacturer, type);
        productLine.add(addedProduct);
        oProductLine.add(addedProduct);
        // taProductLine.appendText(addedProduct.toString() + "\n"); // for TextArea instead

      }
      // STEP 5: Clean-up environment
      rs.close();
    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();

      Alert a = new Alert(Alert.AlertType.ERROR);

      // show the dialog
      a.show();

    } catch (Exception e) {
      // Handle errors for Class.forName
      e.printStackTrace();
    }

  }
  //</editor-fold>

  //<editor-fold desc="Production">
  // Production *************************************************************************************
  @FXML
  void recordProduction(ActionEvent event) {
    System.out.println("in recordProduction");
    Product selectedProduct = lvProductLine.getSelectionModel().getSelectedItem();

    Integer numProduced = Integer.parseInt(cbQuantity.getValue());

    // create collection of produced items
    // don't want to add to production log yet so database can assign production number
    ArrayList<ProductionRecord> productionRun = new ArrayList<>();
    for (int productionRunProduct = 0; productionRunProduct < numProduced; productionRunProduct++) {
      ProductionRecord pr = new ProductionRecord(selectedProduct.getId());
      //productionLog.add(pr);
      productionRun.add(pr);
    }

    // send collection to database and append newly produced items to observable list to update interface
    addToProductionDB(productionRun);
    loadProductionLog();
    showProduction();

  }

  // Loops through the production log building strings and adding to text view
  private void showProduction() {
    //System.out.println("in showProduction");

    StringBuilder fieldContent = new StringBuilder();

    String productName = "";
    int id;

    for (ProductionRecord pr : productionLog) {
      //System.out.println(pr.getRec());

      id = pr.getProductID();

      for (Product product : productLine) {
        if (product.getId() == id) {
          productName = product.getName();
          break;
        }
      }

      fieldContent.append("Production Num: ").append(pr.getProductionNum())
          .append(" Product Name: ")
          .append(productName).append(" Serial Num: ").append(pr.getSerialNum()).append(" Date: ").
          append(pr.getProdDate()).append("\n");

    }
    taViewProd.setText(fieldContent.toString());
  }

  // writes a production run to the production record table of the database
  private void addToProductionDB(ArrayList<ProductionRecord> productionRun) {

    try {

      //STEP 3: Execute a query
      System.out.println("Inserting production records in database.");
      ProductionRecord pr;
      Timestamp ts;

      //TODO: make one big insert string so there is only one SQL statement to execute
      for (int productionRunProduct = 0; productionRunProduct < productionRun.size();
          productionRunProduct++) {
        pr = productionRun.get(productionRunProduct);

        ts = new Timestamp(pr.getProdDate().getTime());

        String sql = "INSERT INTO PRODUCTIONRECORD (PRODUCT_ID, SERIAL_NUM, DATE_PRODUCED)" +
            " VALUES (" +
            pr.getProductID() + ", " +
            pr.getSerialNum() + ", '" +
            ts + "')";

        stmt.executeUpdate(sql);
        System.out.println("Record inserted in database.");

      }
      System.out.println("Completed inserting production records!");
    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();

      Alert a = new Alert(Alert.AlertType.ERROR);

      // show the dialog
      a.show();

    } catch (Exception e) {
      // Handle errors for Class.forName
      e.printStackTrace();
    }

  }

  // reads the production record database table to build the production log
  private void loadProductionLog() {
    System.out.println("loadProductionLog!");

    try {

      // to use just ProductionRecord table
      String sql = "SELECT * FROM PRODUCTIONRECORD";

      // to join tables to show product name instead of id
      //String sql = "SELECT PRODUCTIONRECORD.PRODUCTION_NUM, PRODUCTIONRECORD.product_id, PRODUCT.NAME, " +
      //        "PRODUCTIONRECORD.SERIAL_NUM, PRODUCTIONRECORD.DATE_PRODUCED " +
      //        "FROM PRODUCTIONRECORD " +
      //        "INNER JOIN PRODUCT ON PRODUCTIONRECORD.PRODUCT_ID=PRODUCT.ID";

      ResultSet rs = stmt.executeQuery(sql);

      productionLog.clear();

      // STEP 4: Extract data from result set
      while (rs.next()) {
        // Retrieve by column name
        int prodNum = rs.getInt("production_num");
        // for pr
        int id = rs.getInt("product_id");
        // for display
        //String name = rs.getString("name");
        String serial = rs.getString("serial_num");
        Date prodDate = rs.getDate("date_produced");

        ProductionRecord pr = new ProductionRecord(prodNum, id, serial, prodDate);
        productionLog.add(pr);
        //System.out.println(addedProduct);
        //tvProductLine.getItems().add(addedProduct);
        // Display values
        //System.out.print("prodNum: " + prodNum);
        //System.out.print(", id: " + id);
        //System.out.print(", name: " + name);
        //System.out.print(", serial: " + serial);
        //System.out.println(", prodDate: " + prodDate);
      }
      // STEP 5: Clean-up environment
      rs.close();
      showProduction();
    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();

      Alert a = new Alert(Alert.AlertType.ERROR);

      // show the dialog
      a.show();

    } catch (Exception e) {
      // Handle errors for Class.forName
      e.printStackTrace();
    }
  }
  //</editor-fold>
}

