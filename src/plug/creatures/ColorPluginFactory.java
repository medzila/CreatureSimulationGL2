package plug.creatures;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import creatures.color.IColorStrategy;
import plug.IPlugin;
import plug.PluginLoader;

public class ColorPluginFactory {

	/**
	 * singleton for the abstract factory
	 */
	protected static ColorPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "pluginrepo";
	
	protected Map<String,Constructor<? extends IColorStrategy>> constructorMap; 

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
             _singleton = new ColorPluginFactory();
        }
     }

    /**
     * Get the instance of the singleton of the factory
     * @return The unique instance of the ColorPluginFactory
     */
    public static ColorPluginFactory getInstance() {
    	return _singleton;
    }

    private ColorPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IColorStrategy.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,Constructor<? extends IColorStrategy>>();
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
     * Builds the constructorMap using all the plugins compatible with IColorStrategy
     */
	@SuppressWarnings("unchecked")
	private void buildConstructorMap() {
		for (Class<? extends IPlugin> p : pluginLoader.getPluginClasses()) {
			Constructor<? extends IColorStrategy> c = null;

			try {				
				c = (Constructor<? extends IColorStrategy>)p.getDeclaredConstructor(Color.class, int.class);
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
	
	
	public Map<String,Constructor<? extends IColorStrategy>> getConstructorMap() {
		return constructorMap;
	}
}
