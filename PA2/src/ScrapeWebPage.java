import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;

public class ScrapeWebPage implements Function<String, Collection<String>> {
	@Override
	public Collection<String> apply(String url) {
		Collection<String> result = new LinkedList<String>();
		try {
			for (String link : Util.extractLinks(Util.curl(url)))
				result.add(link);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}