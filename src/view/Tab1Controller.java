package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;

import controller.graphStreamController.NetworkGraph;
import httpRequestHandler.ClientManagerResource;
import httpRequestHandler.RequestHandler;
import httpRequestHandler.ClientManagerResource.ScanResults;
import httpRequestHandler.NetworkManagerResource;
import httpRequestHandler.NetworkManagerResource.PassiveMatrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Client;
import model.SmartApSelection;

public class Tab1Controller implements Initializable{
	
	
	public SmartApSelection parameters;
	
	@FXML 
	private Label Title;
	
	@FXML 
	private Label TitleValue;
	
	@FXML 
	private Label Label1;
	
	@FXML 
	private Label Label1res;
	
	@FXML 
	private Label Label2;
	
	@FXML
	private Label Label2res;
	
	@FXML 
	private Label Label3;
	
	@FXML 
	private Label Label3res;
	
	@FXML 
	private Label Label4;
	
	@FXML 
	private Label Label4res;
	
	@FXML 
	private Label Label5;
	
	@FXML 
	private Label Label5res;
	
	@FXML 
	private Label Label6;
	
	@FXML 
	private Label Label6res;
	
	@FXML 
	private Label Label7;
	
	@FXML 
	private Label Label7res;	
			 
	@FXML 
	private Button tab1_button_matrixDistance;
	
	@FXML
	private Button tab2_button_parameters;
	
	@FXML
	private GridPane pane;
	
	
	private Graph graph;

	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.graph = new MultiGraph("NETWORK GRAPH");   
  		
  	    Viewer vie =  new FxViewer(graph,  FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
  	      		
  	    FxDefaultView view1 = (FxDefaultView) vie.addDefaultView(false);
  	    
  	    graph = NetworkGraph.inicializeNetwork(graph);  	    
  	    
		pane.add(view1,0,0);
		
		BindData();	
		
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(Double.parseDouble(Label2res.getText())*2), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		    	List<ScanResults> scanningRes = ClientManagerResource.getScanResults();
		    	
		    	for (ScanResults scanResults : scanningRes) {
					Node node_client = graph.getNode(scanResults.getHwAddress());
					if(node_client != null) {
						Client client = (Client) node_client.getAttribute("client");
											
						double[] averageDBM = scanResults.getAverageDBM();
						Map<String, Integer> agents = scanResults.getAgentsHeardClient();					
						int id = agents.get("/"+client.getAgent());
						
						//Check potency
						client.setAverageDBM(averageDBM);
						client.setPotency(averageDBM[id]);
						NetworkGraph.ChangeEdgeColor(graph, client); 
						
						if(Math.abs(averageDBM[id])+10> Math.abs(parameters.getSignal_threshold()) 
								||Math.abs(parameters.getSignal_threshold())-10 < Math.abs(averageDBM[id])) {
							NetworkGraph.printOrRefreshPossibleHandoff(client, averageDBM, agents, id, graph);
						}
						
						//Check Handoff
						String actual_agent = client.getAgent();
						String possible_new_agent = scanResults.getLvap().agent.ipAddress;						
												
						if(!actual_agent.equals(possible_new_agent)) {
							NetworkGraph.HandleHandOff(graph, client, possible_new_agent);
							client.setAgent(possible_new_agent);
							node_client.setAttribute("client", client);
							continue;
						}						
						

					}
				}
		    	
		    }
		}));
		fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fiveSecondsWonder.play();
	}
	
	public void BindData() {
	
		this.parameters = RequestHandler.getAppParameters();
				
		if(parameters != null) {
			bindGridLabels(parameters);
		}		
		
		TitleValue.setText("SMARTAP SELECTION");
	}
	
	private void bindGridLabels(SmartApSelection parameters) 
	{		
		Label1res.setText(Integer.toString(parameters.getTime_to_start()));
		Label2res.setText(Integer.toString(parameters.getScanning_interval()));
		Label3res.setText(Integer.toString(parameters.getAdded_time()));
		Label4res.setText(Integer.toString(parameters.getSignal_threshold()));
		Label5res.setText(Integer.toString(parameters.getHysteresis_threshold()));
		Label6res.setText(Double.toString(parameters.getWeight()));
		Label7res.setText(parameters.getMode());

	}
	
	@FXML
	private void handleShowMatrixDistance() {
		
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Matriz de distancias");
        VBox dialogVbox = new VBox(20);        
        
        
        PassiveMatrix passiveMatrix = NetworkManagerResource.getMatrixPasive();
        int length = passiveMatrix.getAgents().length+1;
        int width = passiveMatrix.getClients().length+1;

        
        GridPane root = new GridPane();
        root.setGridLinesVisible(true);
        root.prefWidthProperty().bind(dialogVbox.widthProperty());
        root.prefHeightProperty().bind(dialogVbox.heightProperty());
        
        for(int y = 0; y < length; y++){
            for(int x = 0; x < width; x++){
                // Create a new TextField in each Iteration
                Label tf = new Label();

                tf.setAlignment(Pos.CENTER);                
              
                if(y == 0 && x == 0) {
                	continue;
                }
                else if(y == 0) {
                	String res = passiveMatrix.getAgents()[x-1];
            		tf.setText("AP " +res);
            	}
            	else if(x==0) {
                	String res = passiveMatrix.getAgents()[y-1];
            		tf.setText("Client " + res); 
                    }
            	else {
                    double res = passiveMatrix.getHeardDbms()[y-1][x-1];
                    tf.setText(""+res + " dbms");
                    res = Math.abs(res);
                    if(res<30)
                    	tf.setTextFill(Color.GREEN);
                    else if(res>30 && res<60)
                    	tf.setTextFill(Color.YELLOWGREEN);
                    else
                    	tf.setTextFill(Color.RED);
            	}
                tf.setPrefHeight(150);
                tf.setPrefWidth(150);
            	
            	// Iterate the Index using the loops
            	root.setRowIndex(tf,y);
            	root.setColumnIndex(tf,x);    
            	root.getChildren().add(tf);
            }
        }
        
        dialogVbox.getChildren().add(root);
        
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	@FXML
	private void handleRequestLogFile() {
		try {
			File file = RequestHandler.getSmartApLog();
			if(file != null) {
				java.awt.Desktop.getDesktop().edit(file);				
			}
			else {
				Alert alert = new Alert(AlertType.INFORMATION);
				
				alert.setTitle("Problems with Download");
				alert.setHeaderText(null);
				alert.setContentText("Something happen with the download");
				alert.showAndWait();
				
				if (alert.getResult() == ButtonType.OK) {

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleSubmitParameters() {
		// Create the custom dialog.
		Dialog<SmartApSelection> dialog = new Dialog<>();
		dialog.setHeaderText("Can modify the parameters");
		dialog.setTitle("Change the app's parameters");

		// Set the icon (must be included in the project).
		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Change", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		String timetoStart = Integer.toString(parameters.getTime_to_start());
		String getscanningInterval = Integer.toString(parameters.getScanning_interval());
		String added_time =Integer.toString(parameters.getAdded_time ());
		String signal =Integer.toString(parameters.getSignal_threshold());
		String hysteresis=Integer.toString(parameters.getHysteresis_threshold());
		String weight=Double.toString(parameters.getWeight());
		String txpowerSTA = Integer.toString(parameters.getTxpowerSTA());
		String thReqSTA =Integer.toString(parameters.getThReqSTA());
		
		TextField timeToStart = new TextField();
		timeToStart.setPromptText(timetoStart);
		
		TextField getScanningInterval = new TextField();
		getScanningInterval.setPromptText(getscanningInterval);
		
		TextField Added_time = new TextField();
		Added_time.setPromptText(added_time);
		
		TextField Signal = new TextField();
		Signal.setPromptText(signal);
		
		
		TextField GetScanningInterval = new TextField();
		GetScanningInterval.setPromptText(getscanningInterval);		

		TextField Hysteresis = new TextField();
		Hysteresis.setPromptText(hysteresis);
		
		TextField Weight = new TextField();
		Weight.setPromptText(weight);
		
		TextField TxpowerSTA = new TextField();
		TxpowerSTA.setPromptText(txpowerSTA);
		
		TextField ThReqSTA = new TextField();
		ThReqSTA.setPromptText(thReqSTA);

		grid.add(new Label("Scanning Interval"), 0, 1);
		grid.add(getScanningInterval, 1, 1);
		
		grid.add(new Label("Added time, recommeded value: 0"), 0, 2);
		grid.add(Added_time, 1, 2);
		
		grid.add(new Label("Signal"), 0, 3);
		grid.add(Signal, 1, 3);
		
		grid.add(new Label("Hysteresis, recommended value: 4"), 0, 4);
		grid.add(Hysteresis, 1, 4);
		
		grid.add(new Label("Weight(0-1), recommended value: 0.8"), 0, 5);
		grid.add(Weight, 1, 5);

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> timeToStart.requestFocus());

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		    	double weight_ = castStringToDouble(Weight.getText());
		    	if(weight_<0 || weight_>1) {
		    		weight_ = 0.8;
		    	}

		    	SmartApSelection new_parameters = new SmartApSelection(castStringToInt(timeToStart.getText()),castStringToInt(getScanningInterval.getText()),castStringToInt(Added_time.getText()), 
		    			castStringToInt(Signal.getText()),  castStringToInt(Hysteresis.getText()), weight_, 0, "",  castStringToInt(TxpowerSTA.getText()),
		    			castStringToInt(ThReqSTA.getText()), "");

		        return new_parameters;
		    }
		    return null;
		});

		Optional<SmartApSelection> result = dialog.showAndWait();
		if(result != null) {
			SmartApSelection smartAp = RequestHandler.sendParameters(result.orElseThrow(null));
			if(smartAp != null) {
				bindGridLabels(smartAp);
			}
			else {
			}
		}
	}
	
	public int castStringToInt(String oj) {
		int number = 0;
		try
		{
		    if(oj != null)
		     return number = Integer.parseInt(oj);
		}
		catch (NumberFormatException e)
		{
		   return number = 0;
		}
		return number;
	}
	
	public double castStringToDouble(String oj) {
		double number = 0;
		try
		{
		    if(oj != null)
		     return number = Double.parseDouble(oj);
		}
		catch (NumberFormatException e)
		{
		   return number = 0;
		}
		return number;
	}
}
