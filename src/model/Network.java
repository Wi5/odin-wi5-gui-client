package model;

import java.util.List;

public class Network {
	
	private List<AccessPoint> agents;
	
	private List<Client> clients;

	public Network() {
	}

	public List<AccessPoint> getAgents() {
		return agents;
	}

	public void setAgents(List<AccessPoint> agents) {
		this.agents = agents;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public Network(List<AccessPoint> agents, List<Client> clients) {
		super();
		this.agents = agents;
		this.clients = clients;
	}

}
