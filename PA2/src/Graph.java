import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

public class Graph {

	public Queue<String> toSearch;
	public Map<String, Adjacency> adjacencies;
	public Map<String, String> docs;
	public Set<String> invalidLinks;
	public Set<String> validLinks;
	public Set<String> visited;
	public Collection<String> topics;
	public StringBuilder stringFormat;
	public int requestCounter;

	public Graph(Collection<String> topics) {
		toSearch = new LinkedList<String>();
		adjacencies = new Hashtable<String, Adjacency>();
		invalidLinks = new HashSet<String>();
		validLinks = new HashSet<String>();
		visited = new HashSet<String>();
		docs = new HashMap<String, String>();
		this.topics = topics;
		stringFormat = new StringBuilder();
		requestCounter = 0;
	}
	
	public Graph() {
		toSearch = new LinkedList<String>();
		adjacencies = new Hashtable<String, Adjacency>();
		invalidLinks = new HashSet<String>();
		validLinks = new HashSet<String>();
		visited = new HashSet<String>();
		docs = new HashMap<String, String>();
		stringFormat = new StringBuilder();
		requestCounter = 0;
	}

	public void crawlIteration(int max) throws IOException, InterruptedException {
		add(max, toSearch.remove());
	}

	public void add(int max, String url) throws IOException, InterruptedException {
		if (maxedOutNodes(max) && !nodeMarked(url) || adjacencies.containsKey(url) || !validPage(url))
			return;
		Adjacency adj = new Adjacency(url);
		markNode(url);
		adjacencies.put(url, adj);
		addChildren(max, adj);
	}

	public void addChildren(int max, Adjacency adj) throws IOException, InterruptedException {
		for (String child : Util.extractLinks(getDoc(adj.url)))
			addChild(max, child, adj);
	}

	public void addToAdjacency(Adjacency adj, String child) {
		adj.children.add(child);
		stringFormat.append(adj.url + " " + child + "\n");
	}

	public void addChild(int max, String child, Adjacency adj) throws IOException, InterruptedException {
		if (maxedOutNodes(max)) {
			if (visited.contains(child))
				addToAdjacency(adj, child);
			return;
		}
		if (!isValidPage(child))
			return;
		toSearch.add(child);
		visited.add(child);
		addToAdjacency(adj, child);
	}

	public boolean maxedOutNodes(int max) {
		return visited.size() > max;
	}

	public boolean nodeMarked(String url) {
		return visited.contains(url);
	}

	public void markNode(String url) {
		visited.add(url);
	}

	public String getDoc(String url) throws IOException, InterruptedException {
		if (requestCounter % 25 == 0 && requestCounter != 0)
			Thread.sleep(3000);
		requestCounter++;
		if (docs.containsKey(url))
			return docs.get(url);
		String subdoc = Util.extractSubdoc(Util.curl(WikiCrawler.BASE_URL, url));
		docs.put(url, subdoc);
		return subdoc;
	}

	public boolean cached(String url) {
		return validLinks.contains(url) || invalidLinks.contains(url);
	}

	public boolean isValidPage(String url) throws IOException, InterruptedException {
		if (validLinks.contains(url))
			return true;
		if (invalidLinks.contains(url))
			return false;

		return validatePage(getDoc(url), url);
	}

	public boolean validPage(String url) throws IOException, InterruptedException {
		if (validLinks.contains(url))
			return true;
		if (invalidLinks.contains(url))
			return false;
		return validatePage(getDoc(url), url);
	}

	public boolean validatePage(String subdoc, String url) {
		if (Util.hasTopics(topics, subdoc)) {
			validLinks.add(url);
			visited.add(url);
			return true;
		}
		invalidLinks.add(url);
		return false;
	}

	@Override
	public String toString() {
		return "Graph [toSearch=" + toSearch + ", vertices=" + adjacencies + ", invalidLinks=" + invalidLinks
				+ ", validLinks=" + validLinks + ", topics=" + topics + ", requestCounter=" + requestCounter + "]";
	}

	public String stringFormat() {
		return stringFormat.toString();
	}
}