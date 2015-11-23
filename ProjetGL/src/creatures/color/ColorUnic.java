package creatures.color;

import java.awt.Color;

public class ColorUnic implements IColorStrategy {

	private Color color;
	
	public ColorUnic(Color c, int n) {
		this.color = c;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return ColorUnic.class.getName();
	}

}
