import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Graph {

	public Queue<String> toSearch;
	public Set<Vertex> vertices;
	public Set<String> invalidLinks;
	public Set<String> validLinks;
	public Set<String> nodes;
	public Collection<String> topics;
	public int requestCounter;

	public Graph(Collection<String> topics) {
		toSearch = new LinkedList<String>();
		vertices = new HashSet<Vertex>();
		invalidLinks = new HashSet<String>();
		validLinks = new HashSet<String>();
		nodes = new HashSet<String>();
		this.topics = topics;
		requestCounter = 0;
	}

	public void crawlIteration(int max) throws IOException, InterruptedException {
		add(max, toSearch.remove());
	}

	public void add(int max, String url) throws IOException, InterruptedException {
		String subdoc = Util.extractSubdoc(Util.curl(WikiCrawler.BASE_URL, url));
		if (!isValidPage(url, subdoc))
			return;
		Vertex v = new Vertex(url);
		for (String child : Util.extractLinks(subdoc)) {
			if (!isValidPage(child))
				continue;
			v.children.add(child);
			toSearch.add(child);
			if (nodes.size() >= max)
				break;
		}
		vertices.add(v);
		nodes.add(url);
	}

	private boolean cached(String url) {
		return validLinks.contains(url) || invalidLinks.contains(url);
	}

	private boolean isValidPage(String url) throws IOException, InterruptedException {
		if (validLinks.contains(url))
			return true;
		if (invalidLinks.contains(url))
			return false;
		if (requestCounter % 25 == 0 && requestCounter != 0)
			Thread.sleep(3000);
		requestCounter++;
		String doc = Util.curl(WikiCrawler.BASE_URL, url);
		String subdoc = Util.extractSubdoc(doc);
		return validatePage(subdoc, url);
	}

	private boolean isValidPage(String url, String subdoc) throws IOException {
		if (validLinks.contains(url))
			return true;
		if (invalidLinks.contains(url))
			return false;
		return validatePage(subdoc, url);
	}

	private boolean validatePage(String subdoc, String url) {
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
		return "Graph [toSearch=" + toSearch + ", vertices=" + vertices + ", invalidLinks=" + invalidLinks
				+ ", validLinks=" + validLinks + ", topics=" + topics + ", requestCounter=" + requestCounter + "]";
	}

	public String stringFormat() {
		String result = "";
		for (Vertex v : vertices)
			result += v.stringFormat();
		return result;
	}
}