package creatures;

import static commons.Utils.filter;
import static commons.Utils.mkString;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import commons.Utils.Predicate;
import creatures.behavior.IStrategyBehavior;
import creatures.movement.IStrategieMovement;
import creatures.visual.CreatureSimulator;
import main.Launcher;

public class ComposableCreature implements ICreature, ImageObserver {

	IStrategyBehavior behavior;
	IStrategieMovement movement;
	
	public int currCycle;
	
	public boolean target;

	public static final int DEFAULT_SIZE = 80;
	public static final int DEFAULT_VISION_DISTANCE = 50;
	public static final double DEFAULT_HEALTH = 100d;
	public static final double DEFAULT_LOSS_HEALTH = 0.05d;
	public static final double DEFAULT_GAINED_HEALTH = 5d;
	public static final int DEFAULT_TICKS_BEFORE_BURN = (int)(Launcher.spotsSize/2);
	public static final String FLAME_IMAGE_PATH = "src/commons/flame.png";
	public static final String TEETH_IMAGE_PATH = "src/commons/teeth.png";
	public static final double MAX_SPEED = 5d;
	public static BufferedImage imgFlame;
	public static BufferedImage imgTeeth;
	
	/** If is hunting another creature */
	private boolean isHunting = false;
	
	/** Health at the init */
	protected double health = DEFAULT_HEALTH;
	
	/** Health lost at each tick */
	protected double lossHealth = DEFAULT_LOSS_HEALTH;
	
	/** Health gained when near an energy point */
	protected double gainedHealth = DEFAULT_GAINED_HEALTH;
	
	/** Ticks before the creature dies */
	protected int currentTicksOnEnergyPoint = 0;
	
	/** If the creature is burning */
	protected boolean isBurning = false;

	/** Indicate if the creature is dead*/
	protected boolean isDead = false;
	
	/**
	 * The field of view (FOV) is the extent of the observable world that is
	 * seen at any given moment by a creature in radians.
	 */
	protected double fieldOfView = (PI / 4);

	/**
	 * The distance indicating how far a creature see in front of itself in
	 * pixels.
	 */
	protected double visionDistance = DEFAULT_VISION_DISTANCE;

	/** Position */
	protected Point2D position;

	/** Speed in pixels */
	protected double speed;

	/** Direction in radians (0,2*pi) */
	protected double direction;

	/** Color of the creature */
	protected Color color;

	/** Reference to the environment */
	protected final IEnvironment environment;

	/** Size of the creature in pixels */
	protected final int size = DEFAULT_SIZE;
	
	/**
	 * Create a Creature that Move and Act depending on a {@link IStrategieMovement} and {@link IStrategyBehavior}.
	 * @param environment
	 * @param position
	 * @param direction
	 * @param speed
	 * @param color
	 * @param behav
	 * @param move
	 */
	public ComposableCreature(IEnvironment environment, Point2D position, double direction, double speed,
			Color color, IStrategyBehavior behav, IStrategieMovement move) {
		this.environment=environment;
		this.position=position;
		this.direction=direction;
		this.speed=speed;
		this.color=color;
		this.behavior = behav;
		this.movement = move;
		this.currCycle = 0;
		this.target = false;
		try{
			imgFlame = ImageIO.read(new File(FLAME_IMAGE_PATH));
		}catch(IOException e){
		}
		try{
			imgTeeth = ImageIO.read(new File(TEETH_IMAGE_PATH));
		}catch(IOException e){
		}
	}


	// ----------------------------------------------------------------------------
	// Getters and Setters
	// ----------------------------------------------------------------------------

	
	public boolean isHunting() {
		return isHunting;
	}

	public void setHunting(boolean isHunting) {
		this.isHunting = isHunting;
	}

	public boolean isDead() {
		return isDead;
	}
	
	public void die(){
		isDead = true;
		CreatureSimulator env = (CreatureSimulator) environment;
		env.removeCreature(this);
	}
	
	@Override
	public double getHealth() {
		return health;
	}
	
	public void setHealth(double health) {
		this.health = health;
		if(this.health <= 0){
			this.health = 0;
			die();
		}
		if(health >= DEFAULT_HEALTH)
			this.health = DEFAULT_HEALTH;
	}
	
	@Override
	public double getLossHealth() {
		return lossHealth;
	}
	
	public void setLossHealth(double lossHealth) {
		this.lossHealth = lossHealth;
	}
	
	@Override
	public double getGainedHealth() {
		return gainedHealth;
	}
	
	public void setGainedHealth(double gainedHealth) {
		this.gainedHealth = gainedHealth;
	}
	
	public int getCurrentTicksOnEnergyPoint() {
		return currentTicksOnEnergyPoint;
	}

	public void setCurrentTicksOnEnergyPoint(int currentTicksOnEnergyPoint) {
		this.currentTicksOnEnergyPoint = currentTicksOnEnergyPoint;
	}

	public boolean isBurning() {
		return isBurning;
	}
	
	@Override
	public IEnvironment getEnvironment() {
		return environment;
	}
	
	public double getFieldOfView() {
		return fieldOfView;
	}
	
	public void setFieldOfView(double fieldOfView){
		this.fieldOfView = fieldOfView;
	}
	
	public void setLengthOfView(double lengthOfView){
		this.visionDistance=lengthOfView;
	}


	public double getLengthOfView() {
		return visionDistance;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
		if(this.speed>=MAX_SPEED)
			this.speed = MAX_SPEED;
	}

	@Override
	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction % (PI * 2);
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(position.getX(), position.getY());
	}

	public void setPosition(Point2D position) {
		setPosition(position.getX(), position.getY());
	}

	public void setPosition(double x, double y) {
		Dimension s = environment.getSize();
		
		if (x > s.getWidth() / 2) {
			x = -s.getWidth() / 2;
		} else if (x < -s.getWidth() / 2) {
			x = s.getWidth() / 2;
		}

		if (y > s.getHeight() / 2) {
			y = -s.getHeight() / 2;
		} else if (y < -s.getHeight() / 2) {
			y = s.getHeight() / 2;
		}

		this.position = new Point2D.Double(x, y);
	}
	
	public int getCurrCycle(){
		return this.currCycle;
	}
	
	public void setCurrCycle(int i){
		this.currCycle = i;
	}
	
	public boolean getTarget(){
		return this.target;
	}
	
	public void setTarget(boolean b){
		this.target = b;
	}

	// ----------------------------------------------------------------------------
	// Movement & Behavior handling methods 
	// ----------------------------------------------------------------------------
	
	@Override
	public void move() {
		this.movement.setNextPosition(this);
	}

	@Override
	public void act() {
		this.behavior.setNextDirectionAndSpeed(this);
		
		gainOrLoseHealth();	
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	

	// ----------------------------------------------------------------------------
	// Health handling methods
	// ----------------------------------------------------------------------------
	
	/**
	 * Decrease health depending on {@link ComposableCreature#lossHealth}.
	 * If health is lower than 0, set health 0.
	 */
	public void looseHealth(){
		health -= lossHealth;
		if(health <= 0 && !isDead()){
			health = 0;
			die();
		}
	}
	
	/**
	 * Increments health depending on {@link ComposableCreature#gainedHealth}
	 * if health is higher than 100, set health 100.
	 */
	public void gainHealth(){
		health += gainedHealth;
		if(health >= 100)
			health = 100;
	}
	
	/**
	 * @return <code>true</code> if the creature is near a {@link EnergySource}.
	 */
	public boolean isOnAnEnergySource(){
		ArrayList<EnergySource> tab = (ArrayList<EnergySource>) environment.getEnergySources();
		if (!tab.isEmpty()) {
			for(EnergySource p : tab)
				if(distanceFromAPoint(p.position) <= p.getSize()/2)
					return true;
		}
		return false;
	}
	
	/**
	 * If the creature is near a {@link EnergySource}, {@link ComposableCreature#gainHealth()}.
	 * Else, {@link ComposableCreature#looseHealth()}.
	 * If a creature stay more time than {@link ComposableCreature#DEFAULT_TICKS_BEFORE_BURN}
	 */
	public void gainOrLoseHealth(){
		//If the creature is on an energy source
		if(isOnAnEnergySource()){
			//And if we didn't go over the default ticks before burning
			if(getCurrentTicksOnEnergyPoint() <= DEFAULT_TICKS_BEFORE_BURN){
				//we increment the current ticks on energy point
				currentTicksOnEnergyPoint++;
				//and we gain health
				gainHealth();
			}
			else
				//If the creature stay too long on an energy point, burns
				burn();
		}
		else{
			//If the creature isn't on an energy source and it just 
			//left the energy source, set currentTicksOnEnergyPoint at 0
			if(currentTicksOnEnergyPoint >= 0)
				currentTicksOnEnergyPoint = 0;
			//If the creature is burning and it just left the energy source
			//the creature is no more burning
			if(isBurning){
				isBurning = false;
				setLossHealth(DEFAULT_LOSS_HEALTH);
			}
			looseHealth();
		}
	}
	
	/**
	 * If a creature is burning, lose 20 times more health than usually.
	 */
	public void burn(){
		isBurning = true;
		setLossHealth(DEFAULT_LOSS_HEALTH*20);
		looseHealth();
	}
	
	// ----------------------------------------------------------------------------
	// Positioning methods
	// ----------------------------------------------------------------------------

	protected void move(double incX, double incY) {
		setPosition(position.getX() + incX, position.getY() + incY);
	}

	protected void rotate(double angle) {
		this.direction += angle;
	}

	// ----------------------------------------------------------------------------
	// Methods for calculating the direction
	// ----------------------------------------------------------------------------

	/**
	 * Computes the direction between the given point {@code (x1, y1)} and the
	 * current position in respect to a given {@code axis}.
	 * 
	 * @return direction in radians between given point and current position in
	 *         respect to a given {@code axis}.
	 */
	@Override
	public double directionFormAPoint(Point2D p, double axis) {
		double b = 0d;

		// use a inverse trigonometry to get the angle in an orthogonal triangle
		// formed by the points (x,y) and (x1,y1)
		if (position.getX() != p.getX()) {
			// if we are not in the same horizontal axis
			b = atan((position.getY() - p.getY()) / (position.getX() - p.getX()));
		} else if (position.getY() < p.getY()) {
			// below -pi/2
			b = -PI / 2;
		} else {
			// above +pi/2
			b = PI / 2;
		}

		// make a distinction between the case when the (x1, y1)
		// is right from the (x,y) or left
		if (position.getX() < p.getX()) {
			b += PI;
		}

		// align with the axis of the origin (x1,y1)
		b = b - axis;

		// make sure we always take the smaller angle
		// keeping the range between (-pi, pi)
		if (b >= PI)
			b = b - PI * 2;
		else if (b < -PI)
			b = b + PI * 2;

		return b % (PI * 2);
	}

	/**
	 * Distance between the current position and a given point {@code(x1, y1)}.
	 * 
	 * @return distance between the current position and a given point.
	 */
	@Override
	public double distanceFromAPoint(Point2D p) {
		return getPosition().distance(p);
	}


	// ----------------------------------------------------------------------------
	// Painting
	// ----------------------------------------------------------------------------

	@Override
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(position.getX(), position.getY());
		//g2.fillOval(0,0,5,5);
		// center the surrounding rectangle
		g2.translate(-size / 2, -size / 2);
		if(isHunting){
			g2.drawImage(imgTeeth, 20, 20, 25, 35, this);
		}
		if(isBurning){
			g2.drawImage(imgFlame, 20, 20, 20, 35, this);
		}
		// center the arc
		// rotate towards the direction of our vector
		g2.rotate(-direction, size / 2, size / 2);

		// useful for debugging
		// g2.drawRect(0, 0, size, size);
		
		// we need to do PI - FOV since we want to mirror the arc
		g2.setColor(Color.GRAY);
		g2.fillArc(0, 0, size, size, (int) toDegrees(-fieldOfView / 2),
				(int) toDegrees(fieldOfView));
		
		//g2.fillOval(0,0,5,5);
		// set the color
		g2.setColor(color);
		
		////// Draw "Health pov"
		// Find the relation between the current health and max health
		double relationHealth = health/DEFAULT_HEALTH;
		// Calculate the new size
		int newSize = (int)(size*relationHealth);
		// Calculate the new x,y
		int newCoor = (int)((size-newSize)/2);
		g2.fillArc(newCoor, newCoor, newSize, newSize, (int) toDegrees(-fieldOfView / 2),
				(int) toDegrees(fieldOfView));
		
		//////
		
		////// Draw "classic" HEALTHBAR
		// Uncomment to enable
		//g2.setColor(Color.GRAY);
		//g2.fillRect(0, 0, (int)DEFAULT_HEALTH/2, 10);
		//g2.setColor(Color.GREEN);
		//g2.fillRect(0, 0, (int)health/2, 10);
	}

	// ----------------------------------------------------------------------------
	// Description
	// ----------------------------------------------------------------------------

	public String toString() {
		Class<?> cl = getClass();

		StringBuilder sb = new StringBuilder();
		sb.append(getFullName(cl));
		sb.append("\n---\n");
		sb.append(mkString(getProperties(cl), "\n"));

		return sb.toString();
	}

	private List<String> getProperties(Class<?> clazz) {
		List<String> properties = new ArrayList<String>();

		Iterable<Field> fields = filter(
				Arrays.asList(clazz.getDeclaredFields()),
				new Predicate<Field>() {
					@Override
					public boolean apply(Field input) {
						return !Modifier.isStatic(input.getModifiers());
					}
				});

		for (Field f : fields) {
			String name = f.getName();
			Object value = null;

			try {
				value = f.get(this);
			} catch (IllegalArgumentException e) {
				value = "unable to get value: " + e;
			} catch (IllegalAccessException e) {
				value = "unable to get value: " + e;
			} finally {
				properties.add(name + ": " + value);
			}
		}

		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			properties.addAll(getProperties(superclass));
		}

		return properties;
	}

	private String getFullName(Class<?> clazz) {
		String name = clazz.getSimpleName();
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			return getFullName(superclass) + " > " + name;
		} else {
			return name;
		}
	}
	
	public String getName() {
		return getClass().getName();
	}

}
