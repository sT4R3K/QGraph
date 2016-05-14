import java.util.*;


class Test {
	public static void main (String[] args) {
		//*
		QGraph graph = new QGraph ("sample.qgraph");
		
		System.out.println("Nombre de sommets dans le graphe: " + graph.getNVertices());
		System.out.println("Nombre d' E/S dans le graphe: " + graph.getNBoundaries());
		System.out.println("Nombre de aretes dans le graphe: " + graph.getNEdges());
		
		graph.printVertices();
		graph.printBoundaries();
		
		String vertex = new String("v7");
		
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
		
		System.out.print("Les voisins de " + vertex + " sont: ");
		graph.printNeighbors(vertex);

		System.out.println("Connected: " + graph.connected("v6", "b1"));
		
		String vString = graph.addVertex(Type.GREEN);
		String bString = graph.addBoundary();
		String eString = graph.addEdge(bString, vString);
		
		graph.addHadamardBetween2Vertices ("v6", "v0");
		
		graph.remove("v9");
		
		//graph.commit();
		
		/*
		Vertex v = new Vertex();
		v.setName("v5");
		
		QGraph graph2 = new QGraph();
		String v1 = graph2.addVertex(Type.GREEN);
		String v2 = graph2.addVertex(Type.RED);
		graph2.addEdge(v1, v2);
		//*/
		
		//System.out.println("Le graph a été vérifié: " + ((graph.check())?"valide.":"non valide."));
		
		/*
		QGraph graph2 = new QGraph();
		QGraph graph3 = new QGraph("sample.qgraph");
		graph2.cat(graph3);
	
		graph2.commit("out.qgraph");
		//*/
		
		QGraph graph4 = new QGraph();
		QGraph graph5 = new QGraph("sample.qgraph");
		
		String v1 = graph4.addVertex(Type.GREEN);
		String v2 = graph4.addVertex(Type.RED);
		String b1 = graph4.addBoundary();
		String b2 = graph4.addBoundary();
		graph4.addHadamardBetween2Vertices(v1, v2);
		graph4.addEdge(b1, v1);
		graph4.addEdge(b2, v2);
		
		graph4.commit("C:\\Users\\Tarek\\Desktop\\Master Informatique Lorraine\\S2\\Initiation à la recherche\\zx-project-1.1\\graphs\\4.qgraph");
		graph5.commit("C:\\Users\\Tarek\\Desktop\\Master Informatique Lorraine\\S2\\Initiation à la recherche\\zx-project-1.1\\graphs\\5.qgraph");
		QGraph.cat(graph4, graph5).commit("C:\\Users\\Tarek\\Desktop\\Master Informatique Lorraine\\S2\\Initiation à la recherche\\zx-project-1.1\\graphs\\cat_4_5.qgraph");
		
		String [] b_1 = new String[2];
		String [] b_2 = new String[2];
		b_1[0] = "b0";
		b_2[0] = "b0";
		b_1[1] = "b1";
		b_2[1] = "b1";
		QGraph.cat(graph4, b_1, graph5, b_2).commit("C:\\Users\\Tarek\\Desktop\\Master Informatique Lorraine\\S2\\Initiation à la recherche\\zx-project-1.1\\graphs\\cat_4_5_b.qgraph");
		
		QGraph qg = QGraph.random(45, 27, 0.3, 0.4, 0.1);
		qg.commit("C:\\Users\\Tarek\\Desktop\\Master Informatique Lorraine\\S2\\Initiation à la recherche\\zx-project-1.1\\graphs\\random.qgraph");
		System.out.println(qg.getNGreens());
		System.out.println(qg.getNReds());
		System.out.println(qg.getNEdges());
		
		// Cas impossible:
		//QGraph.random(10, 7, 0, 0, 1);
		
		/* 
		// connectables() test
		QGraph graph6 = new QGraph();
		graph6.addEdge(graph6.addBoundary(), graph6.addBoundary());
		String H = graph6.addVertex(Type.HADAMARD);
		graph6.addEdge(H, H);
		graph6.addBoundary();
		
		graph6.addEdge(graph6.addVertex(Type.GREEN), graph6.addVertex(Type.HADAMARD));
		graph6.commit("C:\\Users\\Tarek\\Desktop\\Master Informatique Lorraine\\S2\\Initiation à la recherche\\zx-project-1.1\\graphs\\graph6.qgraph");
		
		
		ArrayList<Vertex> g6c = graph6.connectableVertices();
		Iterator<Vertex> iterator = g6c.iterator();
		
		while (iterator.hasNext()) {
			System.out.print(iterator.next().getName() + " ");
			System.out.println();
		}
		//*/
	}
}
