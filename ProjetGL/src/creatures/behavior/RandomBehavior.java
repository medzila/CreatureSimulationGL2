package creatures.behavior;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import creatures.ComposableCreature;
import creatures.ICreature;

public class RandomBehavior implements IStrategyBehavior {
	
	private static final double MIN_SPEED = 3;
	private static final double MAX_SPEED = 10;

	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 30;
	
	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		ComposableCreature c1 = (ComposableCreature)c;
		applyNoise(c1);
		c1.move();
	}

	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	public void applyNoise(ICreature c) {
		ComposableCreature c1 = (ComposableCreature)c;
		c1.setCurrCycle(c1.getCurrCycle()+1);
		c1.currCycle %= NUMBER_OF_CYCLES_PER_CHANGE;

		// every NUMBER_OF_CYCLES_PER_CHANGE we do the change
		if (c1.currCycle == 0) {
			c1.setSpeed(c1.getSpeed()+((random() * 2) - 1));

			// maintain the speed within some boundaries
			if (c1.getSpeed() < MIN_SPEED) {
				c1.setSpeed(MIN_SPEED);
			} else if (c1.getSpeed() > MAX_SPEED) {
				c1.setSpeed(MAX_SPEED);
			}

			c1.setDirection(c1.getDirection()
					+ ((random() * PI / 2) - (PI / 4)));
		}
	}

	@Override
	public String getName() {
		return RandomBehavior.class.getName();
	}

}
