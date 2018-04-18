import java.awt.List;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */
public class Vertex {
	
	private String seedUrl;
	private String doc;
	private String parent;
	private Hashtable<String, String> children;
	
	public Vertex(String seedUrl) {
		this.seedUrl = seedUrl;
		this.parent = null;
		children = new Hashtable<String, String>();
	}
	
	public String getSeedUrl() {
		return this.seedUrl;
	}
	
	public String getParent() {
		return this.parent;
	}
	
	public Hashtable<String, String> getChildren() {
		return this.children;
	}
	
	public String getDoc() {
		return this.doc;
	}
	
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public void addChild(String child) {
		this.children.putIfAbsent(child, child);
	}
	
	public void setDoc(String doc) {
		this.doc = doc;
	}
	
}
