package com.mpplab.mpp_dna;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Introduction: Pretreatment of .fasta file 
 * */
public class pre_FastaData {

	// Divide the sequence of a .Fasta file containing multiple sequences into multiple .Fasta files, each file is a sequence
	public static int startOriginalSplit(String inputFile, String outputPath){
		int seqCount = 0;
		
		File file = new File(inputFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean isNotWait = true;
        	StringBuilder sb = new StringBuilder();
        	
            while ((tempString = reader.readLine()) != null) {
            	
            	if(tempString.length() >= 1){
	            	if(tempString.charAt(0) == '>'){
	            		if(isNotWait){	// Process the first sequence 
	            			isNotWait = false;
		            		sb.append(tempString + "\r\n");
	            		}
	            		else{
	            			// output the waiting sequence
	            			outputFasta(sb.toString(), outputPath + seqCount + ".fasta");
	            			System.out.println(" Write file " + outputPath + seqCount + ".fasta");
	            			
	            			// clear and go on
	            			sb = new StringBuilder();
	            			sb.append(tempString + "\r\n");
	            		}
	            		seqCount++;
	            	}
	            	else{
	            		sb.append(tempString + "\r\n");
	            	}
            	}
            }
            
            outputFasta(sb.toString(), outputPath + seqCount + ".fasta");
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		return seqCount;
	}
	
	// Divide a .fasta file containing a number of sequences in the previous count sequence into multiple.Fasta files, each file is a sequence 
	public static int startOriginalSplit(String inputFile, String outputPath, int count){
		int seqCount = 0;
		
		File file = new File(inputFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean isNotWait = true;
        	StringBuilder sb = new StringBuilder();
        	
            while ((tempString = reader.readLine()) != null) {
            	
            	if(tempString.length() >= 1){
	            	if(tempString.charAt(0) == '>'){
	            		if(isNotWait){	// Process the first sequence 
	            			isNotWait = false;
		            		sb.append(tempString + "\r\n");
	            		}
	            		else{
	            			// Output the waiting sequence 
	            			outputFasta(sb.toString(), outputPath + seqCount + ".fasta");
	            			System.out.println("Write to the file " + outputPath + seqCount + ".fasta");
	            			if(seqCount == count) break;	// Stop when meeting the requirements of the total number
	            			
	            			// clear and go on
	            			sb = new StringBuilder();
	            			sb.append(tempString + "\r\n");
	            		}
	            		seqCount++;
	            	}
	            	else{
	            		sb.append(tempString + "\r\n");
	            	}
            	}
            }
            
            outputFasta(sb.toString(), outputPath + seqCount + ".fasta");
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		return seqCount;
	}
	
	// Divide The sequence of the specified starting position into a number of.Fasta files, each file is a sequence
	public static int startRandomSplit(String inputFile, String outputPath, int fromIndex, int toIndex){
		int seqCount = 0;
		List<Integer> list = new ArrayList<Integer>();
		
		for(int i = fromIndex; i <= toIndex; i++){
			list.add(i);
		}
		Collections.shuffle(list);
		
		File file = new File(inputFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean isNotWait = true;
            boolean isStart = false;
        	StringBuilder sb = new StringBuilder();
        	
            while ((tempString = reader.readLine()) != null) {
            	
            	if(tempString.length() >= 1){
	            	if(tempString.charAt(0) == '>'){
	            		if(isNotWait){	// Process the first sequence 
	            			isNotWait = false;
		            		sb.append(tempString + "\r\n");
	            		}
	            		else{
	            			if(seqCount == fromIndex || isStart == true){
	            				isStart = true;

		            			// Output the waiting sequence 
		            			outputFasta(sb.toString(), outputPath + list.get(seqCount - fromIndex) + ".fasta");
		            			System.out.println("Write to the file " + outputPath + list.get(seqCount - fromIndex) + ".fasta");	
		            			if(seqCount == toIndex) break;	// Stop when meeting the requirements of the total number
	            			}
	            		}	            			
            			// clear and go on
            			sb = new StringBuilder();
            			sb.append(tempString + "\r\n");
	            		seqCount++;
	            	}
	            	else{
	            		sb.append(tempString + "\r\n");
	            	}
            	}
            }
            
            outputFasta(sb.toString(), outputPath + list.get(seqCount - fromIndex) + ".fasta");
			System.out.println("Write to the file " + outputPath + list.get(seqCount - fromIndex) + ".fasta");
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		return seqCount;
	}
	
	// Divide A .Fasta file containing a number of sequences randomly into multiple.Fasta files, each file is a sequence 
	public static int startRandomSplit(String inputFile, String outputPath, int total){
		int seqCount = 0;
		List<Integer> list = new ArrayList<Integer>();
		
		for(int i = 1; i <= total; i++){
			list.add(i);
		}
		Collections.shuffle(list);
		
		File file = new File(inputFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean isNotWait = true;
        	StringBuilder sb = new StringBuilder();
        	
            while ((tempString = reader.readLine()) != null) {
            	
            	if(tempString.length() >= 1){
	            	if(tempString.charAt(0) == '>'){
	            		if(isNotWait){	// Process the first sequence 
	            			isNotWait = false;
		            		sb.append(tempString + "\r\n");
	            		}
	            		else{
	            			// Output the waiting sequence 
	            			outputFasta(sb.toString(), outputPath + list.get(seqCount - 1) + ".fasta");
	            			System.out.println("Write to the file " + outputPath + list.get(seqCount - 1) + ".fasta");
	            			
	            			if(seqCount == total) break;	// Stop when meeting the requirements of the total number
	            			
	            			// clear and go on
	            			sb = new StringBuilder();
	            			sb.append(tempString + "\r\n");
	            		}
	            		seqCount++;
	            	}
	            	else{
	            		sb.append(tempString + "\r\n");
	            	}
            	}
            }
            
            outputFasta(sb.toString(), outputPath + list.get(seqCount - 1) + ".fasta");
			System.out.println("Write to the file " + outputPath + list.get(seqCount - 1) + ".fasta");
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		return seqCount;
	}
	
	// Select the top count sequences from a sequence containing multiple .Fasta files and out to a file 
	public static void select2OutputFasta(String inputFile, int count, String outputFile){
		
		int seqCount = 0;
		
		File file = new File(inputFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean isNotWait = true;
        	StringBuilder sb = new StringBuilder();
        	
            while ((tempString = reader.readLine()) != null) {
            	
            	if(tempString.length() >= 1){
	            	if(tempString.charAt(0) == '>'){
	            		if(isNotWait){	// Process the first sequence 
	            			isNotWait = false;
		            		sb.append(tempString + "\r\n");
	            		}
	            		else{
	            			if(seqCount == count){
		            			// Output waiting sequence 
		            			outputFasta(sb.toString(), outputFile);
		            			break;
	            			}
	            			else{
	            				sb.append(tempString + "\r\n");
	            			}
	            		}
	            		seqCount++;
	            	}
	            	else{
	            		sb.append(tempString + "\r\n");
	            	}
            	}
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	public static void outputFasta(String content, String fileName){
		try {
			File outfile = new File(fileName);			
			// if file doesnt exists, then create it
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			
			FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

//		int count = 2316;
		String filePathName = "F:\\MPP_DNA\\Data\\Sequence3\\all.fasta";
//		String outputPathFile = "F:\\MPP_DNA\\Data\\Sequence3\\Multi\\" + count + ".fasta";
		String outputPath = "F:\\MPP_DNA\\Data\\Sequence3\\Single\\";
		
		int count = startOriginalSplit(filePathName, outputPath);
		System.out.println(" Sequential segmentation process completely, total count: " + count);
		
//		startRandomSplit(filePathName, outputPath, count);
//		System.out.println(" Random segmentation process completely, total count: " + count);
		
//		select2OutputFasta(filePathName, count, outputPathFile);
//		System.out.println(" Sequence extraction process completely, total count: " + count);
		
		System.exit(0);
	}
}
