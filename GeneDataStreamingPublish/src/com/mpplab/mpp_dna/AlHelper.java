package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

public class AlHelper {

	// Carry out a static data processing group and obtain the average distance
	// Input:  The required processing of the first seqCount (From 1 to seqCount)
	// Output: group result
	public GroupResult staticProcessAndGetGroupResult(int seqCount){
		
		int sqNum = seqCount;
		
		//--------------------1 Run Clustal and get the distance matrix--------------------
		System.out.println("> Step1 Sequence alignment, get the distance matrix");
		
		DisMatrix dm = runClustalAndGetMatrix(sqNum);
		// Output the distance matrix table
		dm.outDisTable();
		
		//---------------------------------2 Clustering------------------------------------			
		System.out.println("> Step2 Clustering");
		
		GroupHelper gh = new GroupHelper();
		List<int[]> groupList = gh.getMWMGroupByMatrix(dm);
		double avrDis = gh.getArgDisByGroupAndMatrix(groupList, dm);
		
		List<Integer> eleList = new ArrayList<Integer>();	// Convert to sequence number (File name)
		List<Double> disList = new ArrayList<Double>();		// Output to distance list 
		for(int i = 0; i < groupList.size(); i++){
			int[] crGroup = groupList.get(i);
			disList.add(dm.getDis(crGroup[0], crGroup[1]));
			for(int j = 0; j < crGroup.length; j++){
				crGroup[j]++;	// Convert to sequence number (File name)
				eleList.add(crGroup[j]);
			}
			groupList.set(i, crGroup);
		}
		
		GroupResult gResult = new GroupResult();
		gResult.ArgDistance = avrDis;
		gResult.DisList = disList;
		gResult.GroupList = groupList;
		gResult.EleList = eleList;
		
		return gResult;
	}
	
	// Run Clustal sequence alignment and return the distance matrix
	private DisMatrix runClustalAndGetMatrix(int sqNum){
	
		DisMatrix dm = new DisMatrix(sqNum);
		
		for(int i = 0; i < sqNum; i++)
		{
			for(int j = i; j < sqNum; j++)
			{
				if(i == j) dm.setDis(i, j, public_data.MAXDIS);
				else{
					int dis = ClustalGetDis(i+1, j+1);
					dm.setDis(i, j, dis);
				}
			}
		}
		return dm;
	}
	
	// From 4 records to find out the smallest combination of distance
	public GroupResult findMinDisFrom4SeqGroup(int[] group){
		
		GroupResult gResult = new GroupResult();
		int[][] pairGroup = new int[2][2];
		double minDis1 = 0, minDis2 = 0;
		double minValue = 0;
		boolean isFirst = true;
		
		for(int gi = 0; gi < 3; gi++){
			for(int gj = gi + 1; gj < 4; gj++){
				double dis1 = ClustalGetDis(group[gi], group[gj]);
				int[] other = other2SeqExcept2FromGroup(group, group[gi], group[gj]);
				double dis2 = ClustalGetDis(other[0], other[1]);
				double dis = dis1 + dis2;
				if(isFirst){
					isFirst = false;
					pairGroup[0][0] = group[gi]; pairGroup[0][1] = group[gj];
					pairGroup[1][0] = other[0]; pairGroup[1][1] = other[1];
					minValue = dis;
					minDis1 = dis1;
					minDis2 = dis2;
				}
				else{
					if(dis < minValue){
						pairGroup[0][0] = group[gi]; pairGroup[0][1] = group[gj];
						pairGroup[1][0] = other[0]; pairGroup[1][1] = other[1];
						minValue = dis;
						minDis1 = dis1;
						minDis2 = dis2;
					}
				}
			}
		}
		gResult.GroupList.add(pairGroup[0]);
		gResult.DisList.add(minDis1);
		gResult.GroupList.add(pairGroup[1]);
		gResult.DisList.add(minDis2);
		for(int i = 0; i < group.length; i++){
			gResult.EleList.add(group[i]);
		}
		
		return gResult;
	}
	
	// Clustal sequence alignment and return the distance
	public int ClustalGetDis(int seq1Index, int seq2Index){
		
		int sqNum = 2;
		
		String inputFile = public_data.MultiInputPathPfx + "tempMutli.fasta";
		String outputFile = public_data.OutputFile;
		
		FileHelper fh = new FileHelper();
		fh.write2SeqToFastaByIndex(seq1Index, seq2Index, inputFile);
				
		// 1.1 Call clustal, and write the result to file
		System.out.print(">  Sequence alignment: Sequence " + seq1Index + " Sequence " + seq2Index);
		Clustalw ct = new Clustalw(inputFile, outputFile);
		ct.start();
		
		// 1.2 Read the file by line to get snvrs
		ClustalFileHelper cf = new ClustalFileHelper(outputFile, sqNum);
		SeqTable sqTable = cf.getSVNRs();
		
		// Output snvrs
		if(public_data.Debug) sqTable.outSqTable();
		
		int dis = sqTable.CalDis(0, 1);
		System.out.print(" : " + dis + "\n");
		
		return dis;
	}
	
	// Clustal sequence alignment and return the distance
	public int ClustalGetDis(String seq1File, String seq2File){

		int sqNum = 2;
		
		String inputFile = public_data.MultiInputPathPfx + "tempMutli.fasta";
		String outputFile = public_data.OutputFile;
		
		FileHelper fh = new FileHelper();
		fh.write2SeqToFastaByFile(seq1File, seq2File, inputFile);
				
		// 1.1 Call clustal, and write the result to file
		System.out.print("Sequence alignment " + seq1File + " " + seq2File);
		Clustalw ct = new Clustalw(inputFile, outputFile);
		ct.start();
		
		// 1.2 Read the file by line to get snvrs
		ClustalFileHelper cf = new ClustalFileHelper(outputFile, sqNum);
		SeqTable sqTable = cf.getSVNRs();
		
		// Output snvrs
		if(public_data.Debug) sqTable.outSqTable();

		int dis = sqTable.CalDis(0, 1);
		System.out.print(" : " + dis + "\n");
		
		return dis;
	}
	
	// Clustal sequence alignment and return the SNP
	public SeqTable ClustalGetSNP(int seq1Index, int seq2Index){
		
		int sqNum = 2;
		
		String inputFile = public_data.MultiInputPathPfx + "tempPW.fasta";
		String outputFile = public_data.OutputFile;
		
		FileHelper fh = new FileHelper();
		fh.write2SeqToFastaByIndex(seq1Index, seq2Index, inputFile);
				
		// 1.1 Call clustal, and write the result to file
		System.out.println("Sequence alignment and get the SNP " + seq1Index + " " + seq2Index);
		Clustalw ct = new Clustalw(inputFile, outputFile);
		ct.start();
		
		// 1.2 Read the file by line to get snvrs
		ClustalFileHelper cf = new ClustalFileHelper(outputFile, sqNum);
		SeqTable sqTable = cf.getSVNRs();
		
		// Output snvrs
		if(public_data.Debug) sqTable.outSqTable();
		
		return sqTable;
	}
	
	// Clustal sequence alignment and return the generalization result
	public List<Character> ClustalGetGen(int seq1Index, int seq2Index){
		
		int sqNum = 2;
		
		String inputFile = public_data.MultiInputPathPfx + "tempPW.fasta";
		String outputFile = public_data.OutputFile;
		
		FileHelper fh = new FileHelper();
		fh.write2SeqToFastaByIndex(seq1Index, seq2Index, inputFile);
				
		// 1.1 Call clustal, and write the result to file
		System.out.println("Clustal sequence alignment and get the generalization result: Sequence " + seq1Index + " Sequence "+ seq2Index);
		Clustalw ct = new Clustalw(inputFile, outputFile);
		ct.start();
		
		// 1.2 Read the file by line to get the generalization result
//		System.out.println("> Step1.2 Get the generalization result");
		ClustalFileHelper cf = new ClustalFileHelper(outputFile, sqNum);
		SeqTable sqTable = cf.getSeqResult();
		
		// Output the generalization result
		if(public_data.Debug) sqTable.outSqTable();
		
		return sqTable.getGenSeq(0, 1);
	}

	// Find out two other sequences in addition to the 2 sequences from the set of the 4 tuple. 
	public int[] other2SeqExcept2FromGroup(int[] group, int col1, int col2){
		int[] other = new int[2];
		int count = 0;
		for(int i = 0; i < 4; i++){
			int cur = group[i];
			if(cur != col1 && cur != col2){
				other[count++] = cur;
			}
		}
		
		return other;
	}
	
	// Run Clustal alignment and update the distance matrix
	public DisMatrix updateDisMatrix(int newSqNum, List<Integer> seqList, String StreamSetPath){
		
		DisMatrix newDm = null;
		
		if(newSqNum != 1){
			newDm = new DisMatrix(newSqNum);
			
			for(int i = 0; i < newSqNum; i++){
				for(int j = i; j < newSqNum; j++){

					if(i == j) {
						newDm.setDis(i, j, public_data.MAXDIS);
					}
					else{
						int dis = ClustalGetDis(seqList.get(i), seqList.get(j));
						newDm.setDis(i, j, dis);
					}
				}
			}
		}
		else{
			newDm = new DisMatrix(newSqNum);
		}		
		
		return newDm;
	}
		
	// Run Clustal and get the distance list
	// Input: index: the sequence file name need to be sampled, queryIndexList: the sequence file name list need to be queried
	// Output: return the distance list
	public DisList runClustalAndGetDisList(int index, List<Integer> queryIndexList){
		
		int sqNum = queryIndexList.size();
		DisList dl = new DisList(sqNum);
		
		for(int i = 0; i < queryIndexList.size(); i++){
			int dis = ClustalGetDis(index, queryIndexList.get(i));
			dl.setDis(i, dis);
		}
				
		return dl;
	}
	
	// Run Clustal and return the generalization result of two sequence
	public List<Character> ClustalGetGen(String seq1File, String seq2File){
		
		int sqNum = 2;
		
		String inputFile = public_data.MultiInputPathPfx + "tempMutli.fasta";
		String outputFile = public_data.OutputFile;
		
		FileHelper fh = new FileHelper();
		fh.write2SeqToFastaByFile(seq1File, seq2File, inputFile);
				
		// 1.1 Call clustal, output the result to the file
		System.out.print("Run the clustal and find generalization result " + seq1File + " " + seq2File);
		Clustalw ct = new Clustalw(inputFile, outputFile);
		ct.start();
		
		// 1.2 Read align file by line, and output the generalization result
		ClustalFileHelper cf = new ClustalFileHelper(outputFile, sqNum);
		SeqTable sqTable = cf.getSeqResult();
		
		// Output the result to console
		if(public_data.Debug) sqTable.outSqTable();
		
		return sqTable.getGenSeq(0, 1);
	}
	
	// Calculate the distance of triple
	// Input: tuple: group of the triple sequence index
	public double getThreeTupleDis(int[] tuple){
		double dis = 0;
		
		if(tuple.length != 3)
			return dis;
		else{
			List<Character> genNode = ClustalGetGen(tuple[0], tuple[1]);
//			// Output the sequence
//			for(int j = 0; j < genNode.size(); j++)
//			{
//				System.out.print(genNode.get(j));
//				if((j + 1) % 70 == 0) System.out.print("\r\n");
//			}
			System.out.print("\r\n");
			FileHelper fh = new FileHelper();
			String genFile = public_data.GeneDataPathPfx + "genTemp.fasta";
			fh.writeListToFasta(genNode, genFile);	// the generalization sequence of the first two sequence
			
			genNode = ClustalGetGen("genTemp.fasta", tuple[2] + ".fasta");
			fh.writeListToFasta(genNode, genFile);	// write to the file again
			
			for(int i = 0; i < 3; i++){
				dis += ClustalGetDis("genTemp.fasta", tuple[i] + ".fasta");
			}
		}
		
		return dis;
	}
	
	// Search and return the nearest sequence index in the list to the query sequence by the distance matrix
	// QueryIndex must be included in the list
	// Return: the position of the result sequence in the group
	public int findNearestSeqFromMatrix(int queryIndex, List<Integer> list, DisMatrix matrix){
		
		int minIndex = -1;
		double minValue = 0;
		boolean isFirst = true;
		
		// get the position of the query index in the list
		int qPos = list.indexOf(queryIndex);
		
		if(qPos != -1){
		
			// Search the minimum value in the matrix paired with qPos
			// Because of the distance matrix is the upper triangular form, we need to search by block
			for(int i = 0; i < qPos; i++){
				double dis = matrix.getDis(i, qPos);
				if(isFirst){
					minValue = dis;
					minIndex = i;
					isFirst = false;
				}
				else{
					if(dis < minValue){
						minValue = dis;
						minIndex = i;
					}
				}
			}
			for(int i = qPos + 1; i < matrix.sqNum; i++){
				double dis = matrix.getDis(qPos, i);
				if(dis < minValue){
					minValue = dis;
					minIndex = i;
				}
			}	
		}
		
		return minIndex;
	}
	
	// Search and return the nearest sequence index in the list to the query sequence
	// QueryIndex is not included in the list
	// Return: the minimum distance sequence index
	public int findNearestSeqFromList(List<Integer> list, DisList disList){
		
		int minIndex = -1;
		double minValue = 0;
		boolean isFirst = true;

		// get the position of the query index in the list
		int sqNum = list.size();
		
		for(int i = 0; i < sqNum; i++){
			double dis = disList.getDis(i);
			if(isFirst){
				minValue = dis;
				minIndex = i;
				isFirst = false;
			}
			else{
				if(dis < minValue){
					minValue = dis;
					minIndex = i;
				}
			}
		}
		
		return list.get(minIndex);
	}
	
	// Read a sequence which has a minimum ts from list
	public int findMinmunSeqFromList(List<Integer> list){
		int minIndex = 0;
		boolean isFirst = true;
		
		for(int i = 0; i < list.size(); i++){
			if(isFirst){
				minIndex = list.get(0);
				isFirst = false;
			}
			else if(list.get(i) < minIndex){
				minIndex = list.get(i);
			}
		}
		
		return minIndex;
	}

	// Get group the sequence is in
	public int[] getGroupFromOutList(int index, List<int[]> OutGroup){
		int[] group = null;
		
		for(int i = 0; i < OutGroup.size(); i++){
			int[] gcur = OutGroup.get(i);
			for(int j = 0; j < gcur.length; j++){
				if(gcur[j] == index){
					group = gcur;
					break;
				}
			}
		}
		
		return group;
	}
		
	// Output the group result to the console
	public double OutPrintGroup(GroupResult gResult){
		int threeCount = 0;
		List<int[]> groupList = gResult.GroupList;
		System.out.print("[ ");
		for(int i = 0; i < groupList.size(); i++){
			int[] cur = groupList.get(i);
			if(cur.length == 3) threeCount++;
			System.out.print("(");
			String curStr = "";
			for(int j = 0; j < cur.length; j++){
				curStr += cur[j] + ", ";
			}
			curStr = curStr.substring(0, curStr.length()-2);
			System.out.print(curStr + ") ");
		}
		System.out.print("]\n");
		
		double disSum = 0;
		
		System.out.print("  Group distance: [ ");
		for(int i = 0; i < gResult.DisList.size(); i++){
			System.out.print(gResult.DisList.get(i) + " ");
			disSum += gResult.DisList.get(i);
		}
		System.out.print("]");
		
		disSum = disSum / gResult.DisList.size();
		
		System.out.print(" Average distance = " + disSum);
		System.out.print(" The count of triple: " + threeCount + "\n");
		
		return disSum;
		
	} 
	
	//Output the group to the console
	public String getGroupStr(int[] group){
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(int i = 0; i < group.length; i++){
			sb.append(group[i]);
			if(i != group.length - 1) sb.append(", ");
		}
		sb.append(")");
		return sb.toString();		
	}
}
