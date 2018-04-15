import java.awt.List;
import java.util.ArrayList;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */
public class Vertex {
	
	private String seedUrl;
	private Vertex parent;
	private ArrayList<String> children;
	
	public Vertex(String seedUrl) {
		this.seedUrl = seedUrl;
		this.parent = null;
		children = new ArrayList<String>();
	}
	
	public String getSeedUrl() {
		return this.seedUrl;
	}
	
	public Vertex getParent() {
		return this.parent;
	}
	
	public ArrayList<String> getChildren() {
		return this.children;
	}
	
	public void setParent(Vertex parent) {
		this.parent = parent;
	}
	
	public void addChild(String child) {
		this.children.add(child);
	}
	
}
