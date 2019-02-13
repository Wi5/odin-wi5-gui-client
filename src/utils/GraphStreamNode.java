package utils;

import model.AccessPoint;
import model.Client;

public class GraphStreamNode {
	
	private String id;
	private AccessPoint ap;
	private Client client;
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

	public boolean isAP() {
		return isAP;
	}

	public void setAP(boolean isAP) {
		this.isAP = isAP;
	}

	private boolean isAP;

	public GraphStreamNode(String id, AccessPoint ap, Client client, boolean isAP) {
		this.id=id;
		this.ap=ap;
		this.client=client;
		this.isAP = isAP;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
