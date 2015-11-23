package creatures.behavior;

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
import creatures.behavior.EnergyBehavior;
import creatures.movement.TorusMovement;
import creatures.visual.CreatureSimulator;

public class EnergyBehaviorTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 300;
	final double h = 350;
	EnergyBehavior e;
	TorusMovement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		e = new EnergyBehavior();
		t = new TorusMovement();
	}
	
	/**
	 * Check if the creature see the point.
	 * @throws Exception
	 */
	@Test
	public void testPointAround() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/4,3,
				Color.BLACK,e,t);
				
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
						
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		
		e.setNextDirectionAndSpeed(creature);
				
		assertEquals(1, ptsAround.size());		
	}
	
	/**
	 * Checked if the creature takes the right direction to the point
	 */
	@Test
	public void getDirectionToThePoint(){
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/4,3,
				Color.BLACK,e,t);
				
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
		
		e.setNextDirectionAndSpeed(creature);
		
		assertEquals(creature.getDirection(),-angle,0.01);
	}
	
	/**
	 * Verify if the creature get closer to the point.
	 */
	@Test
	public void getCloserToTheEnergyPoint(){
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/4,3,
				Color.BLACK,e,t);
				
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
		
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());

		e.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
				
		assertTrue(distanceAfter < distanceBefore); // show that he cames closer to the point.
	}
	
	/**
	 * Verify if the creature see the Energy point when it's on his field of view
	 * and don't see the point when the creature is to far. 
	 * @throws Exception
	 */
	@Test
	public void testPointsAround() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		int sizePoint = 20;
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(sizePoint);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
		
		EnergySource pte1 = mock(EnergySource.class);
		when(pte1.getPosition()).thenReturn(new Point2D.Double(0,creature.getLengthOfView()+sizePoint/2+1)); // Point Hors du champ de vision
		when(pte1.getSize()).thenReturn(sizePoint);
		when(pte1.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
		
		EnergySource pte2 = mock(EnergySource.class);
		when(pte2.getPosition()).thenReturn(new Point2D.Double(0,creature.getLengthOfView()+sizePoint/2+1)); // Point hors du champ de vision
		when(pte2.getSize()).thenReturn(sizePoint);
		when(pte2.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		ptl.add(pte1);
		ptl.add(pte2);

		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		e.setNextDirectionAndSpeed(creature);
		
		assertEquals(1, ptsAround.size());
		
	}
	
	/**
	 * Checks whether the creature moved closer to the energy point.
	 * @throws Exception
	 */
	@Test
	public void testCreatureGoToAnEnergyPoint() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(2,5),Math.PI/2,6,
				Color.BLACK,e,t);
		
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,-11));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());
		double vx = pte.getPosition().getX() - creature.getPosition().getX();
		double vy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(vy, vx);
		
		e.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
		
		assertEquals(1, ptsAround.size());
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
				Color.BLACK,e,t);
		
		//creature don't see the point
		EnergySource pte = mock(EnergySource.class);
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,110));
		when(pte.getSize()).thenReturn(20);
		when(pte.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());
		double vx = pte.getPosition().getX() - creature.getPosition().getX();
		double vy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(vy, vx);
		
		e.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
		
		assertEquals(0, ptsAround.size());
		assertNotEquals(creature.getDirection(),-angle,0.1);
		assertTrue(distanceAfter >= distanceBefore);
	}
	
}
