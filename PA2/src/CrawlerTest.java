import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

public class CrawlerTest {

	@Test
	public void test() throws IOException {
		LinkedList<String> topics = new LinkedList<String>();
		topics.add("are");
		String doc = Util.curl(WikiCrawler.BASE_URL, "/wiki/Teletubbies");
		Collection<String> results = Util.extractLinks(topics, doc);
		assert (results.contains("/wiki/Pre-school"));
	}
}
