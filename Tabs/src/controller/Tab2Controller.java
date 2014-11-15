package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Tab2Controller implements Initializable{

	@FXML private Label lbl_2;
	@FXML private Button btn2;
	private int counter=0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	
	public  void addViewCounter(){
		lbl_2.setText(""+counter);
		counter++;
	}
	
	@FXML public void resetCounter1(ActionEvent e) throws IOException{
		MainController m = Main.loader.getController();
		m.tab1contentController.resetCounter();
	
   }

	public void resetCounter() {
		counter = 0;
		lbl_2.setText(""+counter);
	}

}