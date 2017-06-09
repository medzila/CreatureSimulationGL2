package creatures.behavior;

import creatures.ComposableCreature;


public class StupidBehavior implements IStrategyBehavior {

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		c.move();

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

}
