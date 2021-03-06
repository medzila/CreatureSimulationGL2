package config;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Launcher;

import fr.unice.deptinfo.familiar_interpreter.FMEngineException;
import fr.unice.deptinfo.familiar_interpreter.impl.FamiliarInterpreter;
import fr.unice.polytech.modalis.familiar.interpreter.VariableNotExistingException;
import fr.unice.polytech.modalis.familiar.parser.VariableAmbigousConflictException;
import fr.unice.polytech.modalis.familiar.variable.FeatureModelVariable;

/**
 * Hello world!
 *
 */
public class ConfigHandler 
{

	public static HashMap<String, String> dictionnary = new HashMap<>();
	public static Pattern pattern;
	public static Matcher matcher;
	
	public static String pathOfFML = "src/commons/simu.fml";
	
	public ConfigHandler(){
		initDictionnary();
	}

	public void launchConfig(String configuration){
		String fmName = "fmSimu";
    	String configName = "config";
 
    	FamiliarInterpreter fi = FamiliarInterpreter.getInstance();
    	try {
			fi.evalFile(pathOfFML);
			FeatureModelVariable fmv = fi.getFMVariable(fmName);
	    	
	    	System.out.println("Instantiated FM : "+fmv.getSyntacticalRepresentation());
	    	
	    	fi.eval(configName+" = configuration "+fmName);
	    	
	    	System.out.println("Selected avant: "+fi.getSelectedFeature(configName));
	    	fi.evalFile("config/"+configuration+".fml");
	    	System.out.println("Selected apres: "+fi.getSelectedFeature(configName));
	    	
	        System.out.println("\n\n\n"+dictionnary+"\n");
	        parse(new ArrayList<String>(fi.getSelectedFeature(configName)));
	    	System.out.println(dictionnary);
	        
		} catch (FMEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VariableNotExistingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VariableAmbigousConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Initialize the dictionnary
	 */
	public static void initDictionnary(){
		dictionnary.put("BEV", "Random");              	// Behavior: Random, Emerging, ...
		dictionnary.put("MOV", "Torus");				// Movement: Torus, Bouncing
		dictionnary.put("CHC", "1");					// CreaturesHeadcount: 1, 25, 50 
		dictionnary.put("EHC", "1");					// EnergySourcesHeadcount: 1, 5, 10
		dictionnary.put("COL", "Cube");					// Color: Cube or Unique
		dictionnary.put("LOV", "50");					// LengthOfView: 50, 75, 100
		dictionnary.put("FOV", "45");					// FieldOfView: 45, 90, 360
		dictionnary.put("ES", "20");					// EnergySourcesSize: 20, 50, 1000
		dictionnary.put("SD", "10");					// Sleepduration: 0, 10, 200, 1000
		dictionnary.put("D", "5000");					// Duration: 1000, 5000, 10000
		dictionnary.put("Implementation", "RealTime"); 	// Visual: can be RealTime or Snapshot
		dictionnary.put("THS","50");					// Treshold: 0, 50, 100
	}
	
	/**
	 * Parse an arraylist of strings and store in the dictionnary
	 * @param toParse an {@link ArrayList} of String 
	 */
	public static void parse(ArrayList<String> toParse){
		pattern = Pattern.compile(".+_.+");
		String[] splitted;
		
		// for each feature
		for(String feature : toParse){
			matcher = pattern.matcher(feature);
			
			// if a feature is like: 1_2, split it and store it in the dictionnary with key=1 and value=2 
			if(matcher.matches()){
				splitted = feature.split("_");
				String key = splitted[0];
				String value = splitted[1];
				
				dictionnary.put(key, value);
			}
		}
	}
	
    public static void main( String[] args ) throws IOException
    {
    	
    	ConfigHandler c = new ConfigHandler();
    	c.launchConfig(args[0]);
    	Launcher.launch(c.dictionnary);
    	
    	/*//init the dict
    	
    	initDictionnary();
    	
    	String fmName = "fmSimu";
    	String configName = "config1";
 
    	FamiliarInterpreter fi = FamiliarInterpreter.getInstance();
    	try {
			fi.evalFile("src/commons/simu.fml");
			FeatureModelVariable fmv = fi.getFMVariable(fmName);
	    	
	    	System.out.println("Instantiated FM : "+fmv.getSyntacticalRepresentation());
	    	
	    	fi.eval(configName+" = configuration "+fmName);
	    	
	        Scanner scan = new Scanner(System.in);
	        String s = "";
	        String selectCmd = "select ";
	        
	        do {
	        	System.out.println("Enter the name of features you wish to select, or type exit to exit.");
	        	s = scan.nextLine();
	        	if (!s.equals("exit")) {
		        	fi.eval(selectCmd+s+" in "+configName);
		        	System.out.println("Selected features :"+fi.getSelectedFeature(configName));
		        	System.out.println("Deselected features :"+fi.getDeselectedFeature(configName));
		        	System.out.println("Unselected features :"+fi.getUnselectedFeature(configName));
	        	}
	        } while (!s.equals("exit"));
	        
	        System.out.println("\n\n\n"+dictionnary+"\n");
	        parse(new ArrayList<String>(fi.getSelectedFeature(configName)));
	    	System.out.println(dictionnary);
	        
		} catch (FMEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VariableNotExistingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VariableAmbigousConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	
    }
}
