/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Phil
 */
public class FXMLViewPageController implements Initializable {

    @FXML
    private ListView tasks_listview;
   
    @FXML
    private Button complete_button;
    
    @FXML
    private MenuButton sort_menubutton;
    
    @FXML
    private MenuButton ascdesc_menubutton;
    
    String orderby;
    String ascdesc;
    ObservableList items;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshList();
    }   
    private void refreshList(){
        //Set items equal to an empty ArrayList
        items = FXCollections.observableArrayList ();
        
        //Select out of the DB, fill accordingly
        getTasks(items);
        
        //Set the listview to what we just populated with DB contents
        tasks_listview.setItems(items);
    }
    /* Queries for tasks as per the parameters passed in */
    private void getTasks(ObservableList items){
        Connection c = null;
        Statement stmt = null;
        
        orderby = sort_menubutton.getText();
        ascdesc = ascdesc_menubutton.getText();
        
        try {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:first.db");
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");
        
        if (orderby.equals("Time")) { orderby = "TIMING";}
        else if (orderby.equals("Title")) { orderby = "TITLE";}
        else { orderby = "LOCATION";}
        
        System.out.println("Query is: SELECT * FROM Tasks" + " ORDER BY "  + orderby + " COLLATE NOCASE " + ascdesc);
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Tasks" + " ORDER BY " + orderby + " COLLATE NOCASE " + ascdesc);
        while ( rs.next() ) {
            String title = rs.getString("TITLE");
            String location = rs.getString("LOCATION");
            String timing  = rs.getObject("TIMING").toString();
            System.out.println( "TITLE = " + title );
            System.out.println( "LOCATION = " + location );
            System.out.println( "TIMING = " + timing);
            System.out.println();
            //IMPORTANT STATEMENT HERE:
            items.add(title + "\t" + location + "\t" + timing);
         }
        rs.close();
        stmt.close();
        c.close();
      
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    
       
    }
    @FXML
    private void displayNotes(ActionEvent event) throws IOException{

        System.out.println("Changing to Notes");
        String notes = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:first.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String selected_item = tasks_listview.getSelectionModel().getSelectedItem().toString();
            String sql = "SELECT NOTES FROM TASKS WHERE TIMING = " + "'" +
            selected_item.substring(selected_item.lastIndexOf("\t") + 1, selected_item.length()) + "'" + ";";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery( sql );
            while ( rs.next() ) {
                notes = rs.getString("NOTES");
                System.out.println( "NOTES = " + notes );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
            
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Operation done successfully");
        
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNotes.fxml"));
        //Parent notes_parent = FXMLLoader.load(getClass().getResource("FXMLNotes.fxml"));
        Scene notes_scene = new Scene((Pane) loader.load());
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setScene(notes_scene);
        FXMLNotesController controller =  loader.<FXMLNotesController>getController();
        controller.setNotes(notes);
        app_stage.show();  
    }
    
    @FXML
    private void sortMenuItemButtonAction(ActionEvent event) throws IOException {
           MenuItem menu = (MenuItem) event.getSource();
           sort_menubutton.setText(menu.getText());
           refreshList();
    }
    @FXML
    private void ascdescMenuItemButtonAction(ActionEvent event) throws IOException {
           MenuItem menu = (MenuItem) event.getSource();
           ascdesc_menubutton.setText(menu.getText());
           refreshList();
    }
    @FXML
    private void completeButtonAction(ActionEvent event) throws IOException {
        Integer delete_index = null;
        delete_index = tasks_listview.getSelectionModel().getSelectedIndex();
        if (!delete_index.equals(null))
        {
            System.out.println("Deleting index " + delete_index.toString());
            deleteTask();
            refreshList();
        }
    }
    @FXML
    private void pressListViewAction() {
        System.out.println("Selected index: " + tasks_listview.getSelectionModel().getSelectedIndex());
        
    }
    private void deleteTask(){
      Connection c = null;
      Statement stmt = null;
      
      try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:first.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String selected_item = tasks_listview.getSelectionModel().getSelectedItem().toString();
      String sql = "DELETE from Tasks WHERE TIMING = " +"'" +
             selected_item.substring(selected_item.lastIndexOf("\t") + 1, selected_item.length()) + "'" + ";";
      System.out.println(sql);
      stmt.executeUpdate(sql);
      c.commit();
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Operation done successfully");
    }
    @FXML
    private void editButtonAction(){
        //go to edit screen, have everything filled in
        
        //update
    }
}
