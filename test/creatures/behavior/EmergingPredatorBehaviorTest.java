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
import java.util.logging.Logger;
import java.util.logging.LogManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import creatures.ComposableCreature;
import creatures.EnergySource;
import creatures.ICreature;
import creatures.movement.TorusMovement;
import creatures.visual.CreatureSimulator;
import main.Launcher;
import plug.creatures.BehaviorPluginFactory;

/**
 *Test of composite behavior : Emerging and Predator
 */
public class EmergingPredatorBehaviorTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	EmergingThenPredatorBehavior composite;
	TorusMovement t;

	/**
	 * Load behavior plugin for the test.
	 */
	@BeforeClass
	public static void setUpClass() {
		LogManager.getLogManager().reset();
		Logger globalLogger = Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(java.util.logging.Level.OFF);
		BehaviorPluginFactory.init();
	}

	/**
	 * Delete behavior plugin after the end of the test.
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		BehaviorPluginFactory.delete();
	}

	@Before
	public void setup() throws Exception {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		composite = new EmergingThenPredatorBehavior();
		t = new TorusMovement();
	}

	/**
	 * Emergin Behavior Test.
	 * @throws Exception
	 */
	@Test
	public void testEmerginBehaviorComposite() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the emerging behavior.
		assertTrue(Launcher.THRESHOLD<=creature.getHealth());

		//the creature see the other creature and follow it.
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
		
		composite.setNextDirectionAndSpeed(creature);
		
		assertEquals(toRadians((270+10)/2), creature.getDirection(), .01);
		assertEquals((5.0+5.0)/2, creature.getSpeed(), .01);			
	}
	
	/**
	 * Test how a creature loose health when she is alone.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementAloneComposite() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the emerging behavior.
		assertTrue(Launcher.THRESHOLD<=creature.getHealth());

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
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the emerging behavior.
		assertTrue(Launcher.THRESHOLD<=creature.getHealth());
		
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
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH,.01);

	}
	
	/**
	 * Test how the creature loose health when there is 2 creatures.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementWith2Creatures() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,composite,t);
		//Verify which behavior the creature will have. Here the emerging behavior.
		assertTrue(Launcher.THRESHOLD<=creature.getHealth());
		
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
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH/2,.01);

	}
	
	/**
	 * Test how the creature loose health when there is 3 creatures.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testLooseHealthComportementWithSeveralCreatures() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(0),5,
				Color.BLACK,composite,t);
		//Verify which behavior the creature will have. Here the emerging behavior.
		assertTrue(Launcher.THRESHOLD<=creature.getHealth());
		
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
		
		assertEquals(creature.getHealth(),100-creature.DEFAULT_LOSS_HEALTH/3,.01);

	}
	
	/**
	 * Test if the predator take the right direction to the prey.
	 */
	@Test
	public void testDirectionPrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Predator behavior.

		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());

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

		composite.setNextDirectionAndSpeed(creature);

		assertEquals(toRadians(270), creature.getDirection(), .01);
	}

	/**
	 * Test if the predator is faster than the prey when he saw it.
	 */	
	@Test
	public void testSpeedPredator() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Predator behavior.

		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());

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

		composite.setNextDirectionAndSpeed(creature);

		assertEquals(other.getSpeed()+1, creature.getSpeed(), .01);

	}


	/**
	 * Test the behavior of the predator when he is closer to the prey and attacks. 
	 */
	@Test
	public void testPredatorAttackPrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,composite,t);

		ComposableCreature other = new ComposableCreature(environment,new Point2D.Double(1, 0),toRadians(100),10,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Predator behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());
		
		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);

		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());

		composite.setNextDirectionAndSpeed(creature);

		assertTrue(other.getHealth() < creature.getHealth());

		assertEquals(other.getHealth(), creature.getHealth()-20, .01);

	}

	/**
	 * Test if the creature do nothing when he don't have a prey.
	 */
	@Test
	public void testCreatureDontSeeThePrey() {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Predator behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		Point2D positionBefore = creature.getPosition();

		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());

		composite.setNextDirectionAndSpeed(creature);

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
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Predator behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());

		ComposableCreature other = new ComposableCreature(environment,new Point2D.Double(1,0),toRadians(180),0,
				Color.BLACK,composite,t);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);

		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getEnergySources()).thenReturn(new ArrayList<EnergySource>());
		double distanceBefore = creature.distanceFromAPoint(other.getPosition());

		composite.setNextDirectionAndSpeed(creature);

		double distanceAfter = creature.distanceFromAPoint(other.getPosition());

		assertTrue(distanceAfter < distanceBefore);
		assertEquals(creature.getDirection(),other.getDirection(),0.01);
		assertEquals(other.getHealth(),100,0.01);
		assertEquals(creature.getHealth(),90,0.01);
		assertEquals(creature.getSpeed(),other.getSpeed()+1,0.01);
	}

}
