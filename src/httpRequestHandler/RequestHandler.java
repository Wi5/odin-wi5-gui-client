package httpRequestHandler;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Map;

import com.google.gson.*;

import httpRequestHandler.NetworkManagerResource.PassiveMatrix;
import model.SmartApSelection;

public class RequestHandler {		
  
    public static final Gson GSON = new Gson();    
    
    public static final String URL = "http://155.210.157.148:8080";
    //public static final String URL = "http://localhost:8080";

	private static final int BUFFER_SIZE = 4096;
    
    /**
     * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in 
     * the 200-399 range.
     * @param url The HTTP URL to be pinged.
     * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
     * the total timeout is effectively two times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */
    public static boolean pingURL(String url, int timeout) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }
    
    public static File getSmartApLog() throws IOException {
    	
    	System.out.println("-----------------------------------------------");
    	java.net.URL url = new java.net.URL(RequestHandler.URL + "/odin/smartApLog");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10,
							disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = "C:\\data\\log.txt";
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = File.separator + fileName;
			
			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();
			System.out.println("File downloaded");
			return new File("C:\\data\\log.txt");

		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
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
				
				System.out.println(response.toString());

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
	
	
	public static SmartApSelection getAppParameters() {
		try {
			String AgentsURL = URL + "/odin/params/json";
			
			java.net.URL url = new java.net.URL(AgentsURL);
		
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
			     
				 SmartApSelection parameters = GSON.fromJson(response.toString(),SmartApSelection.class);
			     
			     return parameters;
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
	
	public static SmartApSelection sendParameters(SmartApSelection smartApSelection) 
	{
		try {
			String AgentsURL = RequestHandler.URL + "/odin/params/json";
	
			java.net.URL url;
	
			url = new java.net.URL(AgentsURL);
	
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
	            wr.writeBytes(RequestHandler.GSON.toJson(smartApSelection));
	            System.out.println(RequestHandler.GSON.toJson(smartApSelection));
	            wr.flush();
	            wr.close();
	        } catch(IOException exception) {
	            throw exception;
	        } finally {
	            closeQuietly(wr);
	        }	    
	
			int status = con.getResponseCode();
	
			System.out.println("\nSending 'POST' request to URL : " + url);
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
			     
				 SmartApSelection res_parameters = GSON.fromJson(response.toString(),SmartApSelection.class);
			     
			     return res_parameters;
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
	


}
