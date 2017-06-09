/**
 * 
 */
package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import creatures.behavior.RandomBehavior;
import creatures.movement.BouncingMovement;
import creatures.visual.CreatureSimulator;

public class HealthSystemTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	double defautHealth;
	double defautLossHealth;
	double defautGainedHealth;
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		
		defautHealth = ComposableCreature.DEFAULT_HEALTH;
		defautLossHealth = ComposableCreature.DEFAULT_LOSS_HEALTH;
		defautGainedHealth = ComposableCreature.DEFAULT_GAINED_HEALTH;
	}

	/** Test if health equals {@link AbstractCreature#DEFAULT_HEALTH} when a creature is created. */
	@Test
	public void initHealth() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		
		assertEquals(bouncing.getHealth(), defautHealth, 0.0);
	}
	
	/** Test if health equals {@link AbstractCreature#DEFAULT_HEALTH} - {@link AbstractCreature#DEFAULT_LOSS_HEALTH} when a calls {@link AbstractCreature#looseHealth()}. */
	@Test
	public void looseHealthTest() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		
		bouncing.looseHealth();
		assertTrue(bouncing.getHealth() < defautHealth);
		assertEquals(bouncing.getHealth(), defautHealth - defautLossHealth, 0.0);
	}
	
	/** Test if health equals 0 and if creature is dead if creature calls {@link AbstractCreature#looseHealth()} when health <= 0. */
	@Test
	public void deathTest() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		
		assertEquals(bouncing.getHealth(), defautHealth, 0.0);
		assertFalse(bouncing.isDead());
		
		bouncing.setLossHealth(defautHealth + 1);
		bouncing.looseHealth();
		
		assertEquals(bouncing.getHealth(), 0.0, 0.0);
		assertTrue(bouncing.isDead());
	}
	
	/** Test if health equals current health + {@link AbstractCreature#DEFAULT_GAINED_HEALTH} when a creature calls {@link AbstractCreature#gainHealth()}. */
	@Test
	public void gainHealthTest() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		
		assertEquals(bouncing.getHealth(), defautHealth, 0.0);
		
		bouncing.setHealth(1);
		
		assertEquals(bouncing.getHealth(), 1, 0.0);
		
		bouncing.gainHealth();
		
		assertEquals(bouncing.getHealth(), 1 + defautGainedHealth, 0.0);
		
	}
	
	/** Test if health equals {@link AbstractCreature#DEFAULT_HEALTH} when a creature calls {@link AbstractCreature#gainHealth()} when health >= {@link AbstractCreature#DEFAULT_HEALTH}. */
	@Test
	public void gainHealthOverMaxHealthTest() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		
		assertEquals(bouncing.getHealth(), defautHealth, 0.0);
		
		bouncing.gainHealth();
		
		assertEquals(bouncing.getHealth(), defautHealth, 0.0);
		
	}

	/** Test if a creature loose health when there is no energy point near. */
	@Test
	public void loseHealthDependingOnIfAnEnergyPointIsNearTest() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		ArrayList<EnergySource> pointsArround = new ArrayList<EnergySource>();
		
		assertEquals(bouncing.getHealth(), defautHealth, 0.0);
		when(environment.getEnergySources()).thenReturn(pointsArround);
		
		bouncing.gainOrLoseHealth();
		
		assertEquals(bouncing.getHealth(), defautHealth - defautLossHealth, 0.0);
		
	}
	
	/** Test if creature gain health when there is an energy point near. */
	@Test
	public void gainHealthDependingOnIfAnEnergyPointIsNearTest() {
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		ArrayList<EnergySource> pointsArround = new ArrayList<EnergySource>();
		pointsArround.add(new EnergySource(new Point2D.Double(0, 0), 100));
		
		bouncing.setHealth(1);
		assertEquals(bouncing.getHealth(), 1, 0.0);
		
		when(environment.getEnergySources()).thenReturn(pointsArround);
		
		bouncing.gainOrLoseHealth();
		
		assertEquals(bouncing.getHealth(), 1 + defautGainedHealth, 0.0);
		
	}
	
	/** Test if {@link AbstractCreature#isOnAnEnergySource()} returns false is there is no energy point around. */	
	@Test
	public void IfIsNotNearAnEnergyPoint(){
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		ArrayList<EnergySource> pointsArround = new ArrayList<EnergySource>();
		when(environment.getEnergySources()).thenReturn(pointsArround);
		
		assertFalse(bouncing.isOnAnEnergySource());
	}
	
	/** Test if {@link AbstractCreature#isOnAnEnergySource()} returns true if the creature and an energy point have the same position. */	
	@Test
	public void IfIsNearCenterOfAnEnergyPoint(){
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		ArrayList<EnergySource> pointsArround = new ArrayList<EnergySource>();
		pointsArround.add(new EnergySource(new Point2D.Double(0,0), 100));
		when(environment.getEnergySources()).thenReturn(pointsArround);
		
		assertTrue(bouncing.isOnAnEnergySource());
	}
	
	/** Test if {@link AbstractCreature#isOnAnEnergySource()} returns true if the creature is at a bound of an energy point. */	
	@Test
	public void IfIsNearBoundsOfEnergyPoint(){
		ComposableCreature bouncing = new ComposableCreature(environment, new Point2D.Double(0, 0), toRadians(0), 5, Color.RED, new RandomBehavior(), new BouncingMovement());
		ArrayList<EnergySource> pointsArround = new ArrayList<EnergySource>();
		pointsArround.add(new EnergySource(new Point2D.Double(0, EnergySource.DEFAULT_SIZE/2), 100));
		
		when(environment.getEnergySources()).thenReturn(pointsArround);
		
		assertTrue(bouncing.isOnAnEnergySource());
	}
}
