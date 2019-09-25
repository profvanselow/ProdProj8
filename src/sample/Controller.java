package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;


public class Controller {

  final String JDBC_DRIVER = "org.h2.Driver";
  final String DB_URL = "jdbc:h2:./res/ProdDB";

  //  Database credentials
  final String USER = "";
  final String PASS = "";
  Connection conn = null;
  Statement stmt = null;

  @FXML
  private Button btnAddProduct;

  @FXML
  private Button btnRecordProduction;

  @FXML
  private ComboBox<String> cbQuantity;
  // although the combobox contains integers it becomes a textbox that returns a string once made editable
  // so it was easiest just to make it String

  @FXML
  void recordProduce(ActionEvent event) {
    System.out.println(cbQuantity.getValue());
  }

  @FXML
  void addProduct(ActionEvent event) {
    //System.out.println("in addProduct");
    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 2: Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      //STEP 3: Execute a query
      stmt = conn.createStatement();

      String sql = "INSERT INTO Product(type, manufacturer, name) VALUES ( 'AUDIO', 'Apple', 'iPod2' );";

      stmt.executeUpdate(sql);

      // STEP 4: Clean-up environment
      stmt.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void initialize() {

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

    initializeDB();
  }

  private void initializeDB() {
    System.out.println("in initializeDB");
    
  }

  // not seeing this called
  public void close() {
    System.out.println("close called");
  }

}
