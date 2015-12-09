package simulator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Simulator<T extends IActionable> { 
	
	private final List<ISimulationListener> listeners = new CopyOnWriteArrayList<ISimulationListener>();
	
	/** Execution delay in milliseconds */
	private volatile int executionDelay;

	/** Animation thread. */
	private Thread thread;
	
	/**Ticks thread*/
	private int ticks = 0;
	
	/**Stop after maxTicks*/
	private int maxTicks = 0;

	/** A flag for controlling the simulation thread */
	private volatile boolean running = false;

	protected final List<T> actionables;
	
	public Simulator(List<T> actionables, int initialExecutionDelay) {
		this.actionables = actionables;
		setExecutionDelay(initialExecutionDelay);
	}

	public void start() {
		if (running) {
			throw new IllegalStateException("Simulation is already running.");
		}

		// the reason we do not inherit from Runnable is that we do not want to
		// expose the void run() method to the outside world. We want to well
		// encapsulate the whole idea of a thread.
		// thread cannot be restarted so we need to always create a new one
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread = new Thread() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				while (running) {
					if((ticks >= maxTicks) && (maxTicks != 0) ){
						this.stop();
					}
					try {
						synchronized (this) {
							Thread.sleep(executionDelay);
						}
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					simulate();
					notifySimulationListeners();
					ticks+=1;
				}
			}
		};
		// set the flag
		running = true;
		// start the thread
		thread.start();

	}

	public void notifySimulationListeners() {
		for (ISimulationListener l : listeners) {
			l.simulationCycleComputed();
		}
	}

	public void addSimulationListener(ISimulationListener listener) {
		listeners.add(listener);
	}
	
	public void removeSimulationListener(ISimulationListener listener) {
		listeners.remove(listener);
	}
	
	protected void simulate() {
		for (IActionable e : actionables) {
			e.act();
		}
	}

	public void stop() {
		if (!running) {
			throw new IllegalStateException("Simulation is stopped.");
		}
		running = false;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized int getExecutionDelay() {
		return executionDelay;
	}

	public synchronized void setExecutionDelay(int executionDelay) {
		if (executionDelay < 0) {
			throw new IllegalArgumentException(
					"Execution delay must be greater than zero.");
		}
		this.executionDelay = executionDelay;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	

}
