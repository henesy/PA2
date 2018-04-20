import java.util.HashSet;
import java.util.Set;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */
public class Adjacency {
	public String url;
	public Set<String> children;

	public Adjacency(String url) {
		this.url = url;
		children = new HashSet<String>();
	}

	@Override
	public String toString() {
		return "Vertex [url=" + url + ", children=" + children + "]";
	}

	public String stringFormat() {
		String result = "";
		for (String child : children)
			result += url + " " + child + "\n";
		return result;
	}

	@Override
	public boolean equals(Object o) {
		return ((Adjacency) o).url.equals(url);

	}
}