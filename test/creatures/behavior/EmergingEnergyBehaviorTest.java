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
 *Test of composite behavior : Emerging and Energy
 */
public class EmergingEnergyBehaviorTest {
	
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	EmergingThenEnergyBehavior composite;
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
		composite = new EmergingThenEnergyBehavior();
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
	 * Checked if the creature takes the right direction to the point
	 */
	@Test
	public void getDirectionToThePoint(){
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/4,3,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Energy behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());
				
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
						
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
				
		double dx = pte.getPosition().getX() - creature.getPosition().getX();
		double dy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(dy, dx);
		
		composite.setNextDirectionAndSpeed(creature);
		
		assertEquals(creature.getDirection(),-angle,0.01);
	}
	
	/**
	 * Verify if the creature get closer to the point.
	 */
	@Test
	public void getCloserToTheEnergyPoint(){
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/4,3,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Energy behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());
		
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
		
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());

		composite.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
				
		assertTrue(distanceAfter < distanceBefore);
	}
	
	
	/**
	 * Checks whether the creature moved closer to the energy point.
	 * @throws Exception
	 */
	@Test
	public void testCreatureGoToAnEnergyPoint() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(2,5),Math.PI/2,6,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Energy behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());
		
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
				
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());
		double vx = pte.getPosition().getX() - creature.getPosition().getX();
		double vy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(vy, vx);
		
		composite.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
		
		assertEquals(creature.getDirection(),-angle,0.1);
		assertTrue(distanceAfter < distanceBefore);
		
	}
	
	/**
	 * Checks whether the creature don't moved closer to the energy point.
	 * @throws Exception
	 */
	@Test
	public void testCreatureDontGoToAnEnergyPoint() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/2,5,
				Color.BLACK,composite,t);
		
		//Verify which behavior the creature will have. Here the Energy behavior.
		creature.setHealth(90);
		Launcher.THRESHOLD = 100;
		assertTrue(Launcher.THRESHOLD > creature.getHealth());
		
		//creature don't see the point
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,110));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
				
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());
		double vx = pte.getPosition().getX() - creature.getPosition().getX();
		double vy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(vy, vx);
		
		composite.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
		
		assertNotEquals(creature.getDirection(),-angle,0.1);
		assertTrue(distanceAfter >= distanceBefore);
	}


}
