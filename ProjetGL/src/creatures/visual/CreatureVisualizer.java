package creatures.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import simulator.ISimulationListener;
import visual.IDrawable;
import visual.Visualizer;
import creatures.ICreature;
import creatures.EnergySource;

@SuppressWarnings("serial")
public class CreatureVisualizer extends Visualizer {

	private CreatureInspector inspector;
	private final CreatureSimulator simulator;
	private boolean debug;
	private Dimension lastSize;

	public CreatureVisualizer(CreatureSimulator simulator) {
		this.simulator = simulator;
		this.lastSize = simulator.getSize();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				handleResize();
			}
		});

		simulator.addSimulationListener(new ISimulationListener() {
			@Override
			public void simulationCycleComputed() {
				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e);
			}
		});
		
	}

	protected void handleResize() {
		synchronized (simulator) {
			simulator.setSize(getSize());
		};		
	}

	protected void handleMouseMoved(MouseEvent event) {
		synchronized (simulator) {
			if (simulator.isRunning()) {
				return;
			}

			if (inspector == null) {
				inspector = new CreatureInspector();
			}

			Point2D point;
			try {
				point = getTransform().inverseTransform(event.getPoint(), null);
			} catch (NoninvertibleTransformException e) {
				throw new RuntimeException(e);
			}
			
			Iterator<ICreature> nearBy = simulator.creaturesNearByAPoint(point, 10.0).iterator();
			
			if (!nearBy.hasNext()) {
				inspector.setCreature(null);
				return;
			}

			if (!inspector.isVisible()) {
				inspector.setVisible(true);
			}

			inspector.setCreature(nearBy.next());
		}
	}

	protected void handleMousePressed(MouseEvent e) {
		synchronized (simulator) {
			if (simulator.isRunning()) {
				simulator.stop();
			} else {
				simulator.start();
			}
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		repaint();
	}

	@Override
	protected void paint(Graphics2D g2) {
		if (debug) {
			paintDebuggingFrame(g2);
		}

		super.paint(g2);
	}

	protected void paintDebuggingFrame(Graphics2D g2) {
		// save color
		Color oldColor = g2.getColor();
		g2.setColor(Color.BLACK);

		// draw coordinates
		g2.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0);
		g2.drawLine(0, -getHeight() / 2, 0, getHeight() / 2);

		// restore color
		g2.setColor(oldColor);
	}

	@Override
	protected Iterable<IDrawable> getDrawables() {
		ArrayList<IDrawable> al = new ArrayList<IDrawable>();
		for (ICreature c : simulator.getCreatures()) {
			al.add(c);
		}
		for (EnergySource p : simulator.getEnergySources()) {
			double newHeight = p.getPosition().getX() * (1.0 -(((double)lastSize.height-(double)getHeight())/(double)getHeight()));
			double newWidth = p.getPosition().getY() * (1.0 -(((double)lastSize.width-(double)getWidth())/(double)getWidth()));
			p.setPosition(new Point2D.Double(newHeight , newWidth));
			al.add(p);
		}
		this.lastSize = simulator.getSize();
		return al;
	}

}
