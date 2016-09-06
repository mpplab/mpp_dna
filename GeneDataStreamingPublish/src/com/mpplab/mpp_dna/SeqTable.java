package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

/*
 * Introduction: Table for storing sequence contents 
 * 
 * */
public class SeqTable {

	public int sqNum;
	ArrayList<List<Character>> st = new ArrayList<List<Character>>();
	
	public SeqTable(int sqNum){
		this.sqNum = sqNum;
		for(int i = 0; i < sqNum; i++)
		{
			List<Character> clst = new ArrayList<Character>();
			st.add(clst);
		}
	}
	
	// Return sequence number 
	public int size()
	{
		return st.size();
	}
	
	// Add a base to the sequence index
	public void add(int index, char name)
	{
		List<Character> clst = st.get(index);
		clst.add(name);
		st.set(index, clst);
	}
	
	// Add a sequence of bases to the sequence index
	public void addArray(int index, char[] names)
	{
		List<Character> clst = st.get(index);
		for(int i = 0; i < names.length; i++)
		{
			clst.add(names[i]);
		}
		st.set(index, clst);
	}
	
	// Add a sequence of bases to the sequence index. 
	public void addArray(int index, List<Character> names)
	{
		List<Character> clst = st.get(index);
		for(int i = 0; i < names.size(); i++)
		{
			clst.add(names.get(i));
		}
		st.set(index, clst);
	}
	
	// Get the first index sequence 
	public List<Character> get(int index)
	{
		return st.get(index);
	}
	
	// Get the xIndex base sequence in yIndex 
	public char getChar(int xIndex, int yIndex)
	{
		return st.get(yIndex).get(xIndex);
	}
	
	// Calculate the distance between two sequences aIndex and bIndex 
	public int CalDis(int aIndex, int bIndex)
	{
		int dis = 0;
		List<Character> alst = st.get(aIndex);
		List<Character> blst = st.get(bIndex);
		
		for(int i = 0; i < alst.size(); i++)
		{
			dis += BDistance.CalDistance(alst.get(i), blst.get(i));
		}
		
		return dis;
	}
	
	// Output the sequence to the console
	public void outSqTable()
	{
		for(int i = 0; i < st.size(); i++)
		{
			List<Character> clst = st.get(i);
			for(int j = 0; j < clst.size(); j++)
			{
				System.out.print(clst.get(j));
//				if((j + 1) % 70 == 0) System.out.print("\r\n");
			}
			System.out.print("\r\n");
		}
	}
	
	// Get the generalization sequence of seq2 and seq1 
	// Input: seq1Pos: position of seq1 in the SeqTable
	// Output: list of sequence characters after generalization 
	public List<Character> getGenSeq(int seq1Pos, int seq2Pos){
		List<Character> seq1List = st.get(seq1Pos);
		List<Character> seq2List = st.get(seq2Pos);
		List<Character> genList = new ArrayList<Character>();
		
		int ndNum = seq1List.size();
		
		if(seq2List.size() != ndNum)
			return genList;
		else{
			for(int i = 0; i < ndNum; i++){
				char ch1 = seq1List.get(i);
				char ch2 = seq2List.get(i);
				if(ch1 == ch2){
					genList.add(ch1);
				}
				else{
					genList.add(GenLattice.CalDistance(ch1, ch2));
				}
			}
		}
		
		return genList;
	}
}
