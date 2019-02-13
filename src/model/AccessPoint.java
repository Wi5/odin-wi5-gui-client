package model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccessPoint {

	
	
	private String ipAddress;
	private String MAC;
	private String network;
	private int numberOfClients;
	private int channel;
	private int txpower;
	private long lastScan;
	private long lastHeard;

	public AccessPoint(String ipAddress, String MAC) {
		this.ipAddress = ipAddress;
		this.MAC = MAC;
		network="Red 1";
		numberOfClients = 1;
		channel= 1;
		txpower= 100;		
	}	
	
	public AccessPoint(String ipAddress, long lastHeard, String network, long lastScan, int Channel, int txpower) {
		this.ipAddress = ipAddress;
		this.MAC = null;
		this.network = network;
		this.numberOfClients = 0;
		channel = Channel;
		this.txpower = txpower;
		this.lastHeard = lastHeard;
		this.lastScan = lastScan;		
	}
	
	public String convertTime(long time){
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
	    return format.format(date);
	}
	
	public String getipAddress() {
		return ipAddress;
	}


	public void setipAddress(String iP) {
		this.ipAddress = iP;
	}


	public String getLastScan() {
		return convertTime(lastScan);
	}


	public void setLastScan(long lastScan) {
		this.lastScan = lastScan;
	}


	public String getLastHeard() {
		return convertTime(lastHeard);
	}


	public void setLastHeard(long lastHeard) {
		this.lastHeard =lastHeard;
	}


	public String getMAC() {
		return MAC;
	}


	public void setmAC(String mAC) {
		this.MAC = mAC;
	}


	public String getNetwork() {
		return network;
	}


	public void setNetwork(String network) {
		this.network = network;
	}


	public int getNumberOfClients() {
		return numberOfClients;
	}


	public void setNumberOfClients(int numberOfClients) {
		this.numberOfClients = numberOfClients;
	}


	public int getChannel() {
		return channel;
	}


	public void setChannel(int channel) {
		this.channel = channel;
	}


	public int getPotency() {
		return txpower;
	}


	public void setPotency(int potency) {
		this.txpower = potency;
	}



}
