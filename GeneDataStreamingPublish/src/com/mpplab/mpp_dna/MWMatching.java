package com.mpplab.mpp_dna;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MWMatching {

	String inFile;
	String outFile;
	PrintStream ps;
	String command;
	Process p = null;
	
	public MWMatching(String inFile,String outFile) {

		String clustalPath = public_data.MWMPath;
	    this.inFile = inFile;
	    this.outFile = outFile;

	    System.out.println("OS = " + System.getProperty("os.name"));
	    
	    command = clustalPath + " -i " + inFile + " -o " + outFile;
	}
	
	public List<int[]> calculate()
	{
    	List<int[]> result = new ArrayList<int[]>();
    	
	    System.out.println("Running command: " +  command);
	    try {
	    	p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", command});
	    	
	    	StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "Error");  
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "Output");  
            errorGobbler.start();  
            outputGobbler.start();
	    	
	    	p.waitFor();
	    	result = new FileHelper().readMWMResultFromFile(this.outFile);
	    } catch( java.io.EOFException eof ) { 
	    	System.out.println("Exception : " + eof);
	    } catch (java.io.IOException e) {
	    	System.out.println("Exception : " + e);
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
    	return result;
	}    
}
