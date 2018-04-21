import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.junit.Test;

public class CrawlerTest {

	public static Graph dummyGraph() {
		Graph gr = new Graph(new LinkedList<>());
		Adjacency a = new Adjacency("A");
		Adjacency b = new Adjacency("B");
		Adjacency c = new Adjacency("C");
		Adjacency d = new Adjacency("D");
		Adjacency e = new Adjacency("E");
		Adjacency f = new Adjacency("F");
		Adjacency g = new Adjacency("G");
		Adjacency h = new Adjacency("H");

		gr.adjacencies.put("A", a);
		gr.adjacencies.put("B", b);
		gr.adjacencies.put("C", c);
		gr.adjacencies.put("D", d);
		gr.adjacencies.put("E", e);
		gr.adjacencies.put("F", f);
		gr.adjacencies.put("G", g);
		gr.adjacencies.put("H", h);
		
		a.children.add("B");
		a.children.add("C");
		a.children.add("D");
		
		b.children.add("I");
		b.children.add("J");
		
		c.children.add("E");
		c.children.add("F");
		c.children.add("B");
		c.children.add("D");
		
		
		d.children.add("G");
		d.children.add("H");
		d.children.add("A");
	
		e.children.add("A");
		e.children.add("A");
		
		a.length = 0;
		b.length = 1;
		c.length = 1;
		d.length = 1;
		e.length = 2;
		f.length = 2;
		g.length = 2;
		h.length = 2;
		
		return gr;
	}
	
	@Test
	public void subdoc() throws IOException {
		String doc = Util.curl(WikiCrawler.BASE_URL, "/wiki/Teletubbies");
		String subdoc = Util.extractSubdoc(doc);
		Collection<String> results = Util.extractLinks(subdoc);
		assert (results.contains("/wiki/Pre-school"));
	}
	
	@Test
	public void influence() {
		Graph g = dummyGraph();
	}
	
	@Test
	public void bfs() {
		Graph g = dummyGraph();
		Util.bfs(g, "A", "H");
	}

	@Test
	public void containstopic() throws IOException {
		LinkedList<String> topics = new LinkedList<String>();
		topics.add("are");
		String doc = Util.curl(WikiCrawler.BASE_URL, "/wiki/Teletubbies");
		assert (Util.hasTopics(topics, doc));
		topics.add("mein");
		topics.add("kampf");
		assert (!Util.hasTopics(topics, doc));
	}

	@Test
	public void addOne() throws IOException, InterruptedException {
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("loons");
		Graph g = new Graph(topics);
		g.add(3, "/wiki/Duck");
	}

	@Test
	public void add3() throws IOException, InterruptedException {
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("are");
		WikiCrawler c = new WikiCrawler("/wiki/Gforth", 10, topics, "foo.txt");
		c.crawl();
	}

	@Test
	public void benchmark() throws IOException {
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("Iowa State");
		topics.add("Cyclones");
		Graph g = new Graph(topics);

		long startTime = System.nanoTime();
		String doc = Util.curl(WikiCrawler.BASE_URL, "/wiki/Iowa_State_University");
		long endTime = System.nanoTime();
		System.out.println("curl: " + (float) (endTime - startTime) / 1000000);

		startTime = System.nanoTime();
		String subdoc = Util.extractSubdoc(doc);
		endTime = System.nanoTime();
		System.out.println("subdoc: " + (float) (endTime - startTime) / 1000000);

		startTime = System.nanoTime();
		Util.extractLinks(subdoc);
		endTime = System.nanoTime();
		System.out.println("extract links: " + (float) (endTime - startTime) / 1000000);

		startTime = System.nanoTime();
		g.validatePage(subdoc, "/wiki/Iowa_State_University");
		endTime = System.nanoTime();
		System.out.println("validate page: " + (float) (endTime - startTime) / 1000000);
	}

	@Test
	public void big12() throws IOException {
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("Iowa State");
		topics.add("Cyclones");

		String doc = Util.extractSubdoc(Util.curl(WikiCrawler.BASE_URL, "/wiki/Big_12_Conference"));
		System.out.println(Pattern.compile("Cyclones").matcher(doc).find());
	}

	@Test
	public void numEdges() throws IOException, InterruptedException {
		ArrayList<String> topics = new ArrayList<String>();
		WikiCrawler c = new WikiCrawler("/wiki/Complexity_theory", 100, topics, "complexity.txt");
		c.crawl();
	}

	@Test
	public void isu() throws IOException, InterruptedException {
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("Iowa State");
		topics.add("Cyclones");
		WikiCrawler c = new WikiCrawler("/wiki/Iowa_State_University", 100, topics, "isu.txt");
		c.crawl();
	}
}
