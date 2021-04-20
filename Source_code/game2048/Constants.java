/**
 * 
 */
package game2048;

/**
 * @author Rachitha Pittala
 *
 */
import java.awt.Color;
import java.util.HashMap;

public class Constants {
	private Constants() {}

	public enum Weights {
		SIDE(1), EMPTY(20), CORNER(50), OTHER(0);
		private int weight;

		Weights(int weight) {
			this.weight = weight;
		}

		public int getWeight() {
			return weight;
		}
	}

	public static final int number_of_directions = 4;
	
	public enum Directions {
		LEFT(0), RIGHT(1), UP(2), DOWN(3);

		private int directionValue;

		Directions(int directionName) {
			this.directionValue = directionName;
		}

		public int getDirectionValue() {
			return directionValue;
		}
	}

	private static final Color colorBlank, color2, color4, color8, color16,
	color32, color64, color128, color256, color512, color1024,
	color2048;
	
	public static final HashMap<String, Color> COLORS;
	static {
		colorBlank = new Color(205, 191, 182);
		color2 = new Color(240, 128, 128);
		color4 = new Color(255, 20, 147);
		color8 = new Color(128, 0, 128);
		color16 = new Color(186, 85, 211);
		color32 = new Color(32, 178, 170);
		color64 = new Color(60, 179, 193);
		color128 = new Color(144, 238, 144);
		color256 = new Color(70,130,180);
		color512 = new Color(0, 191, 255);
		color1024 = new Color(244, 164, 96);
		color2048 = new Color(220, 20, 60);
		
		COLORS = new HashMap<>();
		COLORS.put("", colorBlank);
		COLORS.put("2", color2);
		COLORS.put("4", color4);
		COLORS.put("8", color8);
		COLORS.put("16", color16);
		COLORS.put("32", color32);
		COLORS.put("64", color64);
		COLORS.put("128", color128);
		COLORS.put("256", color256);
		COLORS.put("512", color512);
		COLORS.put("1024", color1024);
		COLORS.put("2048", color2048);
	}
}

