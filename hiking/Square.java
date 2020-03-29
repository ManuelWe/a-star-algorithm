package hiking;

import java.util.StringJoiner;

import a_star.GraphNode;

public class Square implements GraphNode {
	private final String id;
	private final int positionX;
	private final int positionY;
	private final Terrain terrain;

	public Square(String id, int positionX, int positionY, Terrain terrain) {
		this.id = id;
		this.positionX = positionX;
		this.positionY = positionY;
		this.terrain = terrain;
	}

	@Override
	public String getId() {
		return id;
	}

	public int getX() {
		return positionX;
	}

	public int getY() {
		return positionY;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Square.class.getSimpleName() + "[", "]").add("id='" + id + "'")
				.add("posX=" + positionX).add("posY=" + positionY).toString();
	}
}