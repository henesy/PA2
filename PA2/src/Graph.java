import java.util.Queue;
import java.util.Set;

public class Graph {
	public Queue<String> toSearch;
	public Set<Vertex> vertices;
	public Set<String> validPages;
	public Set<String> invalidPages;

	public boolean validPage(String url) {
		if (invalidPages.contains(url))
			return false;
		if (validPages.contains(url))
			return true;
		return validatePage(url);
	}

	private boolean validatePage(String url) {
		
		return false;
	}

	public void add(String url) {
		// TODO Auto-generated method stub
	}
}
