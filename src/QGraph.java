import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.ToDoubleBiFunction;

import javax.lang.model.element.NestingKind;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	// For randomly created vertices purpose:
	private static final int MAX_X = 6;
	private static final int MAX_Y = 4;
	
	// For randomly created boundaries:
	private static final int B_WIDTH = 10;
	private static final int B_HIGHT = 5;
	
	/*
	 * Constructors:
	 */
	
	public QGraph() {
		vertices = new ArrayList<Vertex>();
		boundaries = new ArrayList<Boundary>();
		edges = new ArrayList<Edge>();		
	}
	
	public QGraph(QGraph graph) {
		this.vertices = new ArrayList<Vertex>(graph.getVertices());
		this.boundaries = new ArrayList<Boundary>(graph.getBoundaries());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}
	
	public QGraph (String fileName) {
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
				v.setValue(""); // was "0".
			// Node with "data.value" empty --> angle = 0.
			else if (((JSONObject) ((JSONObject) node_vertices.get(v.getName())).get("data")).get("value").toString().equals(""))
				v.setValue(""); // was "0".
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
			b.setY(Float.parseFloat(((JSONArray) ((JSONObject) ((JSONObject) wire_vertices.get(b.getName())).get("annotation")).get("coord")).get(1).toString()));

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

	public ArrayList<Edge> getEdgesBetween (String abstractVertex1, String abstractVertex2) {
		ArrayList<Edge> e = new ArrayList<Edge>();
		Edge currentE;
		Iterator<Edge> iterator = edges.iterator();
		while (iterator.hasNext()) {
			currentE = iterator.next();
			if (currentE.getTarget().getName().equals(abstractVertex1) && currentE.getSource().getName().equals(abstractVertex2)) {
				e.add(currentE);
			}
			if (currentE.getTarget().getName().equals(abstractVertex2) && currentE.getSource().getName().equals(abstractVertex1)) {
				e.add(currentE);
			}
		}
		
		return e;
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
	
	public boolean exist (String element) {
		switch (element.charAt(0)) {
			case 'v':
				if (getVertex (element) != null)
					return true;
			case 'b':
				if (getBoundary (element) != null)
					return true;
			case 'e':
				if (getEdge (element) != null)
					return true;
			default:
				return false;
		}		
	}
	
	public boolean hasVertex (String abstractVertex) {
		if (getVertex (abstractVertex) != null)
			return true;
		return false;
	}
	
	public boolean hasBoundary (String abstractVertex) {
		if (getBoundary (abstractVertex) != null)
			return true;
		return false;
	}
	
	public boolean hasEdge (String edge) {		
		if (getEdge (edge) != null)
			return true;
		return false;
	}
	
	public boolean hasAbsrtacrtVertex (String abstractVertex) {
		return (hasVertex(abstractVertex) || hasBoundary(abstractVertex));
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
			
			if (current.getSource().getName().equals(abstractVertex) && current.getTarget().getName().equals(abstractVertex))
				degree += 2;
			else if (current.getSource().getName().equals(abstractVertex) || current.getTarget().getName().equals(abstractVertex))
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
		if (!hasVertex (vertex) || type (vertex) == Type.HADAMARD) // || is short-circuit
			return null;

		if (getVertex (vertex).getValue().equals(""))
			return new String ("0");
		
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
		
		if (neighbors(abstractVertex1) == null || neighbors(abstractVertex2) == null)
			return false;
		
		if (neighbors (abstractVertex1).contains(getAbstractVertex (abstractVertex2)) || neighbors (abstractVertex2).contains(getAbstractVertex (abstractVertex1)))
			return true;
		
		return false;
	}
	
	/*
	 * Writing methods:
	 */
	
	public void commit () {
		commit (this.fileName);
	}
	
	@SuppressWarnings("unchecked")
	public void commit (String fileName){ //TODO decide if to use local/Class JSONObjects.
		// Convert vertices (ArrayList<Vertex> to JSONObject):
		JSONObject node_vertices = new JSONObject ();
		Vertex currentV;
		
		Iterator<Vertex> iteratorV = vertices.iterator();
		while (iteratorV.hasNext()) {
			currentV = iteratorV.next();
			node_vertices.put(currentV.getName(), currentV.makeJSONObject());
		}
		
		// Convert boundaries:
		JSONObject wire_vertices = new JSONObject ();
		Boundary currentB;
		
		Iterator<Boundary> iteratorB = boundaries.iterator();
		while(iteratorB.hasNext()) {
			currentB = iteratorB.next();
			wire_vertices.put(currentB.getName(), currentB.makeJSONObject());
		}

		// Convert edges:
		JSONObject undir_edges = new JSONObject ();
		Edge currentE;

		Iterator<Edge> iteratorE = edges.iterator();
		while (iteratorE.hasNext()) {
			currentE = iteratorE.next();
			//System.out.println(currentE.getName());
			undir_edges.put(currentE.getName(), currentE.makeJSONObject());
			
		}
		
		// Write to the given file:
		try (FileWriter file = new FileWriter (fileName)) {
			JSONObject graph = new JSONObject ();
			graph.put("node_vertices", node_vertices);
			graph.put("wire_vertices", wire_vertices);
			graph.put("undir_edges", undir_edges);
			
			
			file.write(graph.toJSONString());
			file.close();
			System.out.println("Successfully Copied JSON Object to " + fileName + " ...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String addVertex (Vertex vertex) {
		vertices.add(vertex);
		
		System.out.println("Vertex (" + vertex.getName() + ") added to the graph.");
		
		return vertex.getName(); // TODO remove getName()
	}

	public String addVertex (Type type, String value, float x, float y) {
		int i;
		for (i = 0; exist (new String ("v" + i)); i++);
		
		Vertex vertex = new Vertex(new String("v" + i), type, value, x, y);

		return addVertex(vertex);
	}
	
	public String addVertex (Type type, String value) {
		// TODO can we auto generate coordinates ?
		return addVertex(type, value, 0, 0);
	}
	
	public String addVertex (Type type) {
		return addVertex(type, "");
	}
	
	public String addBoundary (Boundary boundary) {
		boundaries.add(boundary);
		
		System.out.println("boundary (" + boundary.getName() + ") added to the graph.");
		
		return boundary.getName();
	}
	
	public String addBoundary (float x, float y) {
		int i;
		for (i = 0; exist(new String("b" + i)); i++);
		
		Boundary boundary = new Boundary(new String("b" + i), true, x, y);
		
		return addBoundary(boundary);
	}
	
	public String addBoundary () {
		return addBoundary(0, 0);
	}

	public String addEdge (Edge edge) {
		edges.add(edge);
		
		System.out.println("Edge (" + edge.getName() + ") added to the graph.");

		return edge.getName();
	}
	
	public String addEdge (AbstractVertex target, AbstractVertex source) {
		// TODO hasEdge or something equivalent.
		int i;
		for (i = 0; exist(new String ("e" + i)); i++);
		
		Edge edge = new Edge(new String("e" + i), target, source);

		return addEdge(edge);
	}
	
	public String addEdge (String target, String source) {
		if (!hasAbsrtacrtVertex (target) || !hasAbsrtacrtVertex (source)) {
			System.out.println("Added edges must be between vertices and boundaries.");
			return null;
		}
		
		return addEdge(getAbstractVertex(target), getAbstractVertex(source));
	}
	
	public String addHadamard(){
		return addVertex(Type.HADAMARD, "");
	}
	
	public void addHadamardBetween2Vertices (String target, String source){
		if(! hasAbsrtacrtVertex(target) || ! hasAbsrtacrtVertex(source)) {
			System.out.println(target +" ou " +source +" n'existe pas");
			return ;
		}
		String hadamard = addHadamard();
		addEdge(target, hadamard);
		addEdge(hadamard, source);
	}
	
	public boolean remove (String something) {
		if (hasVertex(something)){
			isolate(something);
			vertices.remove(getVertex(something));
			System.out.println("Vertex " + something + " has been removed.");
			return true;
		} else if (hasBoundary(something)) {
			isolate(something);
			boundaries.remove(getBoundary(something));
			System.out.println("Boundary " + something + " has been removed.");
			return true;
		} else if (hasEdge(something)) {
			edges.remove(getEdge(something));
			System.out.println("Edge " + something + " has been removed.");
			return true;
		} else {
			System.out.println("Incorrect input."); // TODO change the msg.
			return false;
		}
	}
	
	public boolean disconnect (String abstractVertex1, String abstractVertex2) {
		if (!connected(abstractVertex1, abstractVertex2)) {
			System.out.println(abstractVertex1 + " and " + abstractVertex2 + " are not connected.");
			return false;
		}
		
		ArrayList<Edge> edgesBetween = getEdgesBetween(abstractVertex1, abstractVertex2);
		edges.remove(0);
		
		// TODO remove all edges between ? then add getEdges (a1,a2);
		
		
		return true;
	}
	
	public void disconnectAll (String abstractVertex1, String abstractVertex2) {
		ArrayList<Edge> edgesBetween = getEdgesBetween(abstractVertex1, abstractVertex2);
		Edge currentE;
		
		Iterator<Edge> iterator = edgesBetween.iterator();
		while (iterator.hasNext()) {
			currentE = iterator.next();
			edges.remove(currentE);
		}
	}
	
	public void isolate (String abstractVertex) {
		ArrayList<AbstractVertex> neighbors = neighbors(abstractVertex);
		
		Iterator<AbstractVertex> iterator = neighbors.iterator();
		while (iterator.hasNext()) {
			disconnectAll(abstractVertex, iterator.next().getName());
		}
	}

	public boolean check () {
		boolean valid = true;
		// Check vertices:
		Vertex currentV;
		
		Iterator<Vertex> iteratorV = vertices.iterator();
		while (iteratorV.hasNext()) {
			currentV = iteratorV.next();
			if (currentV.getType() == Type.HADAMARD && degree(currentV.getName()) != 2) {
				System.out.println("Hadamard's (" + currentV.getName() + ") degree must be exactly 2.");
				valid = false;
			}
		}
		
		// Check boundaries:
		Boundary currentB;
		
		Iterator<Boundary> iteratorB = boundaries.iterator();
		while (iteratorB.hasNext()) {
			currentB = iteratorB.next();
			if (degree (currentB.getName()) != 1) {
				System.out.println("Boundary's (" + currentB.getName() + ") degree must be exactly 1.");
				valid = false;
			}
		}
		
		// Check edges:
		Edge currentE;
		
		Iterator<Edge> iteratorE = edges.iterator();
		while (iteratorE.hasNext()) {
			currentE = iteratorE.next();
			if (! hasAbsrtacrtVertex (currentE.getTarget().getName())) {
				System.out.println("Edge's (" + currentE.getName() + ") target (" + currentE.getTarget().getName() + ") is not an existing Vertex or Boundary.");
				valid = false;
			}
			if (! hasAbsrtacrtVertex(currentE.getSource().getName())) {
				System.out.println("Edge's (" + currentE.getName() + ") source (" + currentE.getSource().getName() + ") is not an existing Vertex or Boundary.");
				valid = false;
			}
			
		}
		
		// If there's no problem "valid" stays true.
		return valid;
	}

	public Map<String, AbstractVertex> cat (QGraph graph) {
		if (!graph.check()) {
			System.out.println("Input graph isn't valid.");
			return null;
		}
		
		ArrayList<Vertex> newVertices = graph.getVertices();
		ArrayList<Boundary> newBoundaries = graph.getBoundaries();
		ArrayList<Edge> newEdges = graph.getEdges();
		
		// To keep trace of old names:
		HashMap<String, AbstractVertex> oldNamesTrace = new HashMap<>();
		
		// Adding vertices:
		Iterator<Vertex> iteratorV = newVertices.iterator();
		Vertex currentV;
		while (iteratorV.hasNext()) {
			currentV = iteratorV.next();
			String newName = addVertex(currentV.getType(), currentV.getValue(), currentV.getX(), currentV.getY());
			
			oldNamesTrace.put(currentV.getName(), getVertex(newName));
		}
		
		// Adding boundaries:
		Iterator<Boundary> iteratorB = newBoundaries.iterator();
		Boundary currentB;
		while (iteratorB.hasNext()) {
			currentB = iteratorB.next();
			String newName = addBoundary(currentB.getX(), currentB.getY());
			
			oldNamesTrace.put(currentB.getName(), getBoundary(newName));
		}
		
		// Adding edges:
		Iterator<Edge> iteratorE = newEdges.iterator();
		Edge currentE;
		while (iteratorE.hasNext()) {
			currentE = iteratorE.next();
			
			addEdge(oldNamesTrace.get(currentE.getTarget().getName()), oldNamesTrace.get(currentE.getSource().getName()));
		}
		return oldNamesTrace;	
	}

	public static QGraph cat (QGraph graph1, QGraph graph2) {
		QGraph graph = new QGraph(graph1);
		graph.cat(graph2);
		
		return graph;
	}
	
	//*
	public static QGraph cat (QGraph graph1, String [] b1, QGraph graph2, String [] b2) {
		if (b1 == null || b2 == null)
			return QGraph.cat(graph1, graph2);
		if (b1.length != b2.length)
			return null;
		if (b1.length == 0)
			return QGraph.cat(graph1, graph2);
		
		QGraph graph = new QGraph(graph1);
		Map<String, AbstractVertex> oldNamesTrace = graph.cat(graph2);
		
		//System.out.println("h" + b1[0]);
		for (int i =0; i < b1.length; i++) {
			graph.addEdge(graph.getVertex(graph.neighbors(b1[i]).get(0).getName()), graph.neighbors(oldNamesTrace.get(b2[i]).getName()).get(0));
			graph.remove(b1[i]);
			graph.remove(oldNamesTrace.get(b2[i]).getName());
		}
		
		return graph;
	}
	//*/
	
	/*
	 * @param p En prenant les sommets deux à deux, probabilité qu'ils serons connectés.
	 */
	public static QGraph random (int nVertices, int nBoundaries, double green, double red, double p) {
		// Checking parameters:
		if (p > 1 || p < 0) {
			System.out.println("Random: P must be between 0 and 1.");
			return null;
		}
		if (nVertices < 0 || nBoundaries < 0) {
			System.out.println("Random: Number of vertices and boundaries must be positif.");
			return null;
		}
		if (green > 1 || green < 0 || red > 1 || red < 0) {
			System.out.println("Random: parameters green (3rd) and red (4th)  must be between 0 and 1.");
			return null;
		}
		if (green + red > 1) {
			System.out.println("The addition of the parameters: green (3rd) and red (4th) must be inferior to 1.");
			return null;
		}
		if (nVertices == 0 && nBoundaries % 2 == 1) {
			System.out.println("Random: No solution for the input: (" + nVertices + "," + nBoundaries + "," + green + "," + red + "," + p + ").");
			return null;
		}
		if (nBoundaries % 2 == 1 && green == 0 && red == 0) {
			System.out.println("Random: No solution for the input: (" + nVertices + "," + nBoundaries + "," + green + "," + red + "," + p + ").");
			return null;
		}
		
		// Creating an empty graph: 
		QGraph graph = new QGraph();
		
		// Adding boundaries:
		for (int i = 0; i < nBoundaries; i++) {
			String name = graph.addBoundary();
			graph.getBoundary(name).setX((float) (B_WIDTH * Math.cos((2 * i * Math.PI)/nBoundaries)));
			graph.getBoundary(name).setY((float) (B_HIGHT * Math.sin((2 * i * Math.PI)/nBoundaries)));
			
		}
		
		// Adding random N vertices:
		double randomNumber;
		for (int i = 0; i < nVertices; i++) {
			Random random = new Random();
			Random random2 = new Random();
			randomNumber  = Math.random();
			int v = random2.nextInt(5);
			String value = new String();
			switch (v) {
				case 0:
					value = new String("-\\pi");
					break;
				case 1:
					value = new String("-\\pi/2");
					break;
				case 2:
					value = new String("");
					break;
				case 3:
					value = new String("\\pi/2");
					break;
				case 4:
					value = new String("\\pi");
					break;
				default:
					break;
			}
			if (randomNumber < green) {
				//graph.addVertex(Type.GREEN);
				graph.addVertex(Type.GREEN, value, 2*MAX_X*random.nextFloat() - MAX_X, 2*MAX_Y*random.nextFloat() - MAX_Y);
			}
			else if (randomNumber < (green + red)) {
				//graph.addVertex(Type.RED);
				graph.addVertex(Type.RED, value, 2*MAX_X*random.nextFloat() - MAX_X, 2*MAX_Y*random.nextFloat() - MAX_Y);
			}
			else {
				//graph.addVertex(Type.HADAMARD);
				graph.addVertex(Type.HADAMARD, "", 2*MAX_X*random.nextFloat() - MAX_X, 2*MAX_Y*random.nextFloat() - MAX_Y);
			}
		}
		
		// Handling a special case: nBoundaries impair and all generated vertices are Hadamards:
		// Change one hadamard randomly to a green or a red vertex
		if (nBoundaries % 2 == 1 && nVertices > 0){
			if (graph.getNReds() + graph.getNGreens() == 0) {
				Random random = new Random();
				int r = random.nextInt(2);

				if (r == 0)
					graph.getVertex(new String ("v" + random.nextInt(nVertices))).setType(Type.GREEN);
				else if (r == 1)
					graph.getVertex(new String ("v" + random.nextInt(nVertices))).setType(Type.RED);
				
			}
		}
		
		// Handling a special case: 1 Hadamard, nothing else:
		if (nVertices == 1 && !graph.getHadamards().isEmpty())
			graph.addEdge(graph.getHadamards().get(0), graph.getHadamards().get(0));
				
		// Connecting boundaries to random vertices:
		ArrayList<Vertex> CV;
		ArrayList<Boundary> CB;
		
		Random random = new Random();
		Iterator<Boundary> iterator = graph.getBoundaries().iterator();
		Boundary current;
		int rand;
		while (iterator.hasNext()) {
			current = iterator.next();
			CV = graph.connectableVertices();
			CB = graph.connectableBoundaries();
			
			if (graph.degree (current.getName()) > 0) {
				continue;
			}
			if (!CV.isEmpty()) {
					rand = random.nextInt(CV.size());
				graph.addEdge(current, CV.get(rand));
			}
			else {
				do {
					rand = random.nextInt(CB.size());
				} while (current.getName().equals(CB.get(rand).getName()));
				graph.addEdge(current, CB.get(rand));
			}
				
		}
		
		// Connecting vertices according to P:
		
		// Connecting Hadamards:
		ArrayList<Vertex> hadamards = graph.getHadamards();
		Iterator<Vertex> iterH;
		
		for (int i = 0; i < 2; i++) {
			iterH = hadamards.iterator();
			random = new Random();
			while (iterH.hasNext()) {
				Vertex v = iterH.next();
				CV = graph.connectableVertices();
				if (graph.degree(v.getName()) == 2)
					continue;
				
				else {
					do {
						rand = random.nextInt(CV.size());
					} while (v.getName().equals(CV.get(rand).getName()));
						graph.addEdge(v, CV.get(rand));
				}
			}
		}
	
		/*
		random = new Random();
		int r;
		
		ArrayList<Vertex> vertices = graph.getHadamards();
		Iterator<Vertex> iter = vertices.iterator();
		while (iter.hasNext()) {
			Vertex vertex = iter.next();
			if (vertex.getType() == Type.HADAMARD && graph.degree(vertex.getName()) == 2)
				continue;
			
			boolean found = false;
			do {
				r = random.nextInt(nVertices);
				Vertex v = graph.getVertex(new String("v" + r));
				//System.out.println(vertices.size() + " " + r + " " + nVertices);
				if (!(v.getType() == Type.HADAMARD && graph.degree(v.getName()) >= 2) &&
					!(v.getName().equals(new String("v" + r)) && graph.degree(v.getName()) == 1))
					found = true;
			}while (!found);
			
			graph.addEdge(vertex, graph.getVertices().get(r));	
		}
		//*/
		// connecting other vertices:
		for (int i = 0; i < nVertices; i++)
			for (int j = i; j < nVertices; j++) {
				if (graph.getVertex(new String("v" + i)).getType() == Type.HADAMARD ||
					graph.getVertex(new String("v" + j)).getType() == Type.HADAMARD)
					continue;
				
				else if (Math.random() < p)
					graph.addEdge(new String("v" + i), new String("v" + j));
			}
		return graph;
	}
	
	public ArrayList<AbstractVertex> connectables () {
		if (this.getNVertices() == 0 && this.getNBoundaries() == 0)
			return null;
		
		ArrayList<AbstractVertex> C = new ArrayList<AbstractVertex>();
		
		ArrayList<Vertex> vetices = this.getVertices();
		ArrayList<Boundary> boundaries = this.getBoundaries();
		
		Iterator<Vertex> iteratorV = vertices.iterator();
		Iterator<Boundary> iteratorB = boundaries.iterator();
		
		Boundary currentB;
		Vertex currentV;
		
		while (iteratorB.hasNext()) {
			currentB = iteratorB.next();
			if (this.degree(currentB.getName()) < 1)
				C.add(currentB);
		}
		
		while (iteratorV.hasNext()) {
			currentV = iteratorV.next();
			if (currentV.getType() == Type.HADAMARD && this.degree(currentV.getName()) >= 2)
				continue;
			else
				C.add(currentV);
		}
		
		return C;
	}

	public ArrayList<Vertex> connectableVertices() {
		ArrayList<AbstractVertex> connectables = this.connectables();
		
		ArrayList<Vertex> cVertices = new ArrayList<Vertex>();
		Iterator<AbstractVertex> iterator = connectables.iterator();
		while (iterator.hasNext()) {
			AbstractVertex current = iterator.next();
			if (this.hasVertex(current.getName()))
				cVertices.add((Vertex) current);
		}
		
		return cVertices;
	}
	
	public ArrayList<Boundary> connectableBoundaries() {
		ArrayList<AbstractVertex> connectables = this.connectables();
		
		ArrayList<Boundary> cVertices = new ArrayList<Boundary>();
		Iterator<AbstractVertex> iterator = connectables.iterator();
		while (iterator.hasNext()) {
			AbstractVertex current = iterator.next();
			if (this.hasBoundary(current.getName()))
				cVertices.add((Boundary) current);
		}
		
		return cVertices;
	}
}
