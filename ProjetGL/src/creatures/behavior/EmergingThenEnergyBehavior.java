package creatures.behavior;

import java.lang.reflect.Constructor;
import java.util.Map;

import plug.creatures.BehaviorPluginFactory;
import creatures.ComposableCreature;
import main.Launcher;

/**
 * The creatures follow each other when their life is higher than {@link Launcher#THRESHOLD}.
 * Else, the creatures search energy sources.
 */
public class EmergingThenEnergyBehavior implements IStrategyBehavior {
	
	private EnergyBehavior energyBehavior = null;
	private EmergingBehavior emergingBehavior = null;
	boolean isEnergyBehaviorHere = false;
	boolean isEmergingBehaviorHere = false ;
	
	public EmergingThenEnergyBehavior() throws Exception {
		Map<String,Constructor<? extends IStrategyBehavior>> factory = BehaviorPluginFactory.getInstance().getMap();
		
		// We check every behavior in the factory. We have to find every behavior needed (emerging & energy)
		for (String s : factory.keySet()){
			Constructor<? extends IStrategyBehavior> c = factory.get(s);
			if(c == null)
				throw new Exception("Something went wrong with the factory. Report it to devs without reprisal please.");

			//If the plugin is EnergyBehavior && EmergingBehavior
			if (EnergyBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isEnergyBehaviorHere = true;
				this.energyBehavior = (EnergyBehavior) c.newInstance();
			}else if(EmergingBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isEmergingBehaviorHere = true ; 
				this.emergingBehavior = (EmergingBehavior) c.newInstance();
			}
		}
		//If we don't find the Energy or the Emerging behavior, throw a new exception catched in the Launcher
		if(!isEnergyBehaviorHere)
			throw new Exception("Energy behavior is missing. Add the \"EnergyBehavior\" plugin please.");
		if(!isEmergingBehaviorHere)
			throw new Exception("Emerging behavior is missing. Add the \"EmergingBehavior\" plugin please.");
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		if(c.getHealth() >= Launcher.THRESHOLD){
			emergingBehavior.setNextDirectionAndSpeed(c);
		} else {
			energyBehavior.setNextDirectionAndSpeed(c);
		}

	}

}
