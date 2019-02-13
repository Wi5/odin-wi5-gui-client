package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import httpRequestHandler.AgentManagerResource;
import httpRequestHandler.RequestHandler;
import httpRequestHandler.AgentManagerResource.AgentBusyException;
import httpRequestHandler.AgentManagerResource.MACAddressHeard;
import httpRequestHandler.ClientManagerResource;
import httpRequestHandler.NetworkManagerResource;
import httpRequestHandler.NetworkManagerResource.PassiveMatrix;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.AccessPoint;
import model.Client;
import model.SmartApSelection;

public class Tab3Controller implements Initializable{
	
	 @FXML
	 private TreeTableView<TreeItemNetwork> apTable;
	 
	 @FXML
	 private TreeTableColumn<TreeItemNetwork, String> IPNameColumn;
	 
	 @FXML
	 private TreeTableColumn<TreeItemNetwork, String> MACNameColumn; 	 
	 
	 @FXML
	 private TreeTableColumn<TreeItemNetwork, String> APSTANameColumn; 
	 
     
	 @FXML
	 private Label Mac_label;
	 
	 @FXML
	 private Label Mac;
	 
	 @FXML
	 private Label IP_label;

	 @FXML
	 private Label IP;

	 @FXML
	 private Label Network_label;
	 
	 @FXML
	 private Label Network;

	 @FXML
	 private Label Agent;
	 
	 @FXML
	 private Label Agent_label; 

	 
	 @FXML
	 private Label Channel;
	 
	 @FXML
	 private Label Channel_label;


	 @FXML
	 private Label Potency; 
	 
	 @FXML
	 private Label Potency_label;
	 

	 @FXML
	 private Label LastHeard; 
	 
	 @FXML
	 private Label LastHeard_label;
	 
	 
	 @FXML 
	 private Button button_0;
	 
	 @FXML
	 private Button button_1;
	 
	 	 

    @FXML
	public void BindData() {    	
    	
    	String ap_string = "Acces Point";
    	String client_string = "STA";
    	
		IPNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getIpAddress()));
		MACNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getNetwork()));
		APSTANameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getType()));

		
		ObservableList<AccessPoint> agents = AgentManagerResource.getAccesspointInfo();
		List<TreeItem<TreeItemNetwork>> rootlist = new ArrayList<TreeItem<TreeItemNetwork>>();
		for (AccessPoint accessPoint : agents) {
			rootlist.add(new TreeItem<TreeItemNetwork>(new TreeItemNetwork(accessPoint, null, false)));
		}
		
		ObservableList<Client> clients = ClientManagerResource.getClientInfo();
		List<TreeItem<TreeItemNetwork>> clientList = new ArrayList<TreeItem<TreeItemNetwork>>();
		
		for (Client client : clients) {
			clientList.add(new TreeItem<TreeItemNetwork>(new TreeItemNetwork(null, client, true)));
		}
		
		
		apTable.setRoot(createTreeView(rootlist, clientList));
		apTable.setShowRoot(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	BindData();
    	
    	showClientDetails(null);
    	
        
    	apTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showClientDetails(newValue.getValue()));
    	
	}
	
	
	private void showClientDetails(TreeItemNetwork treeObject) {

		if(treeObject!=null) {
			if(treeObject.isClient) {
				fillClient(treeObject.getClient());
			}
			else {
				fillAgent(treeObject.getAp());
			}
			button_0.setVisible(true);
			button_1.setVisible(true);


		} else {
			
			button_0.setVisible(false);
			button_1.setVisible(false);
			
			Mac.setText("");
			Mac.setVisible(false);

			IP.setText("");
			IP.setVisible(false);

			Network.setText("");
			Network.setVisible(false);

			Agent.setText("");
			Agent.setVisible(false);

			Channel.setText(Integer.toString(1));
			Channel.setVisible(false);

			Potency.setText(Integer.toString(0));	
			Potency.setVisible(false);
			
			LastHeard.setText("");
			LastHeard.setVisible(false);
			


		}
	}
	
	private void fillAgent(AccessPoint ap) {		
		
		Mac_label.setText("IP");
		Mac.setText(ap.getipAddress().toString());
		Mac.setVisible(true);

		IP_label.setText("Network");
		IP.setText(ap.getNetwork());
		IP.setVisible(true);

		Network_label.setText("Number of Clients");
		Network.setText(Integer.toString(ap.getNumberOfClients()));
		Network.setVisible(true);

		Agent_label.setText("Channel");
		Agent.setText(Integer.toString(ap.getChannel()));
		Agent.setVisible(true);

		Channel_label.setText("Power");
		Channel.setText(Integer.toString(ap.getPotency()) + " dbms");
		Channel.setVisible(true);
		
		Potency_label.setText("Time of last ping");
		Potency.setText(ap.getLastHeard());
		Potency.setVisible(true);
		
		LastHeard_label.setText("Time of last Scan");
		LastHeard.setVisible(true);
		LastHeard.setText(ap.getLastScan());
		LastHeard_label.setVisible(true);
		
		button_0.setText("Scan");
		button_1.setText("Change Channel");


	}

	private void fillClient(Client client) {
		
		Mac_label.setText("MAC");
		Mac.setText(client.getmAC());
		Mac.setVisible(true);
		
		IP_label.setText("IP");
		IP.setText(client.getiP());
		IP.setVisible(true);
		
		Network_label.setText("Network");
		Network.setText(client.getNetwork());
		Network.setVisible(true);
		
		Agent_label.setText("Agent");
		Agent.setText(client.getAgent());
		Agent.setVisible(true);
		
		Channel_label.setText("Channel");
		Channel.setText(Integer.toString(client.getChannel()));
		Channel.setVisible(true);
		
		Potency_label.setText("Avg Power (RSSI)");
		Potency.setText(Double.toString(client.getPotency()) + " dbms");	
		Potency.setVisible(true);

		LastHeard_label.setVisible(false);
		LastHeard.setText("");
		LastHeard.setVisible(false);
		
		button_0.setText("Handoff");
		button_1.setText("Search");
		
	}


	private TreeItem<TreeItemNetwork> createTreeView(List<TreeItem<TreeItemNetwork>> accespoints, List<TreeItem<TreeItemNetwork>> clients) {
	    TreeItem<TreeItemNetwork> dummyRoot = new TreeItem<>();	
	    
	    for (TreeItem<TreeItemNetwork> treeItem : accespoints) {
			dummyRoot.getChildren().add(treeItem);
			String ipAddress = treeItem.getValue().getIpAddress();
			
			if(clients.size() != 0) {
				for (TreeItem<TreeItemNetwork> children : clients) {
					Client childCli = children.getValue().getClient();					
					if(children.getValue().getIsClient()){
						if(childCli.getAgent().equals(ipAddress)) {
							treeItem.getChildren().add(children);
						}
					}
				}
			}
					
		}
	    return dummyRoot;
	}
	
	@FXML
	public void handleButton0() {
		TreeItem<TreeItemNetwork> item = apTable.getSelectionModel().getSelectedItem();
	    
	    if(item != null) {
	    	TreeItemNetwork realItem = item.getValue();
	    	if(realItem.isClient) {
	    		handleSearch(realItem.getClient());
	    	}
	    	else {
	    		handleChangeChannel(realItem.getAp());

	    	}
	    }
	    else {
	    	ShowErrorMessage("Select one row please.");
	    	return;
	    }	
	}
	
	@FXML
	public void handleButton1() {
		TreeItem<TreeItemNetwork> item = apTable.getSelectionModel().getSelectedItem();
	    
	    if(item != null) {
	    	TreeItemNetwork realItem = item.getValue();
	    	if(realItem.isClient) {
	    		handleHandoff(realItem.getClient());
	    	}
	    	else {
	    		handleScan(realItem.getAp());
	    	}	    	
	    }
	    else {
	    	ShowErrorMessage("Select one row please.");
	    	return;
	    }
	}
	
	public void handleSearch(Client client) {
		PassiveMatrix matrix = NetworkManagerResource.getMatrixPasive();
		
		Dialog<SmartApSelection> dialog = new Dialog<>();
		dialog.setHeaderText("Client Search");
		
	    int index = -1;

	    for (int i = 0; i < matrix.getClients().length; i++) {
	    	System.out.println( client.getmAC()+"-----" +matrix.getClients()[i]);
	        if (matrix.getClients()[i].equals(client.getmAC())) {
	            index = i;
	            break;
	        }
	    }
	    
	    // Set the icon (must be included in the project).
 		// Set the button types.
 		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
 		
	    int agents_number = matrix.getAgents().length;
	    Double[][] dbms = matrix.getHeardDbms();
	    

 		// Create the username and password labels and fields.
 		GridPane grid = new GridPane();
 		grid.setHgap(10);
 		grid.setVgap(10);
 		
	    for (int i = 0; i < agents_number; i++) {
			grid.add(new Label("Access Point: " +matrix.getAgents()[i]), 0, i);
			grid.add(new Label("heard the client with: "+dbms[index][i]+" dbms"), 1, i);
		}
		dialog.getDialogPane().setContent(grid);

	    dialog.showAndWait();
	}
	
	
	public void handleHandoff(Client client) {
		
		PassiveMatrix matrix = NetworkManagerResource.getMatrixPasive();
		
		List<String> agentsAndPotency =  new ArrayList<>();
		
	    int index = -1;

	    for (int i = 0; i < matrix.getClients().length; i++) {
	    	System.out.println( client.getmAC()+"-----" +matrix.getClients()[i]);
	        if (matrix.getClients()[i].equals(client.getmAC())) {
	            index = i;
	            break;
	        }
	    }
	    
	    int agents_number = matrix.getAgents().length;
	    Double[][] dbms = matrix.getHeardDbms();
	    
	    
	    for (int i = 0; i < agents_number; i++) {
	    	if(dbms[index][i] == 99) continue;
			String agentAndPotency = matrix.getAgents()[i] + " - Avg rate: " + dbms[index][i];
			agentsAndPotency.add(agentAndPotency);			
		}

		
		ChoiceDialog<String> dialog = new ChoiceDialog<String>("Access Point to handoff", agentsAndPotency);
		dialog.setHeaderText("Manual handoff client");
		dialog.setHeaderText(null);
		dialog.setTitle("Manual handoff client");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){

			AgentManagerResource.handoffClient(client, result.orElse(""));
			
			BindData();
		}
	}
	
	
	public void handleScan(AccessPoint ap) {
		
	    List<MACAddressHeard> scanRes = null;
		try {
			scanRes = AgentManagerResource.getScanChannel(ap, ap.getChannel(), 200);

		    Dialog<String> dialog = createScanDialog(scanRes);
		    dialog.showAndWait();
		} catch (AgentBusyException e) {
			ShowErrorMessage("AGENT IS BUSY TODO: ADD OLD STATS");
		}
	}
	
	private Dialog<String> createScanDialog(List<MACAddressHeard> ScanRes){
		Dialog<String> dialog = new Dialog<>();
		
		dialog.setTitle("Scanning results");
		dialog.setHeaderText("Here, clients listened to during scanning");
		
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
				
		TableView<MACAddressHeard> table = new TableView<MACAddressHeard>();
		
		table.setEditable(false);
		TableColumn<MACAddressHeard, String> macAddress = new TableColumn<>("Mac Address");
		TableColumn<MACAddressHeard, String> Potency	= new TableColumn<>("Potency");
		
		macAddress.setCellValueFactory(
				cellData ->new SimpleStringProperty(cellData.getValue().getMACAddress().toString()));
		Potency.setCellValueFactory(
				cellData ->new SimpleStringProperty(cellData.getValue().getpotencyHeard().toString()));		
		
		table.setItems(FXCollections.observableArrayList(ScanRes));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.getColumns().add(macAddress);
		table.getColumns().add(Potency);
        
        final VBox vbox = new VBox();
        vbox.getChildren().addAll(table);       
		
		dialog.getDialogPane().setContent(vbox);
		
		return dialog;
	}
	
	
	
	public void handleChangeChannel(AccessPoint ap) {
		
	    List<String> channels = new ArrayList<>();
	    for(int i = 1; i<=14 ;i++) {
	    	channels.add(Integer.toString(i));
	    }
	    
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(Integer.toString(ap.getChannel()), channels);
		dialog.setTitle("Channel change");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the new Channel:");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			AgentManagerResource.changeApchannel(ap,Integer.parseInt(result.get()));
			
			BindData();
		}
	}
	
	
	private void ShowErrorMessage(String error) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText(null);
		alert.setContentText(error);
		
		alert.showAndWait();
	}	
	
	class TreeItemNetwork {
		
		private AccessPoint ap;
		private Client client;
		private Boolean isClient;
		private String ipAddress;
		private String network;
		private String type;
		
		public TreeItemNetwork(AccessPoint ap, Client client, Boolean isClient) {
			this.ap = ap;
			this.client = client;
			this.isClient = isClient;
			if(isClient) {
				this.ipAddress = client.getiP();
				this.network = client.getmAC();
			}
			else {
				this.ipAddress = ap.getipAddress();
				this.network = ap.getNetwork();
			}
		}

		public String getType() {
			if(isClient) {
				return "STA";
			}
			else {
				return "AP";
			}
		}
		
		public AccessPoint getAp() {
			return ap;
		}

		public void setAp(AccessPoint ap) {
			this.ap = ap;
		}

		public Client getClient() {
			return client;
		}

		public void setClient(Client client) {
			this.client = client;
		}

		public Boolean getIsClient() {
			return isClient;
		}

		public void setIsClient(Boolean isClient) {
			this.isClient = isClient;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getNetwork() {
			return network;
		}

		public void setNetwork(String network) {
			this.network = network;
		}		
	}
	
}