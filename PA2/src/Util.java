import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
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
		StringBuilder result = new StringBuilder();
		Scanner s;
		try {
			s = new Scanner(new URL(base + url).openStream());
		} catch (Throwable t) {
			return "";
		}
		while (s.hasNextLine()) {
			result.append(s.nextLine());
		}
		s.close();
		return result.toString();
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
		StringBuilder subdoc = new StringBuilder();
		while (matcher.find()) {
			subdoc.append(matcher.group(1));
		}
		return subdoc.toString();
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

	public static void writeFile(String fileName, String string) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
		writer.write(string);
		writer.close();
	}
}
