package com.mpplab.mpp_dna;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

	// Read the distance from file
	public List<Float> readDisFromFile(String fileName){
		List<Float> result = new ArrayList<Float>();
		
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                float temp = Float.parseFloat(tempString);
                result.add(temp);
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
		
		return result;
	}
	
	// Read the result of MWMatching
	public List<int[]> readMWMResultFromFile(String fileName){
		List<int[]> result = new ArrayList<int[]>(); 
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
                int temp = Integer.parseInt(tempString);
                int[] cur = new int[2];
                cur[0] = line; cur[1] = temp;
                int[] cur2 = new int[2];
                cur2[0] = temp; cur2[1] = line;
                
                // Remove the repeat and useless(-1) group
                if( temp != -1 ) {
                	if(!hasContains(result, cur))
                	{
                		if(!hasContains(result, cur2)){
                			result.add(cur);
                		}
                	}                	
                }
                line++;
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
        
        return result;        
	}
		
	// Check whether a is in list
	public boolean hasContains(List<int[]> list, int[] a){
		boolean check = false;
		
		for(int i = 0; i < list.size(); i++){
			if(list.get(i)[0] == a[0] && list.get(i)[1] == a[1]){   
		        check = true;
		        break;
		    }
		}
		
		return check;
	}
	
	// Write DisMatrix to file
	public void writeDisMatrixToFile(DisMatrix dm, String fileName){
		try {
			StringBuilder content = new StringBuilder();
			int sqNum = dm.sqNum;
			
			for(int i = 0; i < sqNum; i++){
				for(int j = i; j < sqNum; j++){
					double dis = dm.getDis(i, j);
					content.append(dis + "\r\n");
				}
			}
			
			File file = new File(fileName);			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Read distance matrix(DisMatrix) from file
	public DisMatrix readDisMatrixFromFile(int sqNum, String fileName){
		
		DisMatrix dm = new DisMatrix(sqNum);	
		File file = new File(fileName);
        BufferedReader reader = null;
        List<Float> result = new ArrayList<Float>();
        int count = 0;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                float temp = Float.parseFloat(tempString);
                result.add(temp);
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
        
        for(int i = 0; i < sqNum; i++){
        	for(int j = i; j < sqNum; j++){
        		dm.setDis(i, j, result.get(count++));
        	}
        }
        
        return dm;
	}
	
	// Write the distance matrix(List<double[]>) to the file
	public void writeMatrixToFile(List<double[]> cMatrix, String filePath){
		try {
			StringBuilder content = new StringBuilder();
			
			for(int i = 0; i < cMatrix.size(); i++){
				double[] cur = cMatrix.get(i);
				content.append("(" + String.format("%d", (int)cur[0]) + ","+ String.format("%d", (int)cur[1]) + "," + cur[2] + ")" + "\r\n");
			}
			
			File file = new File(filePath);			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Introduction: Read List<Character> from .fasta file
	 * */
	public List<Character> getArrayFromFasta(String fileName){
		
		List<Character> ret = new ArrayList<Character>();
		
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            
            while ((tempString = reader.readLine()) != null) {
            	
            	if(tempString.charAt(0) == '>'){
            		continue;	// skip the first line
            	}
            	char[] chrArray = tempString.toCharArray();
            	if(chrArray[0] == '>') break;	// skip the last line
            	
            	int index = 0;
        		
        		// read every 70 seqs from the line
            	while(index < chrArray.length && index < 70){
            		char cur = chrArray[index++];
            		ret.add(cur);
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
		
		return ret;
	}
	
	/*
	 * Write the List<Character> seq to .fasta file
	 * */
	public void writeListToFasta(List<Character> seqList, String fileName){
		
		try {
			StringBuilder content = new StringBuilder(); 
			content.append(">gi\r\n");
			
			for(int i = 1; i <= seqList.size(); i++){
				char cur = seqList.get(i - 1);
				if(i % 70 == 0){
					content.append(cur + "\r\n");
				}
				else
				{
					content.append(cur);
				}
			}
			
			File file = new File(fileName);			
			// if file dosen't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Introduction: write two seqs(Index) to the appointed file
	 * */
	public void write2SeqToFastaByIndex(int seq1Index, int seq2Index, String filePathName){
		
		String path = public_data.GeneDataPathPfx;
		String outputFilePathName = filePathName;
		
		// Read the two sequence to List<Character>
		List<Character> seq1List = getArrayFromFasta(path + seq1Index + ".fasta");
		List<Character> seq2List = getArrayFromFasta(path + seq2Index + ".fasta");
		
		// Write to the file
		List<List<Character>> seqListList = new ArrayList<List<Character>>();
		seqListList.add(seq1List);
		seqListList.add(seq2List);
		writeMultiListToFasta(seqListList, outputFilePathName);
	}
	
	/*
	 * Introduction: write two seqs(File name) to the appointed file
	 * */
	public void write2SeqToFastaByFile(String seq1File, String seq2File, String filePathName){
		
		String path = public_data.GeneDataPathPfx;
		String outputFilePathName = filePathName;
		
		// Read the two sequence to List<Character>
		List<Character> seq1List = getArrayFromFasta(path + seq1File);
		List<Character> seq2List = getArrayFromFasta(path + seq2File);
		
		// Write to the file
		List<List<Character>> seqListList = new ArrayList<List<Character>>();
		seqListList.add(seq1List);
		seqListList.add(seq2List);
		writeMultiListToFasta(seqListList, outputFilePathName);
	}
	
	/*
	 * Introduction: Write multiple sequences in the list to .fasta file
	 * */
	public void writeMultiListToFasta(List<List<Character>> seqListList, String fileName){
		
		try {
			StringBuilder content = new StringBuilder(); 
			for(int li = 0; li < seqListList.size(); li++){
				List<Character> seqList = seqListList.get(li);
				content.append(">gi" + li + "\r\n");
				
				for(int i = 1; i <= seqList.size(); i++){
					char cur = seqList.get(i - 1);
					if(i % 70 == 0){
						content.append(cur + "\r\n");
					}
					else
					{
						content.append(cur);
					}
				}
				content.append("\r\n");
			}

			File file = new File(fileName);			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Introduction: Write multiple sequences in the list to .fasta file
	 * */
	public void writeMultiSeqToFastaByIndexList(List<Integer> seqIndexList, String fileName){
		
		List<List<Character>> seqListList = new ArrayList<List<Character>>();
		String path = public_data.GeneDataPathPfx;

		for(int i = 0; i < seqIndexList.size(); i++){
			List<Character> seqList = getArrayFromFasta(path + seqIndexList.get(i) + ".fasta");
			seqListList.add(seqList);
		}
		
		writeMultiListToFasta(seqListList, fileName);
	}
	
	//将结果输出到文件中
	public void writeOutDisListToFile(List<Double> list, String fileName){
		try {
			StringBuilder content = new StringBuilder();
			
			content.append("Algorithm Name: " + public_data.AlName + "\r\n"
					+ "Data Source: "+public_data.DataSource + "\r\n"
					+ "Sequence Number: " + public_data.SqNum + "\r\n"
					+ "Static Handling Count: " + public_data.StaticCount + "\r\n"
					+ "Delay constraint: " + public_data.DelayCount + "\r\n"
					+ "Suppression Threshold: " + public_data.SprsHold + "\r\n"
					+ "Output average distance list:" + "\r\n");
			
			for(int i = 0; i < list.size(); i++){
				double dis = list.get(i);
				content.append(dis + "\r\n");
			}
			
			File file = new File(fileName);			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
