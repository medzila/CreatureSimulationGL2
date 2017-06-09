package creatures.behavior;

import static commons.Utils.filter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.random;

import commons.Utils.Predicate;
import creatures.ComposableCreature;

import creatures.ICreature;

/**
 * The creatures are looking for other creatures and eat them.
 */
public class PredatorBehavior implements IStrategyBehavior {
	private static final double MAX_SPEED = 6;
	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 50;
	
	/** Minimal distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;

	/** Minimal speed in pixels per loop. */
	private final static double MIN_SPEED = 3d;
	
	private static final int DEFAULT_EATEN = (int) ((ComposableCreature.DEFAULT_HEALTH * 20)/100);
	static class CreaturesAroundCreature implements Predicate<ICreature> {
		private final ComposableCreature observer;

		public CreaturesAroundCreature(ComposableCreature observer) {
			this.observer = observer;
		}
		@Override
		public boolean apply(ICreature input) {
			if (input == observer) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.getPosition()) <= observer
					.getLengthOfView();

		}
	}

	public Iterable<ICreature> creaturesAround(
			ComposableCreature creature) {
		return filter(creature.getEnvironment().getCreatures(), new CreaturesAroundCreature((ComposableCreature)creature));
	}

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		ComposableCreature c1 = (ComposableCreature)c;
		ComposableCreature prey = null;
		double minDist = Double.MAX_VALUE;
		
		//Get the closest creature around
		Iterable<ICreature> creatures = creaturesAround(c1);
		for (ICreature c2 : creatures) {
			if(c1.distanceFromAPoint(c2.getPosition()) <= minDist){
				prey = (ComposableCreature) c2;
				minDist = c1.distanceFromAPoint(c2.getPosition());
			}
		}
		
		//if has a target
		if(prey != null ){
			//go faster than the prey
			c1.setSpeed(prey.getSpeed()+1);
			//go in the same direction
			c1.setDirection(prey.getDirection());
			//if the prey is close the predator gain health and the prey loose health
			if(c1.distanceFromAPoint(prey.getPosition())<=20){
				c1.setHunting(true);
				c1.setHealth(c1.getHealth() + DEFAULT_EATEN);
				prey.setHealth(prey.getHealth() - DEFAULT_EATEN);
			}
		}
		//if hasn't targert
		if(prey == null){
			//and were hunting
			if(c1.isHunting())
				//is no more hunting
				c1.setHunting(false);
			applyNoise(c1);
		}

		if (minDist > MIN_DIST) {
			c1.move();
		}
	}
	
	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	public void applyNoise(ComposableCreature c1) {
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
		return PredatorBehavior.class.getName();
		}
}