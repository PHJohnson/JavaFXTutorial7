/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Phil
 */
public class FXMLNotesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextArea notes_textarea;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void setNotes(String notes){
        notes_textarea.setText(notes);
    }
    
    @FXML
    private void backButtonAction(ActionEvent event) throws IOException {
        Parent view_page_parent = FXMLLoader.load(getClass().getResource("FXMLViewPage.fxml"));
        Scene view_page_scene = new Scene(view_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setScene(view_page_scene);
        app_stage.show();
    }
    
   
   
    
}
