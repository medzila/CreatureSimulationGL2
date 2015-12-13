package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import plug.creatures.ColorPluginFactory;
import plug.creatures.BehaviorPluginFactory;
import plug.creatures.MovementPluginFactory;
import creatures.ComposableCreature;
import creatures.ICreature;
import creatures.behavior.IStrategyBehavior;
import creatures.EnergySource;
import creatures.color.IColorStrategy;
import creatures.movement.IStrategieMovement;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;



/**
 * Just a simple test of the simulator.
 *
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {
    
    private final MovementPluginFactory movementFactory;
    private final BehaviorPluginFactory behaviorFactory;
    private final ColorPluginFactory colorFactory;
    
    public Constructor<? extends IStrategyBehavior> behavior = null;
    public IStrategieMovement movement = null;
    public Constructor<? extends IColorStrategy> colorConstructor = null;
    
    private final CreatureInspector inspector;
    private final CreatureVisualizer visualizer;
    private final CreatureSimulator simulator;
    
    private JPanel buttons = new JPanel(new GridBagLayout());
    private JPanel buttonsAndTreshold = new JPanel(new GridBagLayout());
    private JPanel threshold = new JPanel(new GridBagLayout());
    
    public double lengthOfView = 50;
    public double fieldOfView = Math.toRadians(90);
    public int creatureNumber = 10;
    public static int spotsNumber = 10;
    public static int spotsSize = 40;
    
    public static float THRESHOLD = (float) (ComposableCreature.DEFAULT_HEALTH / 2);
    
    public Launcher() {
        
        movementFactory = MovementPluginFactory.getInstance();
        behaviorFactory = BehaviorPluginFactory.getInstance();
        colorFactory = ColorPluginFactory.getInstance();
        
        setName("Creature Simulator Plugin Version");
        setLayout(new BorderLayout());
        add(buttonsAndTreshold, BorderLayout.AFTER_LAST_LINE);
        
        simulator = new CreatureSimulator(new Dimension(640, 480));    
        inspector = new CreatureInspector();
        inspector.setFocusableWindowState(false);
        visualizer = new CreatureVisualizer(simulator);

        visualizer.setDebug(false);
        visualizer.setPreferredSize(simulator.getSize());
        visualizer.add(simulator.getLabelCreaturesTotal());
        visualizer.add(simulator.getLabelCreaturesDead());
        visualizer.add(simulator.getLabelCreaturesLife());
                
        add(visualizer, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exit(evt);
            }
        });
        
    }
    
    
    
    public CreatureSimulator getSimulator() {
		return simulator;
	}



	private void exit(WindowEvent evt) {
        System.exit(0);
    }

    public void buildInterface() {    
        
        buttons.removeAll();
        threshold.removeAll();
        
        GridBagConstraints c = new GridBagConstraints();

        /** THRESHOLD TextField */
        JTextField tresholdTextField = new JTextField(Integer.toString((int)ComposableCreature.DEFAULT_HEALTH));          
        tresholdTextField.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
        		Launcher.THRESHOLD = (float) Integer.parseInt( ((JTextField)e.getSource()).getText());
        	}
        });


        tresholdTextField.setFocusable(false);
        tresholdTextField.setHorizontalAlignment(JTextField.CENTER);
        tresholdTextField.setPreferredSize(new Dimension(20, 20));
        
        
        JPanel TextFieldTresholdPanel = new JPanel(new BorderLayout());
        JLabel labelTreshold = new JLabel("Threshold", JLabel.CENTER);
        TextFieldTresholdPanel.add(labelTreshold, BorderLayout.NORTH);
        TextFieldTresholdPanel.add(tresholdTextField, BorderLayout.SOUTH);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        threshold.add(TextFieldTresholdPanel, c);  
        

        /** Interface part where we define the Color Strategy */
        ActionListener colorListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // the name of the plugin is in the ActionCommand
                colorConstructor = colorFactory.getConstructorMap().get(((JTextField) e.getSource()).getText());
            }
        };
        
        JTextField colorTextField = new JTextField(colorConstructor.getName());
        colorTextField.setFocusable(false);
        colorTextField.setHorizontalAlignment(JTextField.CENTER);
        colorTextField.addActionListener(colorListener);

        
        JPanel choiceColorPanel = new JPanel();
        choiceColorPanel.setLayout(new BorderLayout());
        JLabel labelColorCrea = new JLabel("Color", JLabel.CENTER);
        choiceColorPanel.add(labelColorCrea, BorderLayout.NORTH);
        choiceColorPanel.add(colorTextField, BorderLayout.SOUTH);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        buttons.add(choiceColorPanel, c);
        
        
        /** Interface part where we define the Movement Strategy */
        ActionListener movementListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                // the name of the plugin is in the ActionCommand
                movement = movementFactory.getMap().get(((JTextField) e.getSource()).getText());
            }
        };
        
        JTextField movementTextField = new JTextField(movement.getName());
        movementTextField.addActionListener(movementListener);
        movementTextField.setHorizontalAlignment(JTextField.CENTER);
        movementTextField.setFocusable(false);
        
        JPanel choiceMovementPanel = new JPanel();
        choiceMovementPanel.setLayout(new BorderLayout());
        JLabel labelMovementCrea = new JLabel("Movement", JLabel.CENTER);
        choiceMovementPanel.add(labelMovementCrea, BorderLayout.NORTH);
        choiceMovementPanel.add(movementTextField, BorderLayout.SOUTH);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        buttons.add(choiceMovementPanel,c);
        
        
        /** Interface part where we define the Behavior Strategy */
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // the name of the plugin is in the ActionCommand
                behavior = behaviorFactory.getMap().get(((JTextField) e.getSource()).getText());
            }
        };
        
        JTextField actionTextField = new JTextField(behavior.getName());
        actionTextField.addActionListener(actionListener);
        actionTextField.setHorizontalAlignment(JTextField.CENTER);
        actionTextField.setFocusable(false);
        
        JPanel choiceActionPanel = new JPanel();
        choiceActionPanel.setLayout(new BorderLayout());
        JLabel labelActionCrea = new JLabel("Behavior", JLabel.CENTER);
        choiceActionPanel.add(labelActionCrea, BorderLayout.NORTH);
        choiceActionPanel.add(actionTextField, BorderLayout.SOUTH);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        buttons.add(choiceActionPanel,c);
        
        /** Button where we RELOAD the Color Plugins */
        JButton colorLoader = new JButton("(Re-)load color plugin");
        colorLoader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorFactory.reload();
                buildInterface();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        buttons.add(colorLoader, c);
        
        /** Button where we RELOAD the Movement Plugins */
        JButton movementLoader = new JButton("(Re-)load movement plugin");
        movementLoader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movementFactory.reload();
                buildInterface();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        buttons.add(movementLoader, c);
        
        /** Button where we RELOAD the Behavior Plugins */
        JButton actionLoader = new JButton("(Re-)load acting plugin");
        actionLoader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                behaviorFactory.reload();
                buildInterface();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        buttons.add(actionLoader, c);
   
        
        /** Creatures number TextField */
        JTextField numberOfCreaturesTextField = new JTextField(Integer.toString(creatureNumber));  
        numberOfCreaturesTextField.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
                creatureNumber = Integer.parseInt(((JTextField) e.getSource()).getText());
            }
        });
        
        numberOfCreaturesTextField.setHorizontalAlignment(JTextField.CENTER);
        numberOfCreaturesTextField.setFocusable(false);
        
        
        JPanel TextFieldNumberCreaPanel = new JPanel();
        TextFieldNumberCreaPanel.setLayout(new BorderLayout());
        JLabel labelNumberCrea = new JLabel("Number of Creatures", JLabel.CENTER);
        TextFieldNumberCreaPanel.add(labelNumberCrea, BorderLayout.NORTH);
        TextFieldNumberCreaPanel.add(numberOfCreaturesTextField, BorderLayout.SOUTH);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        buttons.add(TextFieldNumberCreaPanel, c);  
        
        /** Energy Sources number TextField */
        JTextField numberOfEnergyTextField = new JTextField(Integer.toString(spotsNumber));  
        numberOfEnergyTextField.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
                spotsNumber = Integer.parseInt(((JTextField) e.getSource()).getText());
            }
        });


        numberOfEnergyTextField.setHorizontalAlignment(JTextField.CENTER);
        numberOfEnergyTextField.setFocusable(false);
        
        JPanel TextFieldNumberPanel = new JPanel();
        TextFieldNumberPanel.setLayout(new BorderLayout());
        JLabel labelNumber = new JLabel("Number of Energy", JLabel.CENTER);
        TextFieldNumberPanel.add(labelNumber, BorderLayout.NORTH);
        TextFieldNumberPanel.add(numberOfEnergyTextField, BorderLayout.SOUTH);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        buttons.add(TextFieldNumberPanel, c);  

        /** Energy Sources Sizes number TextField */
        JTextField sizeOfEnergyTextField = new JTextField(Integer.toString(spotsSize));  
        sizeOfEnergyTextField.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
                spotsSize = Integer.parseInt(((JTextField) e.getSource()).getText());
            }
        });


        sizeOfEnergyTextField.setHorizontalAlignment(JTextField.CENTER); 
        sizeOfEnergyTextField.setFocusable(false);  
        
        JPanel TextFieldSizePanel = new JPanel();
        TextFieldSizePanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Size of Energy", JLabel.CENTER);
        TextFieldSizePanel.add(label, BorderLayout.NORTH);
        TextFieldSizePanel.add(sizeOfEnergyTextField, BorderLayout.SOUTH);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        buttons.add(TextFieldSizePanel, c);  
        

        /** Button that (RE-)START the simulation */
        JButton restart = new JButton("(Re-)start simulation");
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ((behavior != null) && (movement != null)) {
                    synchronized(simulator) {
                        if (simulator.isRunning()) {
                            simulator.stop();
                        }
                    }
                    Collection<? extends ICreature> creatures = null;
                    double myMaxSpeed = 5;
                    simulator.clearCreatures();
                    simulator.clearSpots();
                    simulator.clearStat();
                    try {
                        creatures = Builder.createCreatures(simulator, creatureNumber, colorConstructor.newInstance(Color.BLUE, creatureNumber),behavior.newInstance(), movement, myMaxSpeed, fieldOfView , lengthOfView);
                        Collection<EnergySource> spots = Builder.createPoints(simulator, spotsNumber, spotsSize);
                        simulator.addAllCreatures(creatures);
                        simulator.addAllSpots(spots);
                        simulator.start();
                        setResizable(false);
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(buttons, exception.getCause().getMessage(), "Missing mandatory behavior", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        buttons.add(restart, c);
        
        /** Button that STOP the simulation */
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	synchronized(simulator) {
                    if (simulator.isRunning()) {
                    	simulator.clearSpots();
                        simulator.clearCreatures();
                        simulator.clearStat();
                        simulator.stop();
                        setResizable(true);
                    }
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        buttons.add(stopButton, c);
        
        c.fill =  GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        buttonsAndTreshold.add(buttons, c);

        c.gridx = 1;
        c.gridy = 0;
        buttonsAndTreshold.add(threshold, c);

        revalidate();
        repaint();
        
    }
    
    public static void launch(HashMap<String, String> dictionnary){
    	Logger.getLogger("plug").setLevel(Level.INFO);
        MovementPluginFactory.init();
        BehaviorPluginFactory.init();
        ColorPluginFactory.init();
        Launcher launcher = new Launcher();
    	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior."+dictionnary.get("BEV")+"Behavior");
    	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement."+dictionnary.get("MOV")+"Movement");
    	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("creatures.color.Color"+dictionnary.get("COL"));
    	launcher.creatureNumber = Integer.parseInt(dictionnary.get("CHC"));
        launcher.spotsNumber = Integer.parseInt(dictionnary.get("EHC"));
        launcher.spotsSize = Integer.parseInt(dictionnary.get("ES"));
        launcher.getSimulator().setExecutionDelay(Integer.parseInt(dictionnary.get("SD")));
        launcher.fieldOfView = Math.toRadians(Integer.parseInt(dictionnary.get("FOV")));
        launcher.lengthOfView = Integer.parseInt(dictionnary.get("LOV"));
        
        launcher.getSimulator().setSnapshot(dictionnary.get("Implementation").equals("Snapshot")? true : false );
        launcher.getSimulator().setMaxTicks(launcher.getSimulator().isSnapshot() ? 0 : Integer.parseInt(dictionnary.get("D")));
        launcher.buildInterface();
        launcher.pack();
        launcher.setVisible(true);
        
    }
//    
//    public static void main(String args[]) {
//        Logger.getLogger("plug").setLevel(Level.INFO) ;
//        MovementPluginFactory.init();
//        BehaviorPluginFactory.init();
//        ColorPluginFactory.init();
//        Launcher launcher = new Launcher();
//        launcher.setVisible(true);
//        /* Easiest simulator*/
//        if (args[0].equals("1")){
//        	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior.StupidBehavior");
//        	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement.TorusMovement");
//        	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("creatures.color.ColorCube");
//        	launcher.creatureNumber = 1;
//            launcher.spotsNumber = 0;
//            Launcher.spotsSize = 100;
//        }
//        /* Immortal */
//        if (args[0].equals("2")){
//        	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior.EmergingBehavior");
//        	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement.TorusMovement");
//        	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("creatures.color.ColorCube");
//        	launcher.creatureNumber = 20;
//            launcher.spotsNumber = 15;
//            Launcher.spotsSize = 50;
//        }
//        /* Change behavior for survive */
//        if (args[0].equals("3")){
//        	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior.EmergingThenEnergyBehavior");
//        	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement.TorusMovement");
//        	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("creatures.color.ColorCube");
//        	launcher.creatureNumber = 5;
//            launcher.spotsNumber = 3;
//            Launcher.spotsSize = 60;
//        }
//        /* Trap */
//        if (args[0].equals("4")){
//        	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior.EmergingThenPredatorBehavior");
//        	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement.BouncingMovement");
//        	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("creatures.color.ColorCube");
//        	launcher.creatureNumber = 15;
//            launcher.spotsNumber = 0;
//            Launcher.spotsSize = 60;
//        }
//        /* Chaotic */
//        if (args[0].equals("5")){
//        	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior.RandomBehavior");
//        	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement.BouncingMovement");
//        	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("creatures.color.ColorCube");
//        	launcher.creatureNumber = 25;
//            launcher.spotsNumber = 5;
//            Launcher.spotsSize = 25;
//        }
//    }
//    
}