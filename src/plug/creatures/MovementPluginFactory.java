package plug.creatures;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import plug.IPlugin;
import plug.PluginLoader;
import creatures.movement.IStrategieMovement;

public class MovementPluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static MovementPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "pluginrepo";
	
	protected Map<String,IStrategieMovement> constructorMap; 

	/**
	   * logger facilities to trace plugin loading...
	   */
	private static Logger logger = Logger.getLogger("plug.DeplacementPluginFactory");
	
	/**
	 * Initialize the PluginFactory
	 */
    public static void init() {
        if (_singleton != null) {
            throw new RuntimeException("CreatureFactory already created by " 
				  + _singleton.getClass().getName());
        } else {
             _singleton = new MovementPluginFactory();
        }
     }

    /**
     * Get the instance of the singleton of the factory
     * @return The unique instance of the ColorPluginFactory
     */
    public static MovementPluginFactory getInstance() {
    	return _singleton;
    }

    private MovementPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IStrategieMovement.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,IStrategieMovement>();
    	load();
    }
	
    /**
     * Loads all the plugin into the constructorMap
     */
    public void load() {
    	pluginLoader.loadPlugins();
    	buildConstructorMap();
    }
    
    /**
     * Clears the contructorMap and reloads the plugins into the constructorMap
     */
    public void reload() {
    	pluginLoader.reloadPlugins();
    	constructorMap.clear();
    	buildConstructorMap();
    }
    
    /**
     * Builds the constructorMap using all the plugins compatible with IStrategieMovement
     */
	@SuppressWarnings("unchecked")
	private void buildConstructorMap() {
		for (Class<? extends IPlugin> p : pluginLoader.getPluginClasses()) {
			Constructor<? extends IStrategieMovement> c = null;

			try {				
				c = (Constructor<? extends IStrategieMovement>)p.getDeclaredConstructor();
				c.setAccessible(true);
			} catch (SecurityException e) {
				logger.info("Cannot access (security) constructor for plugin" + p.getName());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.info("No constructor in plugin " + p.getName() + " with the correct signature");
				e.printStackTrace();
			}
			if (c != null)
				try {
					constructorMap.put(p.getName(),c.newInstance());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public Map<String,IStrategieMovement> getMap() {
		return constructorMap;
	}
}
