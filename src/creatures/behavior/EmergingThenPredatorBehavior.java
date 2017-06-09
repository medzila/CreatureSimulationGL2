package creatures.behavior;

import java.lang.reflect.Constructor;
import java.util.Map;

import plug.creatures.BehaviorPluginFactory;
import creatures.ComposableCreature;
import main.Launcher;

/**
 * The creatures follow each other when their life is higher than {@link Launcher#THRESHOLD}.
 * Else, the creatures search for other creatures to eat.
 */
public class EmergingThenPredatorBehavior implements IStrategyBehavior {
		
	private EmergingBehavior emergingBehavior = null;
	private PredatorBehavior predatorBehavior = null;

	private boolean isEmergingBehaviorHere;
	private boolean isPredatorBehaviorHere;
	
	public EmergingThenPredatorBehavior() throws Exception{
		isEmergingBehaviorHere = false ;
		isPredatorBehaviorHere = true;
		Map<String,Constructor<? extends IStrategyBehavior>> factory = BehaviorPluginFactory.getInstance().getMap();
		
		// On regarde chaque comportement dans la factory et il faut trouver tous les comportments qu'on va utilise
		for (String s : factory.keySet()){
			Constructor<? extends IStrategyBehavior> c = factory.get(s);
			if(c == null)
				throw new Exception("Something went wrong with the factory. Report it to devs without reprisal please.");

			//If the plugin is PredatorBehavior && EmergingBehavior
			if(EmergingBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isEmergingBehaviorHere = true ; 
				this.emergingBehavior = (EmergingBehavior) c.newInstance();
			}else if(PredatorBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isPredatorBehaviorHere = true ; 
				this.predatorBehavior = (PredatorBehavior) c.newInstance();
			}
		}
		//If we don't find the Predator or the Emerging behavior, throw a new exception catched in the Launcher
		if(!isEmergingBehaviorHere)
			throw new Exception("Emerging behavior is missing. Add the \"EmergingBehavior\" plugin please.");
		if(!isPredatorBehaviorHere)
			throw new Exception("Predator behavior is missing. Add the \"PredatorBehavior\" plugin please.");
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		if(c.getHealth() >= Launcher.THRESHOLD){
			emergingBehavior.setNextDirectionAndSpeed(c);
		}else {
			predatorBehavior.setNextDirectionAndSpeed(c);
		}

	}

}
