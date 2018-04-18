import java.util.ArrayList;

/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

public class NetworkInfluence {

	public NetworkInfluence(String graphData) {
		// TODO
	}
	
	/**
	 * Returns the out degree of a given vertex v
	 * @param v The vertex to find the out degree of
	 * @return The out degree of v
	 */
	public int outDegree(String v) {
		return 0;
		// TODO
	}
	
	/**
	 * Returns the shortest path from given vertex u to given vertex v
	 * @param u The start vertex
	 * @param v The end vertex
	 * @return shortest path from u to v
	 */
	public ArrayList<String> shortestPath(String u, String v) {
		return null;
		// TODO
	}
	
	/**
	 * Returns the distance from given vertex u to given vertex v
	 * @param u The start vertex
	 * @param v The end vertex
	 * @return distance from u to v
	 */
	public int distance(String u, String v) {
		return 0;
		// TODO
	}
	
	/**
	 * Returns the distance from given subset of vertices s to given vertex v
	 * @param s The start vertex
	 * @param v The end vertex
	 * @return distance from s to v
	 */
	public int distance(ArrayList<String> s, String v) {
		return 0;
		// TODO
	}
	
	/**
	 * Returns the influence of given vertex u
	 * @param u The vertex to get the influence of
	 * @return influence of u
	 */
	public float influence(String u) {
		return 0;
		// TODO
	}
	
	/**
	 * Returns the influence of given set of vertices s
	 * @param s Set of vertices to get the influence of
	 * @return influence of s
	 */
	public float influence(ArrayList<String> s) {
		return 0;
		// TODO
	}
	
	/**
	 * Returns a set of k nodes obtained by using Degree Greedy algorithm
	 * @param k Number of nodes to be in the resulting set
	 * @return resulting set of nodes
	 */
	public ArrayList<String> mostInfluentialDegree(int k) {
		return null;
		// TODO
	}
	
	/**
	 * Returns a set of k nodes obtained by using Modular Greedy algorithm
	 * @param k Number of nodes to be in the resulting set
	 * @return resulting set of nodes
	 */
	public ArrayList<String> mostInfluentialModular(int k) {
		return null;
		// TODO
	}
	
	/**
	 * Returns a set of k nodes obtained by using SubModular Greedy algorithm
	 * @param k Number of nodes to be in the resulting set
	 * @return resulting set of nodes
	 */
	public ArrayList<String> mostInfluentialSubModular(int k) {
		return null;
		// TODO
	}
}
