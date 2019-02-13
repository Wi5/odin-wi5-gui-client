package model;

public class Client {

	
	
	private String ipAddress;
	private String macAddress;
	private String lvapSsid;
	private String agent;
	private String lvapBssid;
	private int channel;
	private double airTime;
	private int numberOfPackets;
	private double potency;
	private double[] averageDBM;


	public double[] getAverageDBM() {
		return averageDBM;
	}

	public void setAverageDBM(double[] averageDBM) {
		this.averageDBM = averageDBM;
	}

	public Client(String IP, String MAC) {
		this.ipAddress = IP;
		this.macAddress = MAC;
		lvapSsid="Red 1";
		agent = "54.121.154.232";
		channel= 1;
		potency= 100;
		
	}
	
	public Client(String IP, String MAC, String agent) {
		this.ipAddress = IP;
		this.macAddress = MAC;
		lvapSsid="Red 1";
		this.agent = agent;
		channel= 1;
		potency= 100;
		
	}
	
	public Client(String IP, String MAC, String lvapSsid, String agent, int Channel, double Potency) {
		this.ipAddress = IP;
		this.macAddress = MAC;
		this.lvapSsid = lvapSsid;
		this.agent = agent;
		channel = Channel;
		potency = Potency;
		
	}
	
	
	public String getiP() {
		return ipAddress;
	}


	public void setiP(String iP) {
		this.ipAddress = iP;
	}


	public String getmAC() {
		return macAddress;
	}


	public void setmAC(String mAC) {
		this.macAddress = mAC;
	}


	public String getNetwork() {
		return lvapSsid;
	}


	public void setNetwork(String network) {
		this.lvapSsid = network;
	}


	public String getAgent() {
		return agent;
	}


	public void setAgent(String agent) {
		this.agent = agent;
	}


	public int getChannel() {
		return channel;
	}


	public void setChannel(int channel) {
		this.channel = channel;
	}


	public double getPotency() {
		return potency;
	}


	public void setPotency(double potency) {
		this.potency = potency;
	}
	
	public String getLvapBssid() {
		return lvapBssid;
	}


	public void setLvapBssid(String lvapBssid) {
		this.lvapBssid = lvapBssid;
	}
}
