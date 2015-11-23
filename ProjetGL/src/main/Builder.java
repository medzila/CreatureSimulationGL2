package main;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import creatures.ComposableCreature;
import creatures.IEnvironment;
import creatures.behavior.IStrategyBehavior;
import creatures.EnergySource;
import creatures.color.IColorStrategy;
import creatures.movement.IStrategieMovement;

public class Builder {

	private final static Random rand = new Random();

	static public  Collection<ComposableCreature> createCreatures(IEnvironment env, int count, 
								IColorStrategy colorStrategy, IStrategyBehavior comp, IStrategieMovement depl, double maxSpeed) {
		Collection<ComposableCreature> creatures = new ArrayList<ComposableCreature>();		
		Dimension s = env.getSize();		
		for (int i=0; i<count; i++) {	
			// X coordinate
			double x = (rand.nextDouble() * s.getWidth()) - s.getWidth() / 2;
			// Y coordinate
			double y = (rand.nextDouble() * s.getHeight()) - s.getHeight() / 2;
			// direction
			double direction = (rand.nextDouble() * 2 * Math.PI);
			// speed
			int speed = (int) (rand.nextDouble() * maxSpeed);			
			ComposableCreature creature = null;
			creature = new ComposableCreature(env, new Point2D.Double(x,y), direction, speed, colorStrategy.getColor(), comp, depl);
			creatures.add(creature);
		}		
		return creatures;
	}

	static public Collection<EnergySource> createPoints(IEnvironment env, int number, int size) {
		Random randomInts = new Random();
		Collection<EnergySource> energySpots = new ArrayList<EnergySource>();
		for (int i = 0; i < number; i++) {
			energySpots.add(new EnergySource(new Point2D.Double(randomInts.nextInt(env.getSize().width) - env.getSize().width / 2,
					randomInts.nextInt(env.getSize().height) - env.getSize().height / 2), size));
		}
		return energySpots;
	}
}
