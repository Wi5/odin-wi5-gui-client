package view;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import httpRequestHandler.AgentManagerResource;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.AccessPoint;
import model.Client;

public class Tab5Controller implements Initializable{
	
	@FXML private ComboBox<String> comboAccesPoints;	
	
	@FXML private BarChart<String, Number> packetsClient;
	
	@FXML private BarChart<String, Number> airTimeClients;
	
	@FXML private BarChart<String, Number> avgRateClient;
	
	private XYChart.Series<String, Number> packetsSerie;
	
	private XYChart.Series<String, Number>  airTimeSerie;
	
	private XYChart.Series<String, Number> avgRateSerie;

	
	@FXML private Button clean;
	
    final NumberAxis yAxis_packets = new NumberAxis(0, 200, 1);
    final NumberAxis yAxis_average = new NumberAxis(0, -99, -1);
    final NumberAxis yAxis_airTime = new NumberAxis(0, 5, 0.5);
    
    final CategoryAxis xAxis = new CategoryAxis();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BindData();		
	}

	public void BindData() {
		comboAccesPoints.getItems().clear();
				
		ObservableList<AccessPoint> listaAp = AgentManagerResource.getAccesspointInfo();
		
		for (AccessPoint accessPoint : listaAp) {
			comboAccesPoints.getItems().add(accessPoint.getipAddress());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@FXML
	private void selectedNewAp() {
		String apSelected = comboAccesPoints.getValue();	
		
		Map<String, Integer> packets = new HashMap<>();		
		Map<String, Double> airTimePerClient = new HashMap<>();
		
		Map<String, Map<String, String>> txResults = AgentManagerResource.getTxResults(apSelected);
		if(txResults == null) return;
		
		for (Entry<String, Map<String, String>> entry : txResults.entrySet())
		{
		    packets.put(entry.getKey(), Integer.parseInt(entry.getValue().get("packets")));
		    airTimePerClient.put(entry.getKey(), Double.parseDouble(entry.getValue().get("packets")));
		}
			
		xAxis.setLabel("Cliente");       
		yAxis_packets.setLabel("Valor");
		yAxis_airTime.setLabel("Tiempo en el aire");
		
		packetsSerie = new XYChart.Series<String, Number>();
		for (Entry<String, Integer> entry : packets.entrySet()) {
			packetsSerie.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));

		}
			
		airTimeSerie = new XYChart.Series<String, Number>();
		
		for (Entry<String, Double> entry : airTimePerClient.entrySet()) {
			airTimeSerie.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));

		}
		
		avgRateSerie = new XYChart.Series<String, Number>();
		
		avgRateSerie.getData().add(new XYChart.Data<String, Number>("08-0A-A3-0B-17-3A", 100-70));
		avgRateSerie.getData().add(new XYChart.Data<String, Number>("B6-C1-0A-29-83-8D", 100-30));
		avgRateSerie.getData().add(new XYChart.Data<String, Number>("D9-06-E4-52-AC-76", 100-20));
		avgRateSerie.getData().add(new XYChart.Data<String, Number>("8F-BB-FE-7A-AC-46", 100-20));
		avgRateSerie.getData().add(new XYChart.Data<String, Number>("55-CE-85-03-4F-68", 100-5));  		
		
		packetsClient.getData().addAll(packetsSerie);
		avgRateClient.getData().addAll(avgRateSerie);
		airTimeClients.getData().addAll(airTimeSerie);
		
	} 
	
	
	@FXML 
	private void handleCleanButton() {
		
		packetsSerie.getData().clear();
		packetsClient.getData().clear();
		airTimeSerie.getData().clear();
		airTimeClients.getData().clear();
		avgRateSerie.getData().clear();
		avgRateClient.getData().clear();
		
	}
	
	

}
