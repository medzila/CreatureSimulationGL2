package plug.creatures;


import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import plug.IPlugin;
import plug.PluginLoader;
import creatures.behavior.IStrategyBehavior;

public class BehaviorPluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static BehaviorPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "pluginrepo";
	
	protected Map<String,Constructor<? extends IStrategyBehavior>> constructorMap; 

	/**
	   * logger facilities to trace plugin loading...
	   */
	private static Logger logger = Logger.getLogger("plug.ComportementPluginFactory");
	
	/**
	 * Initialize the PluginFactory
	 */
    public static void init() {
        if (_singleton != null) {
            throw new RuntimeException("CreatureFactory already created by " 
				  + _singleton.getClass().getName());
        } else {
             _singleton = new BehaviorPluginFactory();
        }
     }

    /**
     * Get the instance of the singleton of the factory
     * @return The unique instance of the BehaviorPluginFactory
     */
    public static BehaviorPluginFactory getInstance() {
    	return _singleton;
    }

    private BehaviorPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IStrategyBehavior.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,Constructor<? extends IStrategyBehavior>>();
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
     * Builds the constructorMap using all the plugins compatible with IStrategyBehavior
     */
	@SuppressWarnings("unchecked")
	private void buildConstructorMap() {
		for (Class<? extends IPlugin> p : pluginLoader.getPluginClasses()) {
			Constructor<? extends IStrategyBehavior> c = null;

			try {				
				c = (Constructor<? extends IStrategyBehavior>)p.getDeclaredConstructor();
				c.setAccessible(true);
			} catch (SecurityException e) {
				logger.info("Cannot access (security) constructor for plugin" + p.getName());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.info("No constructor in plugin " + p.getName() + " with the correct signature");
				e.printStackTrace();
			}
			if (c != null)
				constructorMap.put(p.getName(),c);
		}
	}
	
	public Map<String,Constructor<? extends IStrategyBehavior>> getMap() {
		return constructorMap;
	}
	
	public static void delete() {
		_singleton = null;
	}

}