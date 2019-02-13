package controller.graphStreamController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.abego.treelayout.util.FixedNodeExtentProvider;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import httpRequestHandler.NetworkManagerResource;
import model.AccessPoint;
import model.Client;
import model.Network;
import utils.GraphStreamNode;

public class NetworkGraph {	
	
	
	public static Graph inicializeNetwork(Graph graph) {
			
		Network net = NetworkManagerResource.getNetwork();		
		DefaultTreeForTreeLayout<GraphStreamNode> NodeTree = GenerateTree(net);
		if(NodeTree==null) 
			return null;
		
		DefaultConfiguration<GraphStreamNode> configuration = new DefaultConfiguration<>(1, 1,Location.Bottom);
		TreeLayout<GraphStreamNode> treeLayout = new TreeLayout<GraphStreamNode>(NodeTree,new FixedNodeExtentProvider<GraphStreamNode>(), configuration);
		
		
  		String styleSheet = 
  				
				"graph {"
						+ "padding: 60px;"
				+ "}"
						
				+ "node {"
						+ "text-alignment: above;"
						+ "size: 15px;"
						+ "z-index: 1;"
				+ "}"
						
				+ "edge {"
				 	+ "text-alignment: along;"
				 	+ "text-background-mode: plain;"
					+ "size: 3px;"
					+ "z-index: 0;"
				+ "}"
					
				+ "node.agent {"
					+ "size: 30px;"
					+ "text-alignment: at-right;"
				 	+ "fill-image: url('./resources/ap.jpg'); "
				 	+ "fill-mode: image-scaled; "
				 	+ "shape: box;"
				 	+ "stroke-mode: plain;"
				 	+ "stroke-color: white;"
				 + "}"
				 	
				 + "node.client {"
				 	+ "text-alignment: under;"
				 	+ "fill-image: url('./resources/mobile_icon.jpg'); "
				 	+ "fill-mode: image-scaled;"
				 + "}"
				 	
				 + "node.controller {"
				 	+ "size: 30px;"	
					+ "fill-image: url('./resources/controlador.jpg'); "
					+ "fill-mode: image-scaled;"
				 + "}"
				 
				 + "edge.bad { fill-color: red;}"
				 + "edge.stable { fill-color: yellow;}" 
				 + "edge.good { fill-color: green;}"
				 + "edge.possible { fill-mode: none; size: 0px; stroke-color: yellow; stroke-mode: dots; stroke-width: 2px; }"; 
  		
	    graph.setAttribute("ui.stylesheet", styleSheet);	    
		
	    graph.addNode("controller");
	    graph.getNode("controller").setAttribute("ui.class", "controller");
	    
		Node controller = graph.getNode("controller");
		controller.setAttribute("ui.class", "controller");
		
		
		GraphStreamNode root = treeLayout.getTree().getRoot();
		controller.setAttribute("xy",treeLayout.getNodeBounds().get(root).getX(), treeLayout.getNodeBounds().get(root).getY());

		
        for(GraphStreamNode tx:treeLayout.getTree().getChildren(treeLayout.getTree().getRoot())){       	
        	
        	double x = treeLayout.getNodeBounds().get(tx).getX();
        	double y = treeLayout.getNodeBounds().get(tx).getY();
        	
        	AddNode(graph,NodeTree.getParent(tx), tx, x, y);
        	
        	if(tx.isAP()) {
        		for(GraphStreamNode cliente:treeLayout.getTree().getChildren(tx)) {
        			
                	double c_x = treeLayout.getNodeBounds().get(cliente).getX();
                	double c_y = treeLayout.getNodeBounds().get(cliente).getY();
                	
                	GraphStreamNode parent = NodeTree.getParent(cliente);
                	AddNode(graph,parent, cliente, c_x, c_y);
        		}
        	}       	
        	
        }   

		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.antialias");	


		 for (Node node : graph) {
		        node.setAttribute("ui.label", node.getId());
		 }
		 
		return graph;
	}
	
	
	public static DefaultTreeForTreeLayout<GraphStreamNode> GenerateTree(Network network) {	
		
		GraphStreamNode root;
		
		root = new GraphStreamNode("controller",new AccessPoint("192.168.13.19", "root"), null, true);
		DefaultTreeForTreeLayout<GraphStreamNode> tree = new DefaultTreeForTreeLayout<GraphStreamNode>(root);
		
		GraphStreamNode apNode;
		for(AccessPoint ap: network.getAgents()) {
			apNode = new GraphStreamNode(ap.getipAddress(), ap, null, true);
			tree.addChild(root, apNode);
			
			if(network.getClients() != null && network.getClients().size() != 0) {
				GraphStreamNode clientNode;
				for (Client children : network.getClients()) {
					if(children.getAgent().equals(ap.getipAddress())) {
						clientNode = new GraphStreamNode(children.getiP(), null, children, false);
						tree.addChild(apNode, clientNode);
					}
				}
			}
		}
		
		return tree;
	}
		
	private static void AddNode(Graph graph, GraphStreamNode parent, GraphStreamNode node, double x, double y) {
		
		if(node.isAP()) {
			//Agente
			String ap = node.getAp().getipAddress().toString();
			String NameParent = "controller";
			
			graph.addNode(ap);
			graph.addEdge(NameParent+"-"+ap,NameParent,ap);
			
			Node agente1 = graph.getNode(ap);
			agente1.setAttribute("type", "agent");
			agente1.setAttribute("xy",x,y);
			agente1.setAttribute("ui.class", "agent");  
			
			agente1.setAttribute("client",node.getAp());

		}
		else {
			//Cliente
			String client = node.getClient().getmAC();
			String NameParent = parent.getAp().getipAddress().toString();
			
			graph.addNode(client);
			graph.addEdge(NameParent+"-"+client,NameParent,client);
			
			Node agente1 = graph.getNode(client);
			agente1.setAttribute("type", "client");
			agente1.setAttribute("xy",x,y);
			agente1.setAttribute("ui.class", "client"); 
			
			agente1.setAttribute("client",node.getClient());
			
			String nameEdge= NameParent+"-"+client;
			double potency = node.getClient().getPotency();
			
			if(Math.abs(potency)<30) {
				graph.getEdge(nameEdge).setAttribute("ui.class", "good");
			}
			else if(Math.abs(potency)>80) {
				graph.getEdge(nameEdge).setAttribute("ui.class", "bad");
			}
			else {
				graph.getEdge(nameEdge).setAttribute("ui.class", "stable");
			}	
		}
		   
	}
	
	public static void ChangeEdgeColor(Graph graph, Client client) {
	
		double potency = client.getPotency();
		String agent = client.getAgent();
		String macAddress = client.getmAC();
		
		String nameEdge = agent+"-"+macAddress;
						
		graph.getEdge(nameEdge).setAttribute("ui.label", potency + "dbm");
		
		if(Math.abs(potency)<30) {
			graph.getEdge(nameEdge).setAttribute("ui.class", "good");
		}
		else if(Math.abs(potency)>80) {
			graph.getEdge(nameEdge).setAttribute("ui.class", "bad");
		}
		else {
			graph.getEdge(nameEdge).setAttribute("ui.class", "stable");
		}		
	}

	public static void HandleHandOff(Graph graph, Client client, String possible_new_agent) {
		
		String agent = client.getAgent();
		String macAddress = client.getmAC();
		
		Network net = NetworkManagerResource.getNetwork();		
		DefaultTreeForTreeLayout<GraphStreamNode> NodeTree = GenerateTree(net);
		if(NodeTree==null) 
			return;
		
		DefaultConfiguration<GraphStreamNode> configuration = new DefaultConfiguration<>(1, 1,Location.Bottom);
		TreeLayout<GraphStreamNode> treeLayout = new TreeLayout<GraphStreamNode>(NodeTree,new FixedNodeExtentProvider<GraphStreamNode>(), configuration);
		
        for(GraphStreamNode tx:treeLayout.getTree().getChildren(treeLayout.getTree().getRoot())){       	
        	
        	if(tx.isAP()) {
        		for(GraphStreamNode cliente:treeLayout.getTree().getChildren(tx)) {
        			
                	double c_x = treeLayout.getNodeBounds().get(cliente).getX();
                	double c_y = treeLayout.getNodeBounds().get(cliente).getY();
                	
                	GraphStreamNode parent = NodeTree.getParent(cliente);
                	MoveNode(graph,parent, cliente, c_x, c_y);
        		}
        	}       	
        	
        }   
		
		HandleHandOffEgde(graph, client, possible_new_agent);

	}
	
	
	private static void MoveNode(Graph graph, GraphStreamNode parent, GraphStreamNode node, double x, double y) {
		String client = node.getClient().getmAC();
		String NameParent = parent.getAp().getipAddress().toString();
		
		Node agente1 = graph.getNode(client);
		if(agente1== null) return;
		agente1.setAttribute("xy",x,y);
	}


	public static void HandleHandOffEgde(Graph graph, Client client, String possible_new_agent) {
		double potency = client.getPotency();
		String agent = client.getAgent();
		String macAddress = client.getmAC();
		
		String nameEdge = agent+"-"+macAddress;
						
		graph.removeEdge(nameEdge);
		
		//Add new Egde
		String name_newEdge = possible_new_agent+"-"+macAddress;
		graph.addEdge(name_newEdge,possible_new_agent,macAddress);
		
		graph.getEdge(name_newEdge).setAttribute("ui.label", potency + "dbm");
						
		if(Math.abs(potency)<30) {
			graph.getEdge(name_newEdge).setAttribute("ui.class", "good");
		}
		else if(Math.abs(potency)>80) {
			graph.getEdge(name_newEdge).setAttribute("ui.class", "bad");
		}
		else {
			graph.getEdge(name_newEdge).setAttribute("ui.class", "stable");
		}
		
		removePossibleEdges(graph, client);
		
	}


	private static void removePossibleEdges(Graph graph, Client client) {
		List<Edge> edgesToRemove = new ArrayList<>();
		String objetive = client.getmAC()+"-possible";
		for(int i = 0; i < graph.getEdgeCount(); i++) {
			Edge ed = graph.getEdge(i);
			if(ed.getId().contains(objetive)) {
				edgesToRemove.add(ed);
			}
		}
		
		for (Edge edge : edgesToRemove) {
			graph.removeEdge(edge);
		}
			
	}


	public static void printOrRefreshPossibleHandoff(Client client, double[] averageDBM, Map<String, Integer> agents, int id, Graph graph) {
		double actualDBM = averageDBM[id];
		
		Map<String, Double> possibleNewAgents = new HashMap<String, Double>();
		
		double difference = 10.0;
		
		//Buscamos valores mejores o cercanos
		for (int j = 0; j < averageDBM.length; j++) {
			if(j==id) continue;
			double d = averageDBM[j];
			double tmp = d+difference;
			String agent =getKeyByValue(agents, j);
			if(tmp>actualDBM) possibleNewAgents.put(agent.substring(1),tmp);
			
			printPossibleEdges(client, possibleNewAgents, graph);
		}
	}
	
	private static void printPossibleEdges(Client client, Map<String, Double> possibleNewAgents, Graph graph) {
		for (Map.Entry<String, Double> entry : possibleNewAgents.entrySet())
		{
			printPossibleNewAgent(graph, client, entry.getKey(), entry.getValue());
		}
	}

	
	public static void printPossibleNewAgent(Graph graph, Client client, String possible_new_agent, double averageDBM) {
		double potency = client.getPotency();
		String macAddress = client.getmAC();
		
		//Add new Egde
		String name_newEdge = possible_new_agent+"-"+macAddress+"-possible";
		Edge old = graph.getEdge(name_newEdge);
		if(old==null) graph.addEdge(name_newEdge,possible_new_agent,macAddress);
				
		graph.getEdge(name_newEdge).setAttribute("ui.label", potency + "dbm");
		graph.getEdge(name_newEdge).setAttribute("ui.class", "possible");
	
	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}	
}
