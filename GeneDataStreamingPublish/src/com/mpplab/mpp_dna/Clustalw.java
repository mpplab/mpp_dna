package com.mpplab.mpp_dna;

import java.io.PrintStream;

/*
 * Introduction: clustalw class
 * 
 * */
public class Clustalw {

	String inFile;
	String outFile;
	PrintStream ps;
	String command;
	Process p = null;
	
	public Clustalw(String inFile,String outFile) {

		String clustalPath = public_data.ClustalPath;
	    this.inFile = inFile;
	    this.outFile = outFile;

	    if(public_data.Debug) System.out.println("OS = " + System.getProperty("os.name"));
	    
	    if (System.getProperty("os.name").equals("Windows 95")) { 
	      command = clustalPath + " /inFile=" + inFile + " /outFile=" + outFile + " /outorder=input";
	    } else {
	      command = clustalPath + " -inFile=" + inFile + " -outFile=" + outFile + " /outorder=input";
	    }
	}
	
	public void start()
	{
		if(public_data.Debug) System.out.println("Running command: " +  command);
	    try {
	    	p = Runtime.getRuntime().exec(command);
	       
	    	StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "Error");  
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "Output");  
            errorGobbler.start();  
            outputGobbler.start();
	    	
	    	p.waitFor();
	
//	    	System.out.println("Command thread is done");
	
	    } catch( java.io.EOFException eof ) { 
	    	System.out.println("Exception : " + eof);
	    } catch (java.io.IOException e) {
	    	System.out.println("Exception : " + e);
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
