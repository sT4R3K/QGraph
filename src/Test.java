import java.util.*;


class Test {
	public static void main (String[] args) {
		QGraph graph = new QGraph ("sample.qgraph");
		
		System.out.println("Nombre de sommets dans le graphe: " + graph.getNVertices());
		System.out.println("Nombre d' E/S dans le graphe: " + graph.getNBoundaries());
		System.out.println("Nombre de aretes dans le graphe: " + graph.getNEdges());
		
		graph.printVertices();
		graph.printBoundaries();
		
		String vertex = new String("v2");
		
		// Vertex v = graph.getVertex(vertex);
		// System.out.println(v.getType());

		System.out.println("exist(" + vertex + "):" + graph.exist(vertex));
		System.out.println("Le degré de " + vertex + ": " + graph.degree(vertex));
		System.out.println("Le type de " + vertex + " est: " + graph.type(vertex));
		System.out.println("L'angle de " + vertex + " est: " + graph.Value(vertex));
		
		System.out.print("Nombre de sommets verts: " + graph.getNGreens() + " ; "); graph.printGreens();
		System.out.print("Nombre de sommets rouges: " + graph.getNReds() + " ; "); graph.printReds();
		System.out.print("Nombre d'hadamards: " + graph.getNHadamards() + " ; "); graph.printHadamards();
		
		System.out.println("Parité verte: " + graph.greenParity());
		System.out.println("Parité rouge: " + graph.redParity());
		
		graph.printNeighbors(vertex);

		System.out.println("Connected: " + graph.connected("v6", "b1"));
	}

}
