import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

public class ScrapeWebPage implements Function<String, Collection<String>> {
	@Override
	public Collection<String> apply(String url) {
		Collection<String> result = new LinkedList<String>();
		// try {
		// for (String link : Util.extractLinks(Util.curl(WikiCrawler.BASE_URL,
		// url)))
		// result.add(link);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return result;
	}
}
