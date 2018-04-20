import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph {

	public Queue<String> toSearch;
	public Map<String, Adjacency> adjacencies;
	public Map<String, String> docs;
	public Set<String> invalidLinks;
	public Set<String> validLinks;
	public Set<String> nodes;
	public Collection<String> topics;
	public StringBuilder stringFormat;
	public int requestCounter;

	public Graph(Collection<String> topics) {
		toSearch = new LinkedList<String>();
		adjacencies = new HashMap<String, Adjacency>();
		invalidLinks = new HashSet<String>();
		validLinks = new HashSet<String>();
		nodes = new HashSet<String>();
		docs = new HashMap<String, String>();
		this.topics = topics;
		stringFormat = new StringBuilder();
		requestCounter = 0;
	}

	public void crawlIteration(int max) throws IOException, InterruptedException {
		add(max, toSearch.remove());
	}

	public void add(int max, String url) throws IOException, InterruptedException {
		if (nodes.size() > max && !nodes.contains(url))
			return;
		if (adjacencies.containsKey(url) || !validPage(url))
			return;
		Adjacency adj = new Adjacency(url);
		nodes.add(url);
		adjacencies.put(url, adj);
		for (String child : Util.extractLinks(getDoc(url))) {
			if (nodes.size() > max) {
				if (nodes.contains(child)) {
					adj.children.add(child);
					stringFormat.append(url + " " + child + "\n");
				}
			} else {
				if (!isValidPage(child))
					continue;
				toSearch.add(child);
				nodes.add(child);
				adj.children.add(child);
				stringFormat.append(url + " " + child + "\n");
			}
		}
	}

	public String getDoc(String url) throws IOException, InterruptedException {
		if (requestCounter % 25 == 0 && requestCounter != 0)
			Thread.sleep(3000);
		requestCounter++;
		if (docs.containsKey(url))
			return docs.get(url);
		System.out.println(url);
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
			nodes.add(url);
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