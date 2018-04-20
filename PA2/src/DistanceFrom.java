import java.util.function.Function;

public class DistanceFrom implements Function<Adjacency, Integer> {
	public Graph g;
	public String source;

	public DistanceFrom(Graph g, String source) {
		this.g = g;
		this.source = source;
	}

	@Override
	public Integer apply(Adjacency destination) {
		// TODO Auto-generated method stub
		return null;
	}
}
