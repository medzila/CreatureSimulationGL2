package creatures.movement;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import creatures.ComposableCreature;
import creatures.behavior.StupidBehavior;
import creatures.movement.BouncingMovement;
import creatures.visual.CreatureSimulator;

public class BouncingMovementTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	StupidBehavior s;
	BouncingMovement b;
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		s = new StupidBehavior();
		b = new BouncingMovement();
	}

	
	@Test
	public void testDirectLeftUp() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(-w/2+1, 0), toRadians(150), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(30), creature.getDirection(), 0.01);
		assertEquals(-w/2+6, creature.getPosition().getX(), 2);
		assertEquals(-6, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testDirectLeftDown() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(-w/2+1, 0), toRadians(210), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(330), creature.getDirection(), 0.01);
		assertEquals(-w/2+6, creature.getPosition().getX(), 2);
		assertEquals(6, creature.getPosition().getY(), 2);
    }	
	
	
	@Test 
	public void testDirectRightUp() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(w/2-1, 0), toRadians(30), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(150), creature.getDirection(), 0.01);
		assertEquals(w/2-6, creature.getPosition().getX(), 2);
		assertEquals(-6, creature.getPosition().getY(), 2);
    }	
	
	@Test 
	public void testDirectRightDown() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(w/2-1, 0), toRadians(330), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(210), creature.getDirection(), 0.01);
		assertEquals(w/2-6, creature.getPosition().getX(), 2);
		assertEquals(6, creature.getPosition().getY(), 2);
    }	
	
	
	@Test
	public void testDirectUpRight() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(0, -h/2+1), toRadians(30), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(330), creature.getDirection(), 0.01);
		assertEquals(8, creature.getPosition().getX(), 2);
		assertEquals(-h/2+4, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testDirectUpLeft() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(0, -h/2+1), toRadians(150), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(210), creature.getDirection(), 0.01);
		assertEquals(-8, creature.getPosition().getX(), 2);
		assertEquals(-h/2+4, creature.getPosition().getY(), 2);
    }
	
	@Test
	public void testDirectDownRight() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(0, h/2-1), toRadians(330), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(30), creature.getDirection(), 0.01);
		assertEquals(8, creature.getPosition().getX(), 2);
		assertEquals(h/2-4, creature.getPosition().getY(), 2);
    }	
	
	@Test
	public void testDirectDownLeft() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(0, h/2-1), toRadians(210), 10,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(150), creature.getDirection(), 0.01);
		assertEquals(-8, creature.getPosition().getX(), 2);
		assertEquals(h/2-4, creature.getPosition().getY(), 2);
    }
	
	
	@Test
	public void testUpperRightCorner45() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(w/2, -h/2), toRadians(45), 1,
				Color.BLACK,s,b);
		b.setNextPosition(creature);

		assertEquals(toRadians(225), creature.getDirection(), 0.01);
		assertEquals(w/2, creature.getPosition().getX(), 1);
		assertEquals(-h/2, creature.getPosition().getY(), 1);
    }	
	
	@Test
	public void testUpperRightCorner30() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(w/2, -h/2), toRadians(30), 1,
				Color.BLACK,s,b);
		b.setNextPosition(creature);
		
		assertEquals(toRadians(210), creature.getDirection(), 0.01);
		assertEquals(w/2, creature.getPosition().getX(), 1);
		assertEquals(-h/2, creature.getPosition().getY(), 1);
    }	
	
	@Test
	public void testDirectBottom() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(0, h/2), toRadians(270), 1,
				Color.BLACK,s,b);
		b.setNextPosition(creature);

		assertEquals(toRadians(90), creature.getDirection(), 0.01);
		assertEquals(0, creature.getPosition().getX(), 1);
		assertEquals(h/2, creature.getPosition().getY(), 1);
		
	}

	// Special case: in a corner but not really facing both sides
	@Test
	public void testSpecialCorner() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment, new Point2D.Double(w/2, h/2), toRadians(210), 1,
				Color.BLACK,s,b);
		b.setNextPosition(creature);

		assertEquals(toRadians(150), creature.getDirection(), 0.01);
		assertEquals(h/2, creature.getPosition().getY(), 1);		
	}

}
