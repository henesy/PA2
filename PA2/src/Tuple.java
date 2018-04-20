/**
 * @author Tyler Fenton
 * @author Sean Hinchee
 * @author Ryan Radomski
 */

/**
 * This exists to make NetworkInfluence easier (See PriorityQueue usage)
 *
 */
public class Tuple implements Comparable<Tuple> {
	// The Node in the graph
	public String s;
	// The degree of this Node
	public int d;
	
	// Default constructor
	public Tuple() {
		s = "";
		d = -1;
	}
	
	public Tuple(String s0, int d0) {
		s = s0;
		d = d0;
	}

	@Override
	public int compareTo(Tuple arg0) {
		if(d < ((Tuple)arg0).d)
			return -1;
		if(d == ((Tuple)arg0).d)
			return 0;
		return 1;
	}
}
