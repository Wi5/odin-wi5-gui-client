package httpRequestHandler;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import httpRequestHandler.AgentManagerResource.AgentBusyException;
import httpRequestHandler.AgentManagerResource.MACAddressHeard;
import httpRequestHandler.AgentManagerResource.postHttpInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AccessPoint;
import model.Client;
import model.Network;
import model.SmartApSelection;

public class NetworkManagerResource {
		
	public static Network getNetwork(){	
		
			
		Network network = new Network();
		ObservableList<AccessPoint> agentsList = AgentManagerResource.getAccesspointInfo();
		List<AccessPoint> agent = agentsList.stream().collect(Collectors.toList());
		network.setAgents(agent);
		
		
		ObservableList<Client> clientList = ClientManagerResource.getClientInfo();
		if(clientList!=null && clientList.size()!=0) {
			List<Client> clients = clientList.stream().collect(Collectors.toList());
			network.setClients(clients);		
		}
				
		
		return network;	
	}
	
	
	public static ActiveMatrix getMatrixActive() {
		try {		

			String activeMatrix = RequestHandler.URL + "/odin/agents/activeMatrix";

			java.net.URL url = new java.net.URL(activeMatrix);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int status = con.getResponseCode();

			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + status);

			if(status == 200) {

				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ActiveMatrix matrix = new Gson().fromJson(response.toString(), ActiveMatrix.class);
				return matrix;	
			}
			else {
			}

			con.disconnect();

		} catch (Exception e) {
			e.printStackTrace();			
		}		
		
		return null;
	}
	
	
	public static PassiveMatrix getMatrixPasive() {
		try {		

			String activeMatrix = RequestHandler.URL + "/odin/agents/pasiveMatrix";

			java.net.URL url = new java.net.URL(activeMatrix);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int status = con.getResponseCode();

			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + status);

			if(status == 200) {

				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				PassiveMatrix matrix = new Gson().fromJson(response.toString(), PassiveMatrix.class);
				return matrix;	
			}
			else {
			}

			con.disconnect();

		} catch (Exception e) {
			e.printStackTrace();			
		}		
		
		return null;
	}  
	
	protected static void closeQuietly(Closeable closeable) {
	    try {
	        if( closeable != null ) {
	            closeable.close();
	        }
	    } catch(IOException ex) {
	
	    }
	}
	
	public class ActiveMatrix {
		
		private String[] Agents;
		private Double[][] HeardDbms;
		
		public ActiveMatrix(String[] Agents, Double[][] HeardDbms)
		{
			this.Agents =Agents;
			this.HeardDbms = HeardDbms;
		}
		
	}
	
	
	public class PassiveMatrix{
		
		private String[] agents;
		private String[] clients;
		private Double[][] heardDbms;
		
		public PassiveMatrix() {};
		
		public PassiveMatrix(String[] Agents, Double[][] HeardDbms)
		{
			this.agents =Agents;
			this.heardDbms = HeardDbms;
			this.clients = clients;
		}
	    public String[] getAgents() {
	        return agents;
	    }

	    public void setAgents(String[] agents) {
	        this.agents = agents;
	    }

	    public String[] getClients() {
	        return clients;
	    }

	    public void setClients(String[] clients) {
	        this.clients = clients;
	    }

	    public Double[][] getHeardDbms() {
	        return heardDbms;
	    }

	    public void setHeardDbms(Double[][] heardDbms) {
	        this.heardDbms = heardDbms;
	    }
	}




}
