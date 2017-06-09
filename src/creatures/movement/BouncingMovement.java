package creatures.movement;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Dimension;

import creatures.ICreature;

/**
 * The creatures bounces when they hit a "wall"
 */
public class BouncingMovement implements IStrategieMovement {

	public BouncingMovement() {
	}
	
	private void setDirectionBounceX(ICreature c) {
		if (c.getDirection() >= PI)
			c.setDirection(3*PI - c.getDirection());
		else
			c.setDirection(PI - c.getDirection());
	}

	private void setDirectionBounceY(ICreature c) {
		c.setDirection(PI * 2 - c.getDirection());
	}
	
	@Override
	public void setNextPosition(ICreature c) {	
		
		Dimension s = c.getEnvironment().getSize();
		
		double newX = c.getPosition().getX() + c.getSpeed() * cos(c.getDirection());
		// the reason there is a minus instead of a plus is that in our plane
		// Y coordinates rises downwards
		double newY = c.getPosition().getY() - c.getSpeed() * sin(c.getDirection());

		double hw = s.getWidth() / 2;
		double hh = s.getHeight() / 2;

		// newX and newY were just put on the border of the envt. It's not a bug
		// as long as the tests passed. Now, the mirroring position is computed.
		
		if (newX < -hw) {
			newX = - 2*hw - newX;
			// ERROR #2 direction is badly managed 
			setDirectionBounceX(c);	
		} else if (newX > hw) {
			newX = 2*hw - newX;
			// ERROR #2 direction is badly managed 
			setDirectionBounceX(c);
		} // else // ERROR #1 (NO ELSE, we need to check X and Y independently)
		
		if (newY < -hh) {
			newY = - 2*hh - newY;
			// ERROR #2 direction is badly managed 
			setDirectionBounceY(c);
		} else if (newY > hh) {
			// ERROR #3 (cut and paste led to "hw" instead of "hh")
			newY = 2*hh - newY;
			// ERROR #2 direction is badly managed 
			setDirectionBounceY(c);
		}
		
		c.setPosition(newX, newY);

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
