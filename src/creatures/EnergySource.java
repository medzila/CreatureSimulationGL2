package creatures;

import static java.lang.Math.PI;
import static java.lang.Math.atan;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import visual.IDrawable;

public class EnergySource implements IDrawable {

	public static final int DEFAULT_SIZE = 100;
	public static final Color DEFAULT_COLOR = new Color(255, 0, 0, 128);
	
	public Point2D position;
	public int size;
	
	/**
	 * Create an Energy Source
	 * @param p the position of the center of the EnergySource
	 * @param size the diameter of the EnergySource
	 */
	public EnergySource(Point2D p, int size) {
		this.position = p;
		this.size = size;
	}
	
	// ----------------------------------------------------------------------------
	// Getters and Setters
	// ----------------------------------------------------------------------------

	public void setPosition(Point2D p){
		this.position = p;
	}

	@Override
	public Color getColor() {
		return DEFAULT_COLOR;
	}

	@Override
	public int getSize() {
		return size;
	}

	public Point2D getPosition() {
		return position;
	}

	
	// ----------------------------------------------------------------------------
	// Positioning & Drawing methods
	// ----------------------------------------------------------------------------
	
	@Override
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(position.getX(), position.getY());
		
		// center the surrounding rectangle
		// center the arc
		g2.translate(-size / 2, -size / 2);
		
		
		// useful for debugging
		// g2.drawRect(0, 0, size, size);

		// set the color
		g2.setColor(DEFAULT_COLOR);
		g2.fillOval(0, 0, size, size);

	}
	
	public double directionFormAPoint(Point2D p, double axis) {
		double b = 0d;

		// use a inverse trigonometry to get the angle in an orthogonal triangle
		// formed by the points (x,y) and (x1,y1)
		if (position.getX() != p.getX()) {
			// if we are not in the same horizontal axis
			b = atan((position.getY() - p.getY()) / (position.getX() - p.getX()));
		} else if (position.getY() < p.getY()) {
			// below -pi/2
			b = -PI / 2;
		} else {
			// above +pi/2
			b = PI / 2;
		}

		// make a distinction between the case when the (x1, y1)
		// is right from the (x,y) or left
		if (position.getX() < p.getX()) {
			b += PI;
		}

		// align with the axis of the origin (x1,y1)
		b = b - axis;

		// make sure we always take the smaller angle
		// keeping the range between (-pi, pi)
		if (b >= PI)
			b = b - PI * 2;
		else if (b < -PI)
			b = b + PI * 2;

		return b % (PI * 2);
	}

}
