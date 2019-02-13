package httpRequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import httpRequestHandler.ClientManagerResource.ScanResults;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import model.Client;

public class ClientManagerResource {

	public static ObservableList<Client> getClientInfo(){
		
		ObservableList<Client> ClientData = FXCollections.observableArrayList();

		try {						
			String AgentsURL = RequestHandler.URL + "/odin/clients/connected/json";
			
			java.net.URL url = new java.net.URL(AgentsURL);
		
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("GET");
		
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
			int status = con.getResponseCode();
						
			if(status == 200) {
				
			     BufferedReader in = new BufferedReader(
			             new InputStreamReader(con.getInputStream()));
			     String inputLine;
			     StringBuffer response = new StringBuffer();
			     while ((inputLine = in.readLine()) != null) {
			     	response.append(inputLine);
			     }
			     in.close();
			     			     
			     //Client[] clientsArray = RequestHandler.GSON.fromJson(response.toString(), Client[].class);	
			     
			     ScanResults[] ScanningRes = RequestHandler.GSON.fromJson(response.toString(), ScanResults[].class);
			     if(ScanningRes==null )return ClientData;
			     List<ScanResults> scan = Arrays.asList(ScanningRes);
			     
			     
			     List<Client> clientList = new ArrayList<Client>();
			     
			     for (ScanResults clientScanning : scan) {
					Client client = new Client(clientScanning.getIpAddress(), clientScanning.getHwAddress());
					client.setAverageDBM(clientScanning.getAverageDBM());
					client.setAgent(clientScanning.getLvap().agent.ipAddress);
					System.out.println(clientScanning.getLvap());
					clientList.add(client);
			     }
			     
			     
			     ClientData = FXCollections.observableArrayList(clientList);
			     
			     con.disconnect();
			     return ClientData;				
			}
			else {
				con.disconnect();			
				return null;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static List<ScanResults> getScanResults() {
		try {			
			
			List<ScanResults> scanningresults = null;
			
			String AgentsURL = RequestHandler.URL + "/odin/clients/all/json";
			
			java.net.URL url = new java.net.URL(AgentsURL);
		
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("GET");
		
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
			int status = con.getResponseCode();
						
			if(status == 200) {
				
			     BufferedReader in = new BufferedReader(
			             new InputStreamReader(con.getInputStream()));
			     String inputLine;
			     StringBuffer response = new StringBuffer();
			     while ((inputLine = in.readLine()) != null) {
			     	response.append(inputLine);
			     }
			     in.close();
			     			     
			     ScanResults[] clientsArray = RequestHandler.GSON.fromJson(response.toString(), ScanResults[].class);			     
			     scanningresults = Arrays.asList(clientsArray);
			}
			
			else {
				con.disconnect();			
			}	
			
			return scanningresults;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public class ScanResults {
		
		private final String hwAddress;
		private String rssi;
		private double[] averageDBM;
		private Lvap lvap;
		private Map<String, Integer> agentsHeardClient;
		private Long lastScanInfo;
		private String ipAddress;	
		
		
		public ScanResults(String hwAddress) {
			this.hwAddress = hwAddress;
		}
		
		public String getHwAddress() {
			return hwAddress;
		}

		public String getRssi() {
			return rssi;
		}


		public void setRssi(String rssi) {
			this.rssi = rssi;
		}

		public Lvap getLvap() {
			return lvap;
		}


		public void setLvap(Lvap lvap) {
			this.lvap = lvap;
		}


		public double[] getAverageDBM() {
			return averageDBM;
		}


		public void setAverageDBM(double[] averageDBM) {
			this.averageDBM = averageDBM;
		}


		public Long getLastScanInfo() {
			return lastScanInfo;
		}


		public void setLastScanInfo(Long lastScanInfo) {
			this.lastScanInfo = lastScanInfo;
		}


		public String getIpAddress() {
			return ipAddress;
		}
		
		public Map<String, Integer> getAgentsHeardClient() {
			return agentsHeardClient;
		}

		public void setAgentsHeardClient(Map<String, Integer> agentsHeardClient) {
			this.agentsHeardClient = agentsHeardClient;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}		
	}
	
	public class Lvap {
		
		public ScanAgent agent;
		
	}
	
	public class ScanAgent {
		
		public String ipAddress;
		public String lastHeard;
		
		public String getIpAddress() {
			return ipAddress;
		}
		
		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}	
	}
	


}



