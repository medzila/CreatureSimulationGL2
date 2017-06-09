package creatures.behavior;

import creatures.ComposableCreature;
import plug.IPlugin;

public interface IStrategyBehavior extends IPlugin {
	
	void setNextDirectionAndSpeed(ComposableCreature c);
}
