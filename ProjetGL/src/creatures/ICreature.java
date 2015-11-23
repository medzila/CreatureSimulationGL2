package creatures;

import java.awt.geom.Point2D;
import plug.IPlugin;
import simulator.IActionable;
import visual.IDrawable;

public interface ICreature extends IDrawable, IActionable, IPlugin {

	public IEnvironment getEnvironment();

	public boolean isDead();
	
	public double getHealth();
	
	public double getLossHealth();
	
	public double getGainedHealth();
	
	public double getSpeed();

	public double getDirection();
	
	public void setDirection(double d);
	
	public void setSpeed(double d);

	public Point2D getPosition();
	
	public void move();
	
	public void act();
	
	public void setPosition(double x, double y);

	public abstract double distanceFromAPoint(Point2D p);

	public abstract double directionFormAPoint(Point2D p, double axis);

	

}