
public class Test {
	public static void main (String[] args) {
		QGraph graph = new QGraph ("sample.qgraph");
		
		// System.out.println(graph.getNNodes());
		graph.printNodes();
		graph.printWires();
		System.out.println("Le sommet v7 est de degré " + graph.degree("v7"));
		graph.printType ("v5");
		// graph.getNeighbors("v7");
		graph.printNeighbors("v13");
		graph.connected("v1", "v8");
		graph.connected("v1", "v9");
		graph.connected("v1", "v11");
		System.out.println(graph.angleValue("v5"));
		System.out.println(graph.type("v13"));
		System.out.println("Nombre de sommets rouges: " + graph.getNReds());
		graph.printReds();
		System.out.println("Nomdre de sommets verts: " + graph.getNGreens());
		graph.printGreens();
		System.out.println("Nomdre d'hadamards: " + graph.getNHadamards());
		graph.printHadamards();
		
		System.out.println("Parité Red: " + graph.PRed());
		System.out.println("Parité vert: " + graph.PGreen());
		
		System.out.println ("Node: " + graph.addNode(Type.GREEN, "") + " added.");
		
		graph.changeType("v8", Type.RED);
		graph.changeValue("v8", "1/2*\\pi");
	}
}
