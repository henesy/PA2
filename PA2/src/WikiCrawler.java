/**
 * 
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 *
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {
	public static final String BASE_URL = "https://en.wikipedia.org";
	public String seedUrl;
	public int max;
	public ArrayList<String> topics;
	public String fileName;
	
	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName) {
		this.seedUrl = seedUrl;
		this.max = max;
		this.topics = topics;
		this.fileName = fileName;
	}
	
	/**
	 * This method will construct the web graph. If seedUrl does not contain all of the words from topics, then the
     * graph constructed is empty. If seedUrl does contain all words from topics, then a graph is generated
     */
	public void crawl() {
		// TODO
	}
	
}
