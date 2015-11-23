package creatures.movement;

import creatures.ICreature;
import plug.IPlugin;

public interface IStrategieMovement extends IPlugin{
	
	void setNextPosition(ICreature c);
}