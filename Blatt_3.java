package blatt_3;
import java.io.*;
import java.util.Collections;
import java.util.LinkedList;

/* This program demonstrates some issues that have to be kept in mind when handing in code to the DomJudge system:
 * (1) Input to your program is done via system.in
 * (2) You have to put all your code into a single file
 * (3) You need to hand in a java file, NOT a jar
 * (4) Do not use German "Umlaute" or other special characters.
*/
 

/*
 * The class containing the main method is public
 */
public class Blatt_3 {

	static int nrOfNodes = 0;
	static int nrOfEdges = 0;
	
	public static void main(String[] args) throws IOException {
		// 
		// read from system.in
		//
		// you can test your program by calling
		// java -jar program.jar < input.txt
		//
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		myOutput out = new myOutput();
		
		if (in.ready()) {
			String s = in.readLine();
			nrOfNodes = Integer.valueOf(s.split(" ")[0]);
			nrOfEdges = Integer.valueOf(s.split(" ")[1]);
		}
		Node[] nodes = new Node[nrOfNodes];
		for (int i=0; i<nrOfNodes; i++) {
			if (in.ready()) {
				int h = Integer.valueOf(in.readLine());
				if (i == 0) {
					nodes[i] = new Node(i, 0, h);
				} else {
				    nodes[i] = new Node(i, Integer.MAX_VALUE/2, h);
				}
			}
		}
		
		Edge[] edges = new Edge[nrOfEdges];
		for (int i=0; i<nrOfEdges; i++) {
			if (in.ready()) {
				Node from = nodes[Integer.valueOf(in.readLine().split(" ")[0])];
				Node to = nodes[Integer.valueOf(in.readLine().split(" ")[1])];
				int cost = Integer.valueOf(in.readLine().split(" ")[0]);
				edges[i] = new Edge(from, to, cost);
			}
		}
		
		LinkedList<Node> closed = new LinkedList<Node>();
		LinkedList<Node> fringe = new LinkedList<Node>();
		fringe.add(nodes[0]);
		
		for (int i=0; i<nrOfNodes; i++) {
			if (fringe.isEmpty()) {
				System.out.println("failure");
				break;
			}
			Node node = fringe.poll();
			if (node.equals(nodes[nrOfNodes-1])) {
				closed.add(node);
				break;
			}
			if (!closed.contains(node)) {
				closed.add(node);
				insertToFringe(expand(node, nodes, edges), fringe);
			}
		}
		//
		// output something
		//
		out.println(closed);
	}

	private static LinkedList<Node> expand(Node node, Node[] nodes, Edge[] edges) {
		LinkedList<Node> expandedNeighbours = new LinkedList<Node>();
		for (int i=0; i<nrOfEdges; i++) {
			Edge edge = edges[i];
			if (edge.from.equals(node)) {
				edge.to.g = node.g + edge.cost;
				expandedNeighbours.add(edge.to);
			}
		}
		return expandedNeighbours;
	}
	
	private static void insertToFringe(LinkedList<Node> expanded, LinkedList<Node> fringe) {
		fringe.addAll(expanded);
		Collections.sort(fringe);
	}
}

/*
 * Other classes are not marked as "public"
 */
class myOutput {
	public void println(LinkedList<Node> closed) {
		for (int i=0; i<closed.size(); i++) {
			System.out.println(closed.get(i).nr);
		}
	}
}

class Node implements Comparable<Node>{
	int nr;
	int g; //actual costs from root
	int h; //heuristic 
	
	public Node(int nr, int g, int h) {
		this.nr = nr;
		this.g = g;
		this.h = h;
	}
	
	public int getF() {
		return g + h;
	}
	
	public int compareTo(Node n) {
		if (this.getF() < n.getF()) {
			return -1;
		} else if (this.getF() == n.getF()) {
			return 0;
		} else {
			return 1;
		}
	}
}

class Edge {
	Node from;
	Node to;
	int cost;
	
	public Edge(Node from, Node to, int cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}
}
