import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

public class NetworkInfluence {

	public Graph graph;
	
	public NetworkInfluence(String graphData) {
		String file = "";
		String line;
		
		try {
			FileReader fileReader = new FileReader(graphData);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				file += line +"\n";
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.graph = Util.generateGraph(file);
		
	}

	/**
	 * Returns the out degree of a given vertex v
	 * 
	 * @param v
	 *            The vertex to find the out degree of
	 * @return The out degree of v
	 */
	public int outDegree(String v) {
		Adjacency adjacency = graph.adjacencies.get(v);
		return adjacency.children.size();
	}

	/**
	 * Returns the shortest path from given vertex u to given vertex v
	 * 
	 * @param u
	 *            The start vertex
	 * @param v
	 *            The end vertex
	 * @return shortest path from u to v
	 */
	public ArrayList<String> shortestPath(String u, String v) {
		Adjacency vert = Util.bfs(this.graph, u, v);
		ArrayList<String> shortestPath = Util.followBackUp(this.graph, u, v);
		Collections.reverse(shortestPath);
		return shortestPath;
	}

	/**
	 * Returns the distance from given vertex u to given vertex v
	 * 
	 * @param u
	 *            The start vertex
	 * @param v
	 *            The end vertex
	 * @return distance from u to v
	 */
	public int distance(String u, String v) {
		return Util.bfs(this.graph, u, v).length;
	}

	/**
	 * Returns the distance from given subset of vertices s to given vertex v
	 * 
	 * @param s
	 *            The start vertex
	 * @param v
	 *            The end vertex
	 * @return distance from s to v
	 */
	public int distance(ArrayList<String> s, String v) {
		List<Adjacency> smolGraph = new ArrayList<Adjacency>(s.size());
		for(String str : s) {
			smolGraph.add(Util.bfs(this.graph, str, v));
		}
		Adjacency smolAdj = smolGraph.get(0);
		for(int i = 1; i < smolGraph.size(); i++) {
			if(smolGraph.get(i).length < smolAdj.length) {
				smolAdj = smolGraph.get(i);
			}
		}
		return smolAdj.length;
	}

	/**
	 * Returns the influence of given vertex u
	 * 
	 * @param u
	 *            The vertex to get the influence of
	 * @return influence of u
	 */
	public float influence(String u) {
		return (float)graph.adjacencies.keySet().stream()
				.map((String url) -> {
					Util.bfs(graph, url, "");
					return graph.adjacencies.get(u).length;
				})
				.filter(x -> x > 0 && x < graph.adjacencies.size() + 1)
				.map(x -> 1 / Math.pow(2, x))
				.mapToDouble(x -> x.doubleValue())
				.sum();
	}

	/**
	 * Returns the influence of given set of vertices s
	 * 
	 * @param s
	 *            Set of vertices to get the influence of
	 * @return influence of s
	 */
	public float influence(ArrayList<String> s) {
		return 0;
		// TODO
	}

	/**
	 * Returns a set of k nodes as per nodes with the top k outdegrees (nodes reachable from said node)
	 * 
	 * @param k
	 *            Number of nodes to be in the resulting set
	 * @return resulting set of nodes
	 */
	public ArrayList<String> mostInfluentialDegree(int k) {
		int i;
		ArrayList<String> al = new ArrayList<String>();
		
		// Tuples go into the PQ when we have their total outdegree
		PriorityQueue<Tuple> pq = new PriorityQueue<>();

		for(String s : graph.adjacencies.keySet()) {
			int od = Util.countChildren(graph, s);
			Tuple t = new Tuple(s, od);
			pq.add(t);
		}
		
		for(i = 0; i < k && !pq.isEmpty(); i++)
			al.add(pq.remove().s);
		
		return al;
	}



	/**
	 * Returns a set of k nodes obtained by using Modular Greedy algorithm
	 * 
	 * @param k
	 *            Number of nodes to be in the resulting set
	 * @return resulting set of nodes
	 */
	public ArrayList<String> mostInfluentialModular(int k) {
		int i;
		ArrayList<String> al = new ArrayList<String>();
		
		// Tuples go into the PQ when we have their influence
		PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>();

		for(String s : graph.adjacencies.keySet()) {
			float od = influence(s);
			Tuple t = new Tuple(s, od);
			pq.add(t);
		}
		
		for(i = 0; i < k && !pq.isEmpty(); i++)
			al.add(pq.remove().s);
		
		return al;
	}

	/**
	 * Returns a set of k nodes obtained by using SubModular Greedy algorithm
	 * 
	 * @param k
	 *            Number of nodes to be in the resulting set
	 * @return resulting set of nodes
	 */
	public ArrayList<String> mostInfluentialSubModular(int k) {
		Set<String> set = new HashSet<String>();
		set.add(mostInfluentialModular(1).get(0));
		while(set.size() < k) {
			List<SimpleEntry<String, Float>> l = graph.adjacencies.keySet().stream()
			.filter(x -> !set.contains(x))
			.map(x -> {
				set.add(x);
				float influence = subModularInfluence(set);
				set.remove(x);
				return new SimpleEntry<String, Float>(x, influence);
			}).collect(Collectors.toCollection(LinkedList::new));
			SimpleEntry<String, Float> max = l.get(0);
			for(int i = 1; i < l.size(); i++) {
				if(l.get(i).getValue() > max.getValue())
					max = l.get(i);
			}
			set.add(max.getKey());
		}
		return set.stream().collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Stream<Float> allInfluences(String node, Set<String> doNotSearch) {
		return graph.adjacencies.keySet().stream()
				.filter(x -> !doNotSearch.contains(x))
				.map(x -> influence(x));
	}
	
	public float subModularInfluence(Set<String> set) {
		return (float)graph.adjacencies.keySet().stream()
			.filter(x -> !set.contains(x))
			.map(node -> allInfluences(node, set)
					.mapToDouble(x -> x.doubleValue())
					.max().getAsDouble())
			.mapToDouble(x -> x.doubleValue())
			.max().getAsDouble();
	}
}
