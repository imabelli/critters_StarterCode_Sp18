package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
/**
 * This class executes Critters lab, which simulates a Critter world with critters that reproduce, move, die, and fight
 * @author Isabel Li
 * @version 1.0
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console

  
    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    
    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }
        System.out.print("critters>");	//user input prompt
        String userInput = kb.nextLine();
        String[] splitInput = userInput.trim().split("\\s+");	//split user input
        //add start of controller to parse user input
        while(!(splitInput[0].equals("quit") && (splitInput.length) == 1)) {
        	if(splitInput[0].equals("show")) {	//show command
        		if(splitInput.length == 1) {
        			assignment4.Critter.displayWorld();
        		}
        		else {
        			System.out.println("error processing: " + userInput);
        		}
        	} else if(splitInput[0].equals("step")) {	//process step command
        		stepCommand(splitInput, userInput);
        	} else if(splitInput[0].equals("seed")) {	//process seed command
        		seedCommand(splitInput, userInput);
        	} else if(splitInput[0].equals("make")) {	//process make command
        		makeCommand(splitInput, userInput);
        	} else if(splitInput[0].equals("stats")) {	//process stats command
        		statsCommand(splitInput, userInput);
        	}  else if(splitInput[0].equals("seed")) {	//process seed command
        		seedCommand(splitInput, userInput);
        	}else {
        		System.out.println("invalid command: " + userInput);	//invalid command
        	}
        	System.out.print("critters>");	//reprompt for user input
        	userInput = kb.nextLine();	
        	splitInput = userInput.trim().split("\\s+");
        }
        System.out.flush();
    }
    
    /**
     * statsCommand is called when user requests stats
     * @param splitInput is array of individual words from user input
     * @param userInput is the user input string, unparsed
     */
    private static void statsCommand(String[] splitInput, String userInput) {
    	if(splitInput.length != 2) {	//input must always be 2 words with stats command
    		System.out.println("error processing: " + userInput);
    	} else {
    		String className = splitInput[1];	//this must be the object type we want stats of
        	boolean validCritter = true;
        	List<Critter> instances;
        	try {
        		instances = Critter.getInstances(className);
        	} catch(InvalidCritterException ice) {
        		System.out.println("error processing: " + userInput);
        		validCritter = false;
        	}		
    	}
    }
    
    /**
     * stepCommand is called when user requests step(s)
     * @param splitInput is array of individual words from user input
     * @param userInput is the user input string, unparsed
     */
    private static void stepCommand(String[] splitInput, String userInput) {
    	int numSteps = 1;
		if(splitInput.length != 1) {	//user has tried to specify number of steps
			if(splitInput.length == 2) {
				try {
    				numSteps = Integer.parseInt(splitInput[1]);
    			} catch(NumberFormatException e) {
    				numSteps = 0;
    				System.out.println("error processing: " + userInput);
    			}
			} else {	//user has added extraneous information
				System.out.println("error processing: " + userInput);
				numSteps = 0;
			}
		}
		for(int i = 0; i < numSteps; i++) {
			Critter.worldTimeStep();
		}
    }
    
    /**
     * seedCommand is called when user requests to seed random
     * @param splitInput is array of individual words from user input
     * @param userInput is the user input string, unparsed
     */
    private static void seedCommand(String[] splitInput, String userInput) {
    	int seed = 1;
		if(splitInput.length != 1) {	//user has tried to specify number of steps
			if(splitInput.length == 2) {
				try {
    				seed = Integer.parseInt(splitInput[1]);
    				Critter.setSeed(seed);
    			} catch(NumberFormatException e) {
    				seed = 0;
    				System.out.println("error processing: " + userInput);
    			}
			} else {	//user has added extraneous information
				System.out.println("error processing: " + userInput);
				seed = 0;
			}
		}
    }
    
    /**
     * statsCommand is called when user wants to make Critters
     * @param splitInput is array of individual words from user input
     * @param userInput is the user input string, unparsed
     */
    private static void makeCommand(String[] splitInput, String userInput) {
    	int numCritters = 1;	//1 Critter made by default
    	String critterType = splitInput[1];
    	if(splitInput.length != 2) {	//user has tried to specify number of critters
    		if(splitInput.length == 3) {
    			try {
    				numCritters = Integer.parseInt(splitInput[2]);
    			} catch(NumberFormatException e) {
    				numCritters = 0;
    				System.out.println("error processing: " + userInput);
    			}
    		} else {	//user has entered extraneous information
    			System.out.println("error processing: " + userInput);
    			numCritters = 0;
    		}
    	}
    	boolean validCritter = true;
    	if(splitInput.length < 4) {
    		try {
        		Critter.makeCritter(critterType);
        	} catch(InvalidCritterException ice) {	//invalid critter
        		System.out.println("error processing: " + userInput);	
        		validCritter = false;
        	}		
        	if(validCritter) {
        		for(int i = 1; i < numCritters; i++) {
        			try {
        				Critter.makeCritter(critterType);
        			} catch(InvalidCritterException ice) {	//should never be executed, validCritter eliminates need so we don't keep reprinting error message	
        			}
        	}
        	}
    	}
    }
}
