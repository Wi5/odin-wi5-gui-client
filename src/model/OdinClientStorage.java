package model;

import java.net.InetAddress;
import java.util.Date;

public class OdinClientStorage {
	
	private final String hwAddress;
	private InetAddress ipAddress;
	private String lvap;
	private int Channel;
	private String arr;
	private Double[] averageDBM;
	private Date lastScanInfo;
	
	public OdinClientStorage(String hwAddress, InetAddress ipAddress, String lvap, int channel, String arr,
			Double[] averageDBM, Date lastScanInfo) {
		this.hwAddress = hwAddress;
		this.ipAddress = ipAddress;
		this.lvap = lvap;
		Channel = channel;
		this.arr = arr;
		this.averageDBM = averageDBM;
		this.lastScanInfo = lastScanInfo;
		}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLvap() {
		return lvap;
	}

	public void setLvap(String lvap) {
		this.lvap = lvap;
	}

	public int getChannel() {
		return Channel;
	}

	public void setChannel(int channel) {
		Channel = channel;
	}

	public String getArr() {
		return arr;
	}

	public void setArr(String arr) {
		this.arr = arr;
	}

	public Double[] getAverageDBM() {
		return averageDBM;
	}

	public void setAverageDBM(Double[] averageDBM) {
		this.averageDBM = averageDBM;
	}

	public Date getLastScanInfo() {
		return lastScanInfo;
	}

	public void setLastScanInfo(Date lastScanInfo) {
		this.lastScanInfo = lastScanInfo;
	}

	public String getHwAddress() {
		return hwAddress;
	}


}
