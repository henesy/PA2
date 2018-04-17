import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	/**
	 * Fetches the web page at URL and returns it's string representation
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String curl(String url) throws IOException {
		String result = "";
		Scanner s = new Scanner(new URL(url).openStream());
		while (s.hasNextLine()) {
			result += s.nextLine();
		}
		s.close();
		return result;
	}

	/**
	 * Perform one iteration of a search algorithm
	 */
	public static <E> void SearchIteration(Set<E> visited, Collection<E> coll, Function<E, Collection<E>> search) {
		for (E e : coll) {
			if (!visited.contains(e))
				for (E ne : search.apply(e))
					coll.add(ne);
			visited.add(e);
		}
	}

	/**
	 * Takes in a string representation of a wikipedia page. This method returns
	 * a set of all the string represented links in the doc.
	 * 
	 * @param doc
	 *            The page to be ripped
	 * @return Set of links on the page
	 */
	public static List<String> extractLinks(String doc) {
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
			if (!(substring.contains(":")) && !(substring.contains("#"))) {
				links.add(substring);
			}
		}

		return links;
	}
}
