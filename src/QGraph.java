import java.io.FileReader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;

public class QGraph {
	@SuppressWarnings("unchecked")
	
	private String fileName;
	
	private JSONParser parser;
	private JSONObject graph;
	private JSONObject node_vertices;
	private JSONObject wire_vertices;
	private JSONObject undir_edges;
	
	private ArrayList<Vertex> vertices;
	private ArrayList<Boundary> boundaries;
	private ArrayList<Edge> edges;
	
	/*
	 * Constructors:
	 */
	
	QGraph (String fileName) {
		this.fileName = fileName;

		parser = new JSONParser ();
		try {
			Object obj = parser.parse(new FileReader (fileName));
			graph = (JSONObject) obj;
			
			node_vertices = (JSONObject) graph.get ("node_vertices");
			wire_vertices = (JSONObject) graph.get ("wire_vertices");
			undir_edges = (JSONObject) graph.get ("undir_edges");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vertices = new ArrayList<Vertex>();
		boundaries = new ArrayList<Boundary>();
		edges = new ArrayList<Edge>();
		
		m_init ();
	}

	/*
	 * Accessors and reading methods:
	 */
	
	private void m_init () {
		// Vertices:
		Vertex v;
		
		Iterator<?> iterator = node_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			v = new Vertex ();
			
			// Set vertex's name:
			v.setName(iterator.next().toString());
			
			// Decide and set vertex's type:
			if (!((JSONObject) node_vertices.get(v.getName())).containsKey("data"))
				v.setType(Type.GREEN);
			
			else if (((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("data")).get("type").toString().equals("X"))
				v.setType(Type.RED);
			else if (((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("data")).get("type").toString().equals("Z"))
				v.setType(Type.GREEN);
			else
				v.setType(Type.HADAMARD);
			
			// Decide and set vertex's value:
			// Green node without "data" --> angle = 0.
			if (!((JSONObject) node_vertices.get(v.getName())).containsKey("data"))
				v.setValue("0");
			// Node with "data.value" empty --> angle = 0.
			else if (((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("data")).get("value").toString().equals(""))
				v.setValue("0");
			// Other cases:
			else
				v.setValue(((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("data")).get("value").toString());
			
			// Set coordinates:
			v.setX(Float.parseFloat(((JSONArray) ((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("annotation")).get("coord")).get(0).toString()));
			v.setY(Float.parseFloat(((JSONArray) ((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("annotation")).get("coord")).get(1).toString()));
			
			// Add Vertex v to vertices:
			vertices.add(v);
		}
		
		// Boundaries:
		Boundary b;
		
		iterator = wire_vertices.keySet().iterator();
		
		while (iterator.hasNext()) {
			b = new Boundary (iterator.next().toString());
			b.setBoundary(Boolean.parseBoolean(((JSONObject) ((JSONObject) wire_vertices.get(b.getName())).get("annotation")).get("boundary").toString()));
			
			b.setX(Float.parseFloat(((JSONArray) ((JSONObject) ((JSONObject) wire_vertices.get(b.getName())).get("annotation")).get("coord")).get(0).toString()));
			b.setY(Float.parseFloat(((JSONArray) ((JSONObject) ((JSONObject) wire_vertices.get(b.getName())).get("annotation")).get("coord")).get(0).toString()));

			boundaries.add(b);
		}
		
		// Edges:
		Edge e;
		
		iterator = undir_edges.keySet().iterator();
		
		while (iterator.hasNext()) {
			e = new Edge ();
			
			e.setName(iterator.next().toString());
			
			e.setTarget(getAbstractVertex (((JSONObject) undir_edges.get(e.getName())).get("tgt").toString()));
			e.setSource(getAbstractVertex (((JSONObject) undir_edges.get(e.getName())).get("src").toString()));
			
			edges.add(e);
		}
	}
		
	public Vertex getVertex (String vertex) {
		Vertex current = null;
		Iterator<Vertex> iterator = vertices.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getName().equals(vertex))
				return current;
		}

		return null;
	}
	
	public Boundary getBoundary (String boundary) {
		Boundary current = null;
		Iterator<Boundary> iterator = boundaries.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getName().equals(boundary))
				return current;
		}
		
		return null;
	}
	
	public AbstractVertex getAbstractVertex (String abstractVertex) {
		switch (abstractVertex.charAt(0)) {
			case 'v':
				return getVertex (abstractVertex);
			case 'b':
				return getBoundary (abstractVertex);
			default:
				return null;
		}
	}
	
	public Edge getEdge (String edge) {
		Edge current = null;
		Iterator<Edge> iterator = edges.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getName().equals(edge))
				return current;
		}
		
		return null;
	}
	
	public ArrayList<Vertex> getVertices () {
		return vertices;
	}
	
	public ArrayList<Boundary> getBoundaries () {
		return boundaries;
	}
	
	public ArrayList<Edge> getEdges () {
		return edges;
	}


	public int getNVertices () {
		return vertices.size();
	}
	
	public int getNBoundaries () {
		return boundaries.size();
	}
	
	public int getNEdges () {
		return edges.size();
	}

	public void printVertices () {
		Iterator<Vertex> iterator = vertices.iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()) {
			System.out.print(iterator.next().getName() + " ");
		}
		
		System.out.println("]");
	}
	
	public void printBoundaries () {
		Iterator<Boundary> iterator = boundaries.iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()) {
			System.out.print(iterator.next().getName() + " ");
		}
		
		System.out.println("]");
	}
	
	public boolean exist (String abstractVertex) {
		switch (abstractVertex.charAt(0)) {
			case 'v':
				if (getVertex (abstractVertex) != null)
					return true;
			case 'b':
				if (getBoundary (abstractVertex) != null)
					return true;
			default:
				return false;
		}		
	}
	
	public boolean isVertex (String abstractVertex) {
		if (getVertex (abstractVertex) != null)
			return true;
		return false;
	}
	
	public boolean isBoundary (String abstractVertex) {
		if (getBoundary (abstractVertex) != null)
			return true;
		return false;
	}
	
	public int degree (String abstractVertex) {
		if (! exist (abstractVertex)) {
			// System.out.println("This vertex does not exist.");
			return -1;
		}
		
		Edge current;
		int degree = 0;
		
		Iterator<Edge> iterator = edges.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			
			if (current.getSource().getName().equals(abstractVertex) || current.getTarget().getName().equals(abstractVertex))
				degree++;
		}

		return degree;
	}

	public Type type (String vertex) {
		Vertex current;
		Iterator<Vertex> iterator = vertices.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getName().equals(vertex))
				return current.getType();
		}

		return null;
	}
		
	public String Value (String vertex) {
		if (!isVertex (vertex) || type (vertex) == Type.HADAMARD) // || is short-circuit
			return null;

		return getVertex (vertex).getValue(); 
	}
	
	public ArrayList<Vertex> getGreens () {
		ArrayList<Vertex> greens = new ArrayList<Vertex>();
		Vertex current;
		
		Iterator<Vertex> iterator = vertices.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getType() == Type.GREEN)
				greens.add(current);
		}
		
		return greens;
	}
	
	public ArrayList<Vertex> getReds () {
		ArrayList<Vertex> reds = new ArrayList<Vertex>();
		Vertex current;
		
		Iterator<Vertex> iterator = vertices.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getType() == Type.RED)
				reds.add(current);
		}
		
		return reds;
	}
	
	public ArrayList<Vertex> getHadamards () {
		ArrayList<Vertex> hadamards = new ArrayList<Vertex>();
		Vertex current;
		
		Iterator<Vertex> iterator = vertices.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getType() == Type.HADAMARD)
				hadamards.add(current);
		}
		
		return hadamards;
	}
	
	public int getNGreens () {
		return getGreens ().size();
	}

	public int getNReds () {
		return getReds ().size();
	}

	public int getNHadamards () {
		return getHadamards ().size();
	}

	public void printGreens () {
		Iterator<Vertex> iterator = getGreens ().iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()) {
			System.out.print(iterator.next().getName() + " ");
		}
		
		System.out.println("]");
	}
	
	public void printReds () {
		Iterator<Vertex> iterator = getReds ().iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()) {
			System.out.print(iterator.next().getName() + " ");
		}
		
		System.out.println("]");
	}

	public void printHadamards () {
		Iterator<Vertex> iterator = getHadamards ().iterator();
		
		System.out.print("[ ");
		
		while (iterator.hasNext()) {
			System.out.print( iterator.next().getName() + " ");
		}
		
		System.out.println("]");
	}

	public int greenParity () {
		int S = 0;
	
		Iterator<Vertex> iterator = getGreens ().iterator();
		
		while (iterator.hasNext()) {
			S += degree (iterator.next().getName());
		}
		return ((getNHadamards () + S) % 2);		
	}
	
	public int redParity () {
		int S = 0;

		Iterator<Vertex> iterator = getReds ().iterator();
		
		while (iterator.hasNext()) {
			S += degree (iterator.next().getName());
	}
		return ((getNHadamards () + S) % 2);
	}

	public ArrayList<AbstractVertex> neighbors (String abstractVertex) {
		if (degree (abstractVertex) <= 0) // 0: no neighbors, -1: vertex doesn't exist.
			return null;
		
		ArrayList<AbstractVertex> neighbors = new ArrayList<AbstractVertex>();
		Edge current;
		
		Iterator<Edge> iterator = edges.iterator();
		
		while (iterator.hasNext()) {
			current = iterator.next();
			
			if (current.getSource().getName().equals(abstractVertex))
				neighbors.add (current.getTarget());
			else if (current.getTarget().getName().equals(abstractVertex))
				neighbors.add (current.getSource());				
		}
		
		return neighbors;
	}

	public void printNeighbors (String abstractVertex) {
		ArrayList<AbstractVertex> neighbors = neighbors (abstractVertex);
		
		if (neighbors == null) {
			System.out.println("No neighbors!");
			return;
		}
		
		System.out.print("[ ");
		
		Iterator<AbstractVertex> iterator = neighbors.iterator();
		
		while (iterator.hasNext())
			System.out.print(iterator.next().getName() + " ");
		
		System.out.println("]");
		
	}
	
	public boolean connected (String abstractVertex1, String abstractVertex2) {
		if (!exist (abstractVertex1) || !exist (abstractVertex2))
			return false;
		
		if (neighbors (abstractVertex1).contains(getAbstractVertex (abstractVertex2)) || neighbors (abstractVertex2).contains(getAbstractVertex (abstractVertex1)))
			return true;
		
		return false;
	}
	
	/*
	 * Writing methods:
	 */
	
	public void commit () {
		
	}
	
	public void addVertex (Vertex vertex) {
		vertices.add(vertex);
	}
	
	public void addBoundary (Boundary boundary) {
		boundaries.add(boundary);
	}
	
	public void addEdge (Edge edge) {
		edges.add(edge);
	}
	
}
