
/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {
	public static final String BASE_URL = "https://en.wikipedia.org";
	public String seedUrl;
	public int max;
	public ArrayList<String> topics;
	public HashSet<String> visited;
	public String fileName;
	public int current;

	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName) {
		this.seedUrl = seedUrl;
		this.max = max;
		this.topics = topics;
		this.fileName = fileName;
	}

	/**
	 * This method will construct the web graph. If seedUrl does not contain all
	 * of the words from topics, then the graph constructed is empty. If seedUrl
	 * does contain all words from topics, then a graph is generated
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void crawl() throws IOException, InterruptedException {
		Graph g = new Graph(topics);
		g.add(max, seedUrl);
		while (g.nodes.size() < max && !g.toSearch.isEmpty())
			g.crawlIteration(max);
		System.out.println(g.stringFormat());
		Util.writeFile(fileName, g.stringFormat());
	}

	/**
	 * Takes in a string representation of a wikipedia page. This method returns
	 * a set of all the string represented links in the doc.
	 * 
	 * @param doc
	 *            The page to be ripped
	 * @return Set of links on the page
	 */
	private List<String> extractLinks(String doc) {
		List<String> links = new ArrayList<>();

		Pattern pattern = Pattern.compile("<p>(.*?)</p>");
		Matcher matcher = pattern.matcher(doc);
		String subdoc = null;

		if (!matcher.find()) {
			return links;
		}

		matcher.reset();
		while (matcher.find()) {
			subdoc += matcher.group(1);
		}

		pattern = Pattern.compile("<a href=\"(.*?)\"");
		matcher = pattern.matcher(subdoc);

		if (!matcher.find()) {
			return links;
		}

		matcher.reset();
		String substring = null;
		while (matcher.find()) {
			substring = matcher.group(1);
			if (!substring.contains(":") && !substring.contains("#") && !substring.contains("index.php?")) {
				links.add(substring);
			}
		}

		return links;
	}

}
