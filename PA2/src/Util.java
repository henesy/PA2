import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

public class Util {
	/**
	 * Fetches the web page at URL and returns it's string representation
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String curl(String base, String url) throws IOException {
		StringBuilder result = new StringBuilder();
		Scanner s;
		try {
			s = new Scanner(new URL(base + url).openStream());
		} catch (Throwable t) {
			return "";
		}
		while (s.hasNextLine()) {
			result.append(s.nextLine());
		}
		s.close();
		return result.toString();
	}

	/**
	 * Follows the shortest path back up from vertex v to vertex u
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	public static ArrayList<String> followBackUp(Graph graph, String u, String v) {
		Adjacency adj = graph.adjacencies.get(v);
		Collection<String> ret = new ArrayList<>();
		while (!adj.url.equals(u)) {
			String smol = adj.parents.get(0);
			for (String s : adj.parents) {
				if (graph.adjacencies.get(smol).length > graph.adjacencies.get(s).length) {
					smol = s;
				}
			}
			ret.add(smol);
			adj = graph.adjacencies.get(smol);
		}
		ret.add(u);
		return (ArrayList<String>) ret;
	}

	public static Adjacency bfs(Graph graph, String start, String destination) {
		int visitTime = 0;
		Queue<String> queue = new LinkedList<String>();
		for(Map.Entry<String, Adjacency> entry : graph.adjacencies.entrySet()) {
			entry.getValue().length = graph.adjacencies.size() + 1;
		}
		queue.add(start);
		graph.adjacencies.get(start).length = visitTime;
		visitTime++;
		
		while(!queue.isEmpty()) {
			String head = queue.remove();
			Adjacency adjacency = graph.adjacencies.get(head);
			for(String child : adjacency.children) {
				Adjacency childAdjacency = graph.adjacencies.get(child);
				
				if(childAdjacency != null && child == destination) {
					childAdjacency.length = visitTime;
					return childAdjacency;
				}
				if(childAdjacency != null && childAdjacency.length > visitTime) {
					childAdjacency.length = visitTime;
					queue.add(child);
				}
			}
		}
		return null;
	}
	
	public static Graph generateGraph(String file) {
		Graph graph = new Graph();
		Scanner scan = new Scanner(file);
		int numVertices = Integer.parseInt(scan.nextLine());
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] vertices = line.split(" ");
			if(graph.adjacencies.containsKey(vertices[0])) { //if has vertex in graph
				Adjacency adj = graph.adjacencies.get(vertices[0]); //gets vertex from graph
				if(graph.adjacencies.containsKey(vertices[1])) { //if has child in graph
					adj.children.add(vertices[1]); //puts child into vertex's children
					Adjacency child = graph.adjacencies.get(vertices[1]); //gets child from graph
					child.parents.add(vertices[0]); //puts vertex into child's parents
				} else { //if hasn't child in graph
					adj.children.add(vertices[1]); //adds child to vertex's children
					Adjacency child = new Adjacency(vertices[1]); //creates new adjacency for child
					child.parents.add(vertices[0]); //puts vertex into childs's parents
					graph.adjacencies.put(child.url, child); //puts child adjacency into graph
				}
			} else { //if hasn't vertex in graph
				Adjacency adj = new Adjacency(vertices[0]); //creates new adjacency for vertex
				if(graph.adjacencies.containsKey(vertices[1])) { //if has child in graph
					adj.children.add(vertices[1]); //adds child to new vertex's children
					Adjacency child = graph.adjacencies.get(vertices[1]); //gets child from graph
					child.parents.add(vertices[0]); //adds vertex to child's parents
				} else { //if hasn't child in graph
					adj.children.add(vertices[1]); //
					Adjacency child = new Adjacency(vertices[1]); //creates new adjacency for child
					child.parents.add(vertices[0]); //adds vertex to child's parents
					graph.adjacencies.put(child.url, child); //puts child adjacency into graph
				}
				graph.adjacencies.put(adj.url, adj); //puts vertex in graph
			}
		}
		return graph;
	}

	/**
	 * Git subdoc
	 * 
	 * @param doc
	 * @return
	 */
	public static String extractSubdoc(String doc) {
		Pattern pattern = Pattern.compile("<p>(.*?)</p>");
		Matcher matcher = pattern.matcher(doc);
		StringBuilder subdoc = new StringBuilder();
		while (matcher.find()) {
			subdoc.append(matcher.group(1));
		}
		return subdoc.toString();
	}

	/**
	 * 
	 * @param topics
	 * @param doc
	 * @return
	 */
	public static boolean hasTopics(Collection<String> topics, String doc) {
		Pattern pattern;
		Matcher matcher;
		for (String topic : topics) {
			pattern = Pattern.compile(topic);
			matcher = pattern.matcher(doc);
			if (!matcher.find())
				return false;
		}
		return true;
	}

	/**
	 * Takes in a string representation of a wikipedia page. This method returns
	 * a set of all the string represented links in the doc.
	 * 
	 * @param doc
	 *            The page to be ripped
	 * @return Set of links on the page
	 */
	public static List<String> extractLinks(String doc) {
		List<String> links = new ArrayList<>();
		Pattern pattern;
		Matcher matcher;

		pattern = Pattern.compile("<a href=\"(.*?)\"");
		matcher = pattern.matcher(doc);

		if (!matcher.find()) {
			return links;
		}

		matcher.reset();
		String substring = "";
		while (matcher.find()) {
			substring = matcher.group(1);
			if (!substring.contains(":") && !substring.contains("index.php?") && !substring.contains("#")) {
				links.add(substring);
			}
		}

		return links;
	}

	public static <E, V> Map<E, Set<V>> groupBy(Collection<V> coll, Function<V, E> f) {
		Map<E, Set<V>> m = new HashMap<E, Set<V>>();
		for (V v : coll) {
			E key = f.apply(v);
			Set<V> vs = m.containsKey(key) ? m.get(key) : new HashSet<V>();
			m.put(key, vs);
			vs.add(v);
		}
		return m;
	}

	public static void writeFile(String fileName, String string) throws IOException {
		Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), "utf-8");
		writer.write(string);
		writer.close();
	}
	
	// Recursively counts the number of children from a given 's' ;; this calculates the outdegree of s
	public static int countChildren(Graph g, String s) {
		// Outdegree is 0 if it connects to no other nodes
		int count = 0;
		count += g.adjacencies.get(s).children.size();
		for(String childString : g.adjacencies.get(s).children)
			count += countChildren(g, childString);
		return count;
	}
}
