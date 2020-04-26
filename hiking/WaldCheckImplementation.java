package hiking;

import java.util.ArrayList;

import a_star.WaldCheck;

// checks if cost for path over "wald" have to be reduced. Cost have to be reduced if it is likely that a break has to be taken  
public class WaldCheckImplementation implements WaldCheck<Square> {
	ArrayList<Terrain> terrains;

	public WaldCheckImplementation(ArrayList<Terrain> terrains) {
		this.terrains = terrains;
	}

	@Override
	public double checkCostReduction(Square from, Square connection, Square to, Square prev,
			double currentExhaustionPoints, double targetScorerCost) {

		if (prev != null && from.getTerrain().getCode() == 3) { // only relevant if current = "wald"
			if (connection.getId() == prev.getId()) { // no reduction if walking back to previous node
				return targetScorerCost;
			} else if ((Math.abs(prev.getX() - to.getX()) + Math.abs(prev.getY() - to.getY()) - 1) * 4 >= 10
					- currentExhaustionPoints && currentExhaustionPoints > 0) { // reduction of path over "wald" if
																				// exhaustion points can get greater 10
				return targetScorerCost - 5;
			}
		}
		return targetScorerCost;
	}
}
