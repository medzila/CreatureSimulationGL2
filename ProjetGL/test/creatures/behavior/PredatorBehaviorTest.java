package creatures.behavior;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import creatures.ComposableCreature;
import creatures.EnergySource;
import creatures.ICreature;
import creatures.behavior.PredatorBehavior;
import creatures.movement.TorusMovement;
import creatures.visual.CreatureSimulator;


public class PredatorBehaviorTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	PredatorBehavior p;
	TorusMovement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		p = new PredatorBehavior();
		t = new TorusMovement();
	}

	/**
	 * Test if the predator take the right direction to the prey.
	 */
	@Test
	public void testDirectionPrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,p,t);
		
		ComposableCreature other = mock(ComposableCreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));
		when(other.distanceFromAPoint(eq(creature.getPosition()))).thenReturn(1.0);
		when(other.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		p.setNextDirectionAndSpeed(creature);
		
		assertEquals(toRadians(270), creature.getDirection(), .01);
	}
	
	/**
	 * Test if the predator is faster than the prey when he saw it.
	 */
	@Test
	public void testSpeedPredator() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,p,t);
		
		ComposableCreature other = mock(ComposableCreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(4.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));
		when(other.distanceFromAPoint(eq(creature.getPosition()))).thenReturn(1.0);
		when(other.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		p.setNextDirectionAndSpeed(creature);
		
		assertEquals(other.getSpeed()+1, creature.getSpeed(), .01);

	}
	
	/**
	 * Test the behavior of the predator when he is closer to the prey and attacks. 
	 */
	@Test
	public void testPredatorAttackPrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,p,t);
		
		ComposableCreature other = new ComposableCreature(environment,new Point2D.Double(1, 0),toRadians(100),10,
				Color.BLACK,p,t);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		p.setNextDirectionAndSpeed(creature);
		
		assertTrue(other.getHealth() < creature.getHealth());

		assertEquals(other.getHealth(), creature.getHealth()-20, .01);

	}
	
	/**
	 * Test if the creature do nothing when he don't have a prey.
	 */
	@Test
	public void testCreatureDontSeeThePrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,p,t);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		Point2D positionBefore = creature.getPosition();
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		p.setNextDirectionAndSpeed(creature);
				
		assertFalse(creature.isHunting());
		assertNotEquals(positionBefore,creature.getPosition());
	}
	
	/**
	 * Test if the predator get closer to the prey when he is to far.
	 * He doesn't attack yet.
	 */
	@Test
	public void testPredatorGetCloserToThePrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(22,0),toRadians(180),5,
				Color.BLACK,p,t);
		
		ComposableCreature other = new ComposableCreature(environment,new Point2D.Double(1,0),toRadians(180),0,
				Color.BLACK,p,t);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		double distanceBefore = creature.distanceFromAPoint(other.getPosition());
						
		p.setNextDirectionAndSpeed(creature);

		double distanceAfter = creature.distanceFromAPoint(other.getPosition());
		
		assertTrue(distanceAfter < distanceBefore);
		assertEquals(creature.getDirection(),other.getDirection(),0.01);
		assertEquals(other.getHealth(),creature.getHealth(),0.01);
		assertEquals(creature.getSpeed(),other.getSpeed()+1,0.01);
	}
}
