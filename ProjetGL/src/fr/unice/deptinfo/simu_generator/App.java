package fr.unice.deptinfo.simu_generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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
	public static String affiche(String fichier) throws IOException {
		String ligne = "";

		BufferedReader ficTexte;
		try {
			ficTexte = new BufferedReader(new FileReader(new File(fichier)));
			if (ficTexte == null) {
				throw new FileNotFoundException("Fichier non trouvé: "
						+ fichier);
			}
			do {
				ligne += ficTexte.readLine();
			} while (ficTexte != null);
			ficTexte.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ligne;

	}
    public static void main( String[] args ) throws IOException
    {

    	//String FM = affiche("src/commons/feature.fml");
    	//System.out.println(FM);
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
	        if(fi.getSelectedFeature(configName).contains("C_1")){
	        	Launcher.launch(1);
	        }
	        if(fi.getSelectedFeature(configName).contains("C_10")){
	        	Launcher.launch(10);
	        }
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
