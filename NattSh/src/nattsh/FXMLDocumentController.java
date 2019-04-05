/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nattsh;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Students
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView tableView;
    
    private ObservableList<Person> data = FXCollections.observableArrayList();
    
    final String JDBC_DRIVER = "org.sqlite.JDBC";  
    final String DB_URL = "jdbc:sqlite:profile.db";

   //  Database credentials
    final String USER = "username";
    static final String PASS = "password";

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS profile('id'integer primary key autoincrement, 'name'text, 'email'text, 'phone'text)";

    private final String write = "insert into profile(name, email, phone) values(?,?,?)";

    Connection conn = null;
    Statement stmt = null;
    
    //private TableView<Person> table = new TableView<Person>();
    
   
    public static void main(String[] args) {
        launch(args);
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            connectdb();
            
            
            
            tableView.setEditable(true);
            for(int p = 0; p <20; p++){
                
           // writedb("name"+p, "last"+p, "email"+p);
            
            }
            readdb();
            
            TableColumn firstNameCol = new TableColumn("First Name");
            firstNameCol.setMinWidth(100);
            firstNameCol.setCellValueFactory(
                    new PropertyValueFactory<Person, String>("firstName"));
            
            TableColumn lastNameCol = new TableColumn("Last Name");
            lastNameCol.setMinWidth(100);
            lastNameCol.setCellValueFactory(
                    new PropertyValueFactory<Person, String>("lastName"));
            
            TableColumn emailCol = new TableColumn("Email");
            emailCol.setMinWidth(200);
            emailCol.setCellValueFactory(
                    new PropertyValueFactory<Person, String>("email"));
            
            tableView.setItems(data);
            tableView.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         public static class Person {
 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
 
        private Person(String fName, String lName, String email) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
        }
 
        public String getFirstName() {
            return firstName.get();
        }
 
        public void setFirstName(String fName) {
            firstName.set(fName);
        }
 
        public String getLastName() {
            return lastName.get();
        }
 
        public void setLastName(String fName) {
            lastName.set(fName);
        }
 
        public String getEmail() {
            return email.get();
        }
 
        public void setEmail(String fName) {
            email.set(fName);
        }
    }
         public void readdb() throws SQLException{
             
      String sql;
      sql = "SELECT * FROM profile";    
      
      ResultSet rs = stmt.executeQuery(sql);
      
      

      //STEP 5: Extract data from result set
     
      while(rs.next()){
         //Retrieve by column name
         Person p = new Person(rs.getString("name"),rs.getString("phone"),rs.getString("email"));
         int id  = rs.getInt("id");
         //p.setEmail(rs.getString("email"));
         //p.setFirstName(rs.getString("name"));
        // p.setLastName(rs.getString("phone"));
         data.addAll(p);
        
      }
     
      
      rs.close();
      stmt.close();
   }
   
    public void writedb (String name, String last, String email) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(write);
      ps.setString(1, name);
       ps.setString(2, last);
        ps.setString(3, email);
     // ps.setInt(2, Integer.parseInt(enteremail.getText()));
      ps.executeUpdate();
      ps.close();
   }
   
    public void updatedb(int id,String name, int age) throws SQLException {
        String update = "update profile set name='"+name+"',age="+age+" where id="+id;
        PreparedStatement ps = conn.prepareStatement(update);
      ps.executeUpdate();
      ps.close();
   }
    
    public void deletedb() throws SQLException {
        String update = "delete from profile";
        PreparedStatement ps = conn.prepareStatement(update);
      ps.executeUpdate();
      ps.close();
   }
   
    public void connectdb () throws ClassNotFoundException, SQLException{
       
           //STEP 2: Register JDBC driver
           Class.forName("org.sqlite.JDBC");
             //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL/*,USER,PASS*/);
      
      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement(); 
      
      stmt.executeUpdate(CREATE_TABLE);
      
   }
}