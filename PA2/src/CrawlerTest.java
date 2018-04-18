import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

public class CrawlerTest {

	@Test
	public void test() throws IOException {
		String doc = Util.curl(WikiCrawler.BASE_URL, "/wiki/Teletubbies");
		String subdoc = Util.extractSubdoc(doc);
		Collection<String> results = Util.extractLinks(subdoc);
		assert (results.contains("/wiki/Pre-school"));
	}

	@Test
	public void containstopic() throws IOException {
		LinkedList<String> topics = new LinkedList<String>();
		topics.add("are");
		String doc = Util.curl(WikiCrawler.BASE_URL, "/wiki/Teletubbies");
		assert(Util.hasTopics(topics, doc));
		topics.add("mein");
		topics.add("kampf");
		assert(!Util.hasTopics(topics, doc));

	}
}
