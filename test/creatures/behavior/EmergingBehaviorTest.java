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
import creatures.behavior.EmergingBehavior;
import creatures.movement.TorusMovement;
import creatures.visual.CreatureSimulator;

public class EmergingBehaviorTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	EmergingBehavior s;
	TorusMovement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		s = new EmergingBehavior();
		t = new TorusMovement();
	}
	
	/**
	 * Emergin Behavior Test.
	 * @throws Exception
	 */
	@Test
	public void testEmerginBehavior() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,s,t);
		
		//the creature see the other creature and follow it.
		ICreature other = mock(ICreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));
		when(other.distanceFromAPoint(eq(creature.getPosition()))).thenReturn(1.0);
		when(other.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		s.setNextDirectionAndSpeed(creature);
		
		assertEquals(toRadians((270+10)/2), creature.getDirection(), .01);
		assertEquals((5.0+5.0)/2, creature.getSpeed(), .01);
				
	}
	
	/**
	 * Test how a creature loose health when she is alone.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementAlone() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,s,t);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		creature.act();
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH,.01);

	}
	
	/**
	 * Test how a creature loose health where there is another creature.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementWithOneCreature() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,s,t);
		
		ComposableCreature other = mock(ComposableCreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getHealth()).thenReturn(100.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		creature.act();
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH/1,.01); // health - (Default_Loss_health/numberOfCreatureAround)

	}
	
	/**
	 * Test how the creature loose health when there is 2 creatures.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementWith2Creatures() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,s,t);
		
		
		ComposableCreature other = mock(ComposableCreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getHealth()).thenReturn(100.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		creaturesAround.add(other);

		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		creature.act();
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH/2,.01);  // health - (Default_Loss_health/numberOfCreatureAround)

	}
	
	/**
	 * Test how the creature loose health when there is 3 creatures.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementWithSeveralCreatures() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,s,t);
		
		ComposableCreature other = mock(ComposableCreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getHealth()).thenReturn(100.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		creaturesAround.add(other);
		creaturesAround.add(other);

		
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		
		creature.act();
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH/3,.01);  // health - (Default_Loss_health/numberOfCreatureAround)

	}

}
