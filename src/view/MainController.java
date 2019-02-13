package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.SmartApSelection;

public class MainController implements Initializable{	
	
	private SmartApSelection parameters;
	
	@FXML private Tab tab1;
	@FXML private Tab tab2;
	@FXML private Tab tab3;
	@FXML private Tab tab4;
	@FXML private Tab tab5;
	
	
	@FXML private TabPane pane;
	@FXML public  Tab1Controller tab1contentController;
	@FXML public  Tab3Controller tab3contentController;
	@FXML public  Tab4Controller tab4contentController;
	@FXML public  Tab5Controller tab5contentController;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    pane.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldTab, newTab) -> {
            if (newTab == tab1) {
            } else if (newTab == tab3) {            	
            } else if (newTab == tab4) {       
            } else if (newTab == tab5) {
            }
        });
	}
	
	@FXML
	private void handleTab1Change() {
		tab1contentController.BindData();
	}
	
	@FXML
	private void handleTab3Change() {
		tab3contentController.BindData();
	}
	
	@FXML
	private void handleTab5Change() {
		tab5contentController.BindData();
	}
	
	
	@SuppressWarnings("unused")
	private SmartApSelection getSmartAppParameters() {
		return parameters;
	}
	
	@SuppressWarnings("unused")
	private void setSmarAppParameters(SmartApSelection params) {
		this.parameters = params;
	}
	

}