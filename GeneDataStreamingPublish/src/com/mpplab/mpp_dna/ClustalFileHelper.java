package com.mpplab.mpp_dna;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Instruction: clustal file helper
 * 				Get the SNVRs
 * 
 * */
public class ClustalFileHelper {
	
	public String filePath;		// File path
	public int sqNum;			// Sequence number
	
	public ClustalFileHelper(String filePath, int sqNum)
	{
		if(!new File(filePath).exists()) {
			System.out.println(" File does not exist, read failed! ");
			System.exit(0);
		}
		this.filePath = filePath;
		this.sqNum = sqNum;
	}
	
	// Complete output the sequence alignment results to SeqTable
	public SeqTable getSeqResult(){

		SeqTable sTable = new SeqTable(this.sqNum);
		
		try {
			// read file content from file
			FileReader reader = new FileReader(this.filePath);
			BufferedReader br = new BufferedReader(reader);          
			
			String strline = null;          
			
			while((strline = br.readLine()) != null) {
				String fword = "";
				if(strline.length() > 0) fword = strline.substring(0, 1);
				switch(fword){
				case "g":
					SeqTable tempTable = new SeqTable(this.sqNum);	// Temporary read a set of all sequences 
					
					int sIndex = strline.lastIndexOf(" ") + 1;	// Get started index
					
					for(int i = 0; i < this.sqNum; i++){
						// Insert a row sequence 
						tempTable.addArray(i, strline.substring(sIndex).toCharArray());
						// Read the next line of text 
						strline = br.readLine();
					}
					for(int ti = 0; ti < tempTable.size(); ti++){
						sTable.addArray(ti, tempTable.get(ti));
					}
					break;
				case "C":
					break;
				default:
					break;
				}
			}
			br.close();
			reader.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return sTable;
	}
	
	public SeqTable getSVNRs()
	{
		SeqTable sTable = new SeqTable(this.sqNum);
		
		try {
			// read file content from file
			FileReader reader = new FileReader(this.filePath);
			BufferedReader br = new BufferedReader(reader);          
			
			String strline = null;          
			
			while((strline = br.readLine()) != null) {
				String fword = "";
				if(strline.length() > 0) fword = strline.substring(0, 1);
				switch(fword)
				{
				case "g":
					SeqTable tempTable = new SeqTable(this.sqNum);	// Temporary read a set of all sequences 
					
					int sIndex = strline.lastIndexOf(" ") + 1;	// Get started index
					
					for(int i = 0; i < this.sqNum; i++)
					{
						// Insert a row sequence 
						tempTable.addArray(i, strline.substring(sIndex).toCharArray());
						// Read the next line of text 
						strline = br.readLine();
					}
					// Intercept the * sequence 
					strline = strline.substring(sIndex);
					if(strline.indexOf(' ') != -1)
					{
						char[] chrLine = strline.toCharArray();
						for(int si = 0; si < chrLine.length; si++)
						{
							if(chrLine[si] != '*')
							{
								for(int ti = 0; ti < tempTable.size(); ti++)
								{
									sTable.add(ti, tempTable.getChar(si, ti));
								}
							}
						}
					}
					break;
				case "C":
					break;
				default:
					break;
				}
			}
			br.close();
			reader.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return sTable;
	}
}
