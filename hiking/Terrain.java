package hiking;

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

	public double computeExhaustionPoints(double exhaustion) {
		if (code == 5) // Felswand
			return exhaustion + 3;
		else if (code == 1) // Fluss
			return exhaustion + 4;
		else if (code == 3) { // Wald
			return exhaustion / 2;
		} else
			return exhaustion;
	}
}
