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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AccessPoint;
import model.Client;

public class AgentManagerResource {    
    
	public static ObservableList<AccessPoint> getAccesspointInfo(){

		ObservableList<AccessPoint> APData = FXCollections.observableArrayList();
		try {		

			String AgentsURL = RequestHandler.URL + "/odin/agents/json";

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

				AccessPoint[] apArray = new Gson().fromJson(response.toString(), AccessPoint[].class);
				List<AccessPoint> aps = Arrays.asList(apArray);

				APData = FXCollections.observableArrayList(aps);
			}
			else {
			}

			con.disconnect();

		} catch (Exception e) {
			e.printStackTrace();			
		}		
		return APData;
	}



	public static AccessPoint changeApchannel(AccessPoint ap, int channel) {
		try {
			String AgentsURL = RequestHandler.URL + "/odin/agents/json";

			java.net.URL url = new java.net.URL(AgentsURL);
			
			postHttpInfo info = new postHttpInfo(ap.getipAddress(), channel);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);			
			con.setDoInput(true);
			con.setDoOutput(true);
			
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Accept", "application/json");			
			con.setRequestMethod("POST");
			
	        DataOutputStream wr = null;
	        try {
	            wr = new DataOutputStream(con.getOutputStream());
	            wr.writeBytes(RequestHandler.GSON.toJson(info));
	            System.out.println(RequestHandler.GSON.toJson(info));
	            wr.flush();
	            wr.close();
	        } catch(IOException exception) {
	            throw exception;
	        } finally {
	            closeQuietly(wr);
	        }	    

			int status = con.getResponseCode();

			if(status == 200) {
				ap.setChannel(channel);
				return ap;
			}		
			con.disconnect();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static Map<String, Map<String, String>> getTxResults(String ap) {
		try {
			String AgentsURL = RequestHandler.URL + "/odin/agents/scaninfo/internal/txStats/json";

			java.net.URL url = new java.net.URL(AgentsURL);
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);			
			con.setDoInput(true);
			con.setDoOutput(true);
			
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Accept", "application/json");			
			con.setRequestMethod("POST");
			
			ApIpAddress accessPoint = new ApIpAddress(ap);
			
	        DataOutputStream wr = null;
	        try {
	            wr = new DataOutputStream(con.getOutputStream());
	            wr.writeBytes(RequestHandler.GSON.toJson(accessPoint));
	            wr.flush();
	            wr.close();
	        } catch(IOException exception) {
	            throw exception;
	        } finally {
	            closeQuietly(wr);
	        }	    

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
				
				Map<String, Map<String, String>> txResults = new Gson().fromJson(response.toString(), Map.class);
				return txResults;

			}
			else {
		}			

			con.disconnect();
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	static class ApIpAddress {
		private String apIpAddress;
		
		public ApIpAddress(String apIpAddress) { this.apIpAddress=apIpAddress;}
	}

	
	public static List<MACAddressHeard> getScanChannel(AccessPoint ap, int channel, int time) throws AgentBusyException {
		try {
			String AgentsURL = RequestHandler.URL + "/odin/agents/scan/json";

			java.net.URL url;

			url = new java.net.URL(AgentsURL);

			
			postHttpInfo info = new postHttpInfo(ap.getipAddress(), channel, time);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);			
			con.setDoInput(true);
			con.setDoOutput(true);
			
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Accept", "application/json");			
			con.setRequestMethod("POST");
			
	        DataOutputStream wr = null;
	        try {
	            wr = new DataOutputStream(con.getOutputStream());
	            wr.writeBytes(RequestHandler.GSON.toJson(info));
	            System.out.println(RequestHandler.GSON.toJson(info));
	            wr.flush();
	            wr.close();
	        } catch(IOException exception) {
	            throw exception;
	        } finally {
	            closeQuietly(wr);
	        }	    

			int status = con.getResponseCode();

			if(status == 200) {
				
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				
				String inputLine;
				
				List<MACAddressHeard> scanResults = new ArrayList<MACAddressHeard>();
				while ((inputLine = in.readLine()) != null) {
					
					if(inputLine.contains("BUSY")) {
						throw new AgentBusyException();
					}
					
					//Format and clean input					
					inputLine=inputLine.replace("{", "");
					inputLine=inputLine.replace("\"", "");
					inputLine=inputLine.replace("res:", "");
					inputLine=inputLine.replace("}", "");
					inputLine=inputLine.trim();

					String[] params = inputLine.split(" ");

					if(inputLine.isEmpty() ||params.length != 2)
						continue;
					
					MACAddressHeard addressHeard = new MACAddressHeard(params[0], params[1]);
					scanResults.add(addressHeard);					
				}
				in.close();
				return scanResults;		
			}		
			con.disconnect();
			return null;
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	
	
	protected static void closeQuietly(Closeable closeable) {
	    try {
	        if( closeable != null ) {
	            closeable.close();
	        }
	    } catch(IOException ex) {
	
	    }
	}

	static class postHttpInfo {
		@SuppressWarnings("unused")
		private String apIpAddress;
		@SuppressWarnings("unused")
		private int channel;
		@SuppressWarnings("unused")
		private int timeToScan;
		@SuppressWarnings("unused")
		private final String network = "*";
		
		public postHttpInfo(String apIpAddress, int channel) {
			this.apIpAddress = apIpAddress;
			this.channel = channel;
		}

		public postHttpInfo(String apIpAddress, int channel, int timeToScan) {
			this.apIpAddress = apIpAddress;
			this.channel = channel;
			this.timeToScan = timeToScan;
		}
	}
	
	
	@SuppressWarnings("serial")
	public static class AgentBusyException extends Exception {		 
		public AgentBusyException() { super(); }
	}
	
	public static class MACAddressHeard {
		private String MACAddress;
		private String potencyHeard;
		
		public MACAddressHeard(String MACAddress, String potencyHeard) {
			this.MACAddress= MACAddress;
			this.potencyHeard = potencyHeard;
		}
		
		public String getMACAddress() {
			return MACAddress;
		}
		public String getpotencyHeard() {
			return potencyHeard;
		}

		
	}

	public static HashMap<Client, Integer> getAccessPointChannelOccupancy(String apSelected) {
		HashMap<Client, Integer> map = new HashMap<Client, Integer>();
		
		return map;
	}



	public static HashMap<Client, Double> getAccesPointAirTime(String apSelected) {
		return null;
	}



	public static void handoffClient(Client client, String new_ap) {

		if(new_ap.isEmpty()) return;
	}


}
