import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Graph {

	public Queue<String> toSearch;
	public Set<Adjacency> adjacencies;
	public Set<String> adjacencyUrls;
	public Set<String> invalidLinks;
	public Set<String> validLinks;
	public Set<String> nodes;
	public Collection<String> topics;
	public StringBuilder stringFormat;
	public int requestCounter;

	public Graph(Collection<String> topics) {
		toSearch = new LinkedList<String>();
		adjacencies = new HashSet<Adjacency>();
		invalidLinks = new HashSet<String>();
		validLinks = new HashSet<String>();
		nodes = new HashSet<String>();
		adjacencyUrls = new HashSet<String>();
		this.topics = topics;
		stringFormat = new StringBuilder();
		requestCounter = 0;
	}

	public void crawlIteration(int max) throws IOException, InterruptedException {
		add(max, toSearch.remove());
	}

	public void add(int max, String url) throws IOException, InterruptedException {
		if (adjacencyUrls.contains(url))
			return;
		String doc = Util.curl(WikiCrawler.BASE_URL, url);
		String subdoc = Util.extractSubdoc(doc);
		if (!validPage(url, subdoc))
			return;
		Adjacency adj = new Adjacency(url);
		nodes.add(url);
		if (adjacencyUrls.contains(adj.url))
			return;
		adjacencyUrls.add(adj.url);
		adjacencies.add(adj);
		for (String child : Util.extractLinks(subdoc)) {
			if (!isValidPage(child))
				continue;
			if (nodes.size() > max) {
				if (nodes.contains(child))
					adj.children.add(child);
			} else {
				toSearch.add(child);
				nodes.add(child);
				stringFormat.append(url + " " + child + "\n");
			}
		}
	}

	public boolean cached(String url) {
		return validLinks.contains(url) || invalidLinks.contains(url);
	}

	public boolean isValidPage(String url) throws IOException, InterruptedException {
		if (validLinks.contains(url))
			return true;
		if (invalidLinks.contains(url))
			return false;
		if (requestCounter % 25 == 0 && requestCounter != 0)
			Thread.sleep(3000);
		requestCounter++;

		String doc;

		try {
			doc = Util.curl(WikiCrawler.BASE_URL, url);
		} catch (java.io.FileNotFoundException e) {
			return false;
		}

		return validatePage(Util.extractSubdoc(doc), url);
	}

	public boolean validPage(String url, String subdoc) throws IOException {
		if (validLinks.contains(url))
			return true;
		if (invalidLinks.contains(url))
			return false;
		return validatePage(subdoc, url);
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