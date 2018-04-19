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
	public static String curl(String base, String url) throws IOException {
		String result = "";
		Scanner s = new Scanner(new URL(base + url).openStream());
		while (s.hasNextLine()) {
			result += s.nextLine();
		}
		s.close();
		return result;
	}

	/**
	 * Git subdoc
	 * 
	 * @param doc
	 * @return
	 */
	public static String extractSubdoc(String doc) {
		Pattern pattern = Pattern.compile("<p>(.*?)</p>");
		Matcher matcher = pattern.matcher(doc);
		String subdoc = "";
		while (matcher.find()) {
			subdoc += matcher.group(1);
		}
		return subdoc;
	}

	/**
	 * 
	 * @param topics
	 * @param doc
	 * @return
	 */
	public static boolean hasTopics(Collection<String> topics, String doc) {
		Pattern pattern;
		Matcher matcher;
		for (String topic : topics) {
			pattern = Pattern.compile(topic);
			matcher = pattern.matcher(doc);
			if (!matcher.find())
				return false;
		}
		return true;
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
		Pattern pattern;
		Matcher matcher;

		pattern = Pattern.compile("<a href=\"(.*?)\"");
		matcher = pattern.matcher(doc);

		if (!matcher.find()) {
			return links;
		}

		matcher.reset();
		String substring = "";
		while (matcher.find()) {
			substring = matcher.group(1);
			if (!substring.contains(":") && !substring.contains("index.php?") && !substring.contains("#")) {
				links.add(substring);
			}
		}

		return links;
	}

	public static void writeFile(String fileName, String string) {
		// TODO Auto-generated method stub
		
	}
}
