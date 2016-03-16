import java.io.FileReader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileWriter;
import java.io.IOException;


public class QGraph {
	@SuppressWarnings("unchecked")
	
	JSONParser parser;
	JSONObject graph;
	JSONObject wire_vertices;
	JSONObject node_vertices;
	JSONObject undir_edges;
	
	String fileName;
	
	QGraph (String fileName) {
		parser = new JSONParser ();
		try {
			Object obj = parser.parse(new FileReader (fileName));
			graph = (JSONObject) obj;
			
			wire_vertices = (JSONObject) graph.get ("wire_vertices");
			node_vertices = (JSONObject) graph.get ("node_vertices");
			undir_edges = (JSONObject) graph.get ("undir_edges");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.fileName = fileName;
	}
	
	int getNWires () {
		return wire_vertices.size();
	}
	
	int getNNodes () {
		return node_vertices.size();
	}
	
	int getNEdges () {
		return undir_edges.size();
	}
	
	boolean exist (String node) {
		if (node_vertices.get(node) == null)
			return false;
		else
			return true;
	}
	
	void printNodes () {
		Iterator iterator = node_vertices.keySet().iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()){
			System.out.print(iterator.next().toString() + " ");
		}
		
		System.out.println("]");
	}
	
	void printWires () {
		Iterator iterator = wire_vertices.keySet().iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()){
			System.out.print(iterator.next().toString() + " ");
		}
		
		System.out.println("]");
	}
	
	int degree (String node) {
		JSONObject current;
		int degree = 0;
		
		if (!exist (node)){
			System.out.println("This node does not exist.");
			return -1;
		}
		
		Iterator iterator = undir_edges.keySet().iterator();
		
		while (iterator.hasNext()) {
			current = (JSONObject) undir_edges.get(iterator.next());
			// current = (JSONObject) iterator.next();
			
			if (current.get("src").toString().equals(node) || current.get("tgt").toString().equals(node)) {
				// System.out.println(current.toString());
				degree++;
			}		
		}
		
		return degree;
	}
	
	String angleValue (String node) {
		if (!exist (node)){
			System.out.println("This node does not exist.");
			return null;
		}
		
		if (type (node) == Type.HADAMARD) {
			System.out.println("This node has no angle value (Hadamard).");
			return null;
		}
		
		// Green node without "data" --> angle = 0.
		if (!((JSONObject) node_vertices.get(node)).containsKey("data"))
			return new String ("0");
		
		// Node with "data.value" empty --> angle = 0.
		if (((JSONObject) ((JSONObject) node_vertices.get(node)).get("data")).get("value").toString().equals(""))
			return new String ("0");
		
		return ((JSONObject) ((JSONObject) node_vertices.get(node)).get("data")).get("value").toString();
		
	}

	Type type (String node) {
		if (!exist (node)){
			System.out.println("This node does not exist.");
			return null;
		}
		
		if (!((JSONObject) node_vertices.get(node)).containsKey("data"))
			return Type.GREEN;

		if (((JSONObject) ((JSONObject) node_vertices.get(node)).get("data")).get("type").toString().equals("X"))
			return Type.RED;
		else if (((JSONObject) ((JSONObject) node_vertices.get(node)).get("data")).get("type").toString().equals("Z"))
			return Type.GREEN;
		else
			return Type.HADAMARD;
	}
	
	//*
	void printType (String node) {
		if (type (node) == Type.RED)
			System.out.println(node + " is red.");
		else if (type (node) == Type.HADAMARD)
			System.out.println(node + " is an Hadamard.");
		else
			System.out.println(node + " is green.");
	}
	//*/
	
	private JSONObject [] m_getNeighbors (String node) {
		int degree = degree (node);
		if (degree == -1)
			return null;
		
		if (degree == 0) {
			System.out.println("Le sommet " + node + " est de degré 0.");
			return null;
		}
		
		JSONObject current;
		JSONObject [] neighbors = new JSONObject [degree];
		int i = 0;
		
		Iterator iterator = undir_edges.keySet().iterator();
		
		while (iterator.hasNext()) {
			current = (JSONObject) undir_edges.get(iterator.next());
			// current = (JSONObject) iterator.next();
			
			if (current.get("src").toString().equals(node) || current.get("tgt").toString().equals(node)) {
				// System.out.println(current.toString());
				neighbors [i] = current;
				i++;
			}			
		}
		
		return neighbors;
	}

	String[][] getNeighbors (String node) {
		String [][] neighbors = new String [degree (node)][2];
		JSONObject [] neighborsObjects = m_getNeighbors (node);
		
		if (neighborsObjects == null)
			return null;
		
		for (int i = 0; i < degree (node); i++) {
			// System.out.println(neighborsObjects [i]);
			if (neighborsObjects [i].get("src").toString().equals(node)) {
				neighbors [i][0] = neighborsObjects [i].get("tgt").toString();
				neighbors [i][1] = "target";
			}
			else if (neighborsObjects [i].get("tgt").toString().equals(node)) {
				neighbors [i][0] = neighborsObjects
						[i].get("src").toString();
				neighbors [i][1] = "source";
			}
		}
		
		return neighbors;
	}
	
	void printNeighbors (String node) {
		String [][] neighbors = getNeighbors (node);
		
		for (int i = 0; i < degree (node); i++) {
			System.out.println((i+1) + ": " + neighbors [i][0] + " (" + neighbors[i][1] + ").");
		}
	}
	
	boolean connected (String node1, String node2) {
		if (!exist (node1) || !exist (node2)){
			System.out.println("One or both nodes does not exist.");
			return false; // raise exception.
		}
		
		String [][] neighbors1 = getNeighbors (node1);
		
		for (int i = 0; i < degree (node1); i++) {
			if (neighbors1 [i][0].equals(node2)) {
				if (neighbors1 [i][1].equals("source"))
					System.out.println(node1 + " <-- " + node2);
				else
					System.out.println(node1 + " --> " + node2); 
				return true;
			}
		}
		
		System.out.println("Not connected.");
		return false;
	}

	int getNReds () {
		int nReds = 0;
		Iterator iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			if (type (iterator.next().toString()) == Type.RED)
				nReds++;		
		}
		
		return nReds;
	}
	
	int getNGreens () {
		int nGreens = 0;
		Iterator iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			if (type (iterator.next().toString()) == Type.GREEN)
				nGreens++;		
		}
		
		return nGreens;
	}
	
	int getNHadamards () {
		int nHadamards = 0;
		Iterator iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			if (type (iterator.next().toString()) == Type.HADAMARD)
				nHadamards++;		
		}
		
		return nHadamards;
	}
	
	String [] getReds () {
		if (getNReds() == 0)
			return null;
		
		String [] reds = new String [getNReds ()];
		int i = 0;
		
		Iterator iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			String current = (String) iterator.next();
			
			if (type (current) == Type.RED) {
				reds [i] = current;
				i++;
			}
		}
		
		return reds;
	}
	
	String [] getGreens () {
		if (getNGreens() == 0)
			return null;
		
		String [] greens = new String [getNGreens ()];
		int i = 0;
		
		Iterator iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			String current = (String) iterator.next();
			if (type (current) == Type.GREEN) {
				greens [i] = current;
				i++;
			}
		}
		
		return greens;
	}
	
	String [] getHadamards () {
		if (getNHadamards() == 0)
			return null;
		
		String [] hadamards = new String [getNHadamards ()];
		int i = 0;
		
		Iterator iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			String current = (String) iterator.next();
			if (type (current) == Type.HADAMARD) {
				hadamards [i] = current;
				i++;
			}
		}
		
		return hadamards;
	}
	
	void printReds () {
		String [] reds = getReds ();
		
		System.out.print("[ ");
		for (int i = 0; i < getNReds (); i++) {
			System.out.print(reds [i] + " ");
		}
		System.out.println("]");
	}
	
	void printGreens () {
		String [] greens = getGreens ();
		
		System.out.print("[ ");
		for (int i = 0; i < getNGreens (); i++) {
			System.out.print(greens [i] + " ");
		}
		System.out.println("]");
	}

	void printHadamards () {
		String [] hadamards = getHadamards ();
		
		System.out.print("[ ");
		for (int i = 0; i < getNHadamards (); i++) {
			System.out.print(hadamards [i] + " ");
		}
		System.out.println("]");
	}

	int PRed () {
		int S = 0;
		String [] reds = getReds ();
		
		for (int i = 0; i < getNReds (); i++) {
			S += degree(reds [i]);
		}
		
		return ((getNHadamards () + S) % 2);
	}
	
	int PGreen () {
		int S = 0;
		String [] greens = getGreens ();
		
		for (int i = 0; i < getNGreens (); i++) {
			S += degree(greens [i]);
		}
		
		return ((getNHadamards () + S) % 2);
	}
	
	/*
	 * -------------------------------------------------------- 
	 * 			Graph manipulation:
	 * --------------------------------------------------------
	 */
	
	private void writeToFile () {
		try (FileWriter file = new FileWriter (fileName)) {
			//graph.put("node_vertices", node_vertices);
			//graph.put("wire_vertices", wire_vertices);
			//graph.put("undir_edges", undir_edges);
			
			
			file.write(graph.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	 String  addNode (Type type, String value) {
		int i;
		for (i = 0; exist (new String ("v" + i)); i++);
		
		String id = new String ("v" + i);
		String typeString;

		switch (type) {
			case GREEN:
				typeString = new String ("Z");
				break;
			case RED:
				typeString = new String ("X");
				break;
			case HADAMARD:
				typeString = new String ("hadamard");
				break;
			default:
				return null;
		}
		
		String valueString = value;

		JSONObject data = new JSONObject ();
		(data).put ("type", typeString);
		data.put ("value", valueString);

		JSONArray coord = new JSONArray ();
		coord.add (0);
		coord.add (0);
		
		JSONObject annotation = new JSONObject ();
		annotation.put("coord", coord);
		
		JSONObject node = new JSONObject ();
		node.put ("data", data);
		node.put ("annotation", annotation);
		
		node_vertices.put(id, node);
		
		writeToFile ();
		
		return id;
	}
	 
	void changeType (String node, Type type) {
		if (!exist (node))
			return;
		
		if (type (node) == type)
			return;
		
		String typeString;

		switch (type) {
			case GREEN:
				typeString = new String ("Z");
				break;
			case RED:
				typeString = new String ("X");
				break;
			case HADAMARD:
				typeString = new String ("hadamard");
				break;
			default:
				return;
		}
		
		String valueString = angleValue (node);

		JSONObject data = new JSONObject ();
		(data).put ("type", typeString);
		data.put ("value", valueString);

		JSONArray coord = new JSONArray ();
		coord.add (((JSONArray) ((JSONObject) ((JSONObject) node_vertices.get(node)).get("annotation")).get("coord")).get(0));
		coord.add (((JSONArray) ((JSONObject) ((JSONObject) node_vertices.get(node)).get("annotation")).get("coord")).get(1));
		
		JSONObject annotation = new JSONObject ();
		annotation.put("coord", coord);
		
		JSONObject nodeObject = new JSONObject ();
		nodeObject.put ("data", data);
		nodeObject.put ("annotation", annotation);
		
		node_vertices.put(node, nodeObject);
		
		System.out.println("Node: " + node + "changed to " + type.toString() + ".");
		
		writeToFile ();
	}
	
	void changeValue (String node, String value) {
		if (!exist (node))
			return;
		
		if (angleValue (node) == value)
			return;
		
		String typeString;

		switch (type (node)) {
			case GREEN:
				typeString = new String ("Z");
				break;
			case RED:
				typeString = new String ("X");
				break;
			case HADAMARD:
				typeString = new String ("hadamard");
				break;
			default:
				return;
		}
		
		String valueString = value;

		JSONObject data = new JSONObject ();
		(data).put ("type", typeString);
		data.put ("value", valueString);

		JSONArray coord = new JSONArray ();
		coord.add (((JSONArray) ((JSONObject) ((JSONObject) node_vertices.get(node)).get("annotation")).get("coord")).get(0));
		coord.add (((JSONArray) ((JSONObject) ((JSONObject) node_vertices.get(node)).get("annotation")).get("coord")).get(1));
		
		JSONObject annotation = new JSONObject ();
		annotation.put("coord", coord);
		
		JSONObject nodeObject = new JSONObject ();
		nodeObject.put ("data", data);
		nodeObject.put ("annotation", annotation);
		
		node_vertices.put(node, nodeObject);
		
		System.out.println("Node: " + node + " value changed to " + value + ".");
		
		writeToFile ();
	}
}
