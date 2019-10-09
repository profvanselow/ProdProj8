package io.github.profvanselow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
  private ObservableList<Product> oProductList;

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
  private ComboBox<String> cbQuantity;
  // although the combobox contains integers it becomes a textbox that returns a string once made editable
  // so it was easiest just to make it String

  @FXML
  private ChoiceBox<ItemType> cbType;
  // although the combobox contains integers it becomes a textbox that returns a string once made editable
  // so it was easiest just to make it String

  @FXML
  private TableView<Product> tvProductLine;

  @FXML
  private ListView<Product> lvProductLine;

  @FXML
  void recordProduce(ActionEvent event) {
    System.out.println(cbQuantity.getValue());
  }

  @FXML
  void addProduct(ActionEvent event) {

    //System.out.println("in addProduct");
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
      taProductLine.appendText(newProduct.toString() + "\n");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void initialize() {

    playerDriver();

    cbQuantity.getItems().add("1");
    cbQuantity.getItems().add("2");
    cbQuantity.getItems().add("3");
    cbQuantity.getItems().add("4");
    cbQuantity.getItems().add("5");
    cbQuantity.getItems().add("6");
    cbQuantity.getItems().add("7");
    cbQuantity.getItems().add("8");
    cbQuantity.getItems().add("9");
    cbQuantity.getItems().add("10");

    cbQuantity.setEditable(true);
    cbQuantity.getSelectionModel().selectFirst();
    System.out.println(cbQuantity.getValue());
    // better add https://stackoverflow.com/questions/47878484/generate-list-of-int-to-populate-combobox-in-javafx

    for (ItemType type : ItemType.values()) {
      cbType.getItems().add(type);
    }
    // OR cbType.getItems().addAll(ItemType.values());
    cbType.getSelectionModel().selectFirst();

    initializeDB();

    // Convert the productLine ArrayList to an ObservableList to use with the TableView
    oProductList = FXCollections.observableArrayList(productLine); // using ArrayList

    setupProductLineTable();
    lvProductLine.setItems(oProductList);
    loadProductList();
  }

  private void initializeDB() {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/ProdDB";

    //  Database credentials
    final String USER = "";
    final String PASS = "dbpw";

    System.out.println("in initializeDB");
    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 2: Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      //STEP 3: Execute a query
      stmt = conn.createStatement();
      System.out.println("Successful connection to database!");
    } catch (Exception e) {
      e.printStackTrace();

      Alert a = new Alert(Alert.AlertType.ERROR);

      // show the dialog
      a.show();
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

    tvProductLine.setItems(oProductList);
    tvProductLine.getColumns().addAll(productIdCol, productNameCol, manufacturerCol, itemTypeCol);

  }

  // not seeing this called
  public void close() {
    System.out.println("close called");
  }

  private void loadProductList() {

    try {

      String sql = "SELECT id, name, type, manufacturer FROM Product";
      ResultSet rs = stmt.executeQuery(sql);

      oProductList.clear();

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
        oProductList.add(addedProduct);
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
}
