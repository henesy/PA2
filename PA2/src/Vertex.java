import java.util.HashSet;
import java.util.Set;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */
public class Vertex {
	public String url;
	public Set<String> children;

	public Vertex(String url) {
		this.url = url;
		children = new HashSet<String>();
	}

	@Override
	public String toString() {
		return "Vertex [url=" + url + ", children=" + children + "]";
	}
}
