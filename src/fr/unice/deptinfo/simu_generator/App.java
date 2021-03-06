package fr.unice.deptinfo.simu_generator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
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
public class App 
{

	public static HashMap<String, String> dictionnary = new HashMap<>();
	public static Pattern pattern;
	public static Matcher matcher;
	
	/**
	 * Initialize the dictionnary
	 */
	public static void initDictionnary(){
		dictionnary.put("BEV", "Random");
		dictionnary.put("MOV", "Torus");
		dictionnary.put("CHC", "1");
		dictionnary.put("EHC", "0");
		dictionnary.put("COL", "Cube");
		dictionnary.put("LOV", "50");
		dictionnary.put("FOV", "45");
		dictionnary.put("ES", "0");
		dictionnary.put("SD", "0");
		dictionnary.put("Duration", "1000");
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
    	//init the dict
    	initDictionnary();
    	System.out.println(dictionnary);
    	
    	String fmName = "fmSimu";
    	String configName = "config1";
    	System.out.println("\n\n\n"+dictionnary+"\n\n\n");
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
    	
    }
}
