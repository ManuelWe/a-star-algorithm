package hiking;

// class to save terrain property belonging to a square
public class Terrain {
	private final int code;
	private final String name;
	private final int cost;

	public Terrain(int code, String name, int cost) {
		this.code = code;
		this.name = name;
		this.cost = cost;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}
}
