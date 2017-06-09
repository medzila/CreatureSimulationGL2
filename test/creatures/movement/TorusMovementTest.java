package creatures.movement;

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
import creatures.movement.TorusMovement;
import creatures.visual.CreatureSimulator;


public class TorusMovementTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 50;
	StupidBehavior s;
	TorusMovement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		s = new StupidBehavior();
		t = new TorusMovement();
	}
	
	@Test
	public void testTorusDeplacementRightWall() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(49,0),0,10,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
				
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition(),new Point2D.Double(-50,0));
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
	@Test
	public void testTorusDeplacementLeftWall() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(-49,0),Math.toRadians(180),10,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
				
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getX(),50,0);
		assertEquals(creature.getPosition().getY(),0,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}

	@Test
	public void testTorusDeplacementUpWall() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,24),-Math.PI/2,5,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getY(),-25,0);
		assertEquals(creature.getPosition().getX(),0,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
	@Test
	public void testTorusDeplacementDownWall() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,-24),Math.PI/2,5,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getY(),25,0);
		assertEquals(creature.getPosition().getX(),0,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
	@Test
	public void testTorusDeplacementAngleRightUp() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(49,24),Math.toRadians(-45),2,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getY(),-24,1);
		assertEquals(creature.getPosition().getX(),-50,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
	@Test
	public void testTorusDeplacementAngleDownLeft() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(-49,-24),Math.toRadians(-225),2,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getY(),24,1);
		assertEquals(creature.getPosition().getX(),50,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
	@Test
	public void testTorusDeplacementAngleUpLeft() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(-49,24),Math.toRadians(-135),2,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getY(),-24,1);
		assertEquals(creature.getPosition().getX(),50,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
	@Test
	public void testTorusDeplacementAngleDownRight() throws Exception {
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(49,-24),Math.toRadians(45),2,
				Color.BLACK,s,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		
		t.setNextPosition(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertEquals(creature.getPosition().getY(),24,1);
		assertEquals(creature.getPosition().getX(),-50,0.1);
		assertEquals(creature.getDirection(),directionBefore,0.001);
		assertEquals(creature.getSpeed(),speedBefore,0.001);	
				
	}
	
}
