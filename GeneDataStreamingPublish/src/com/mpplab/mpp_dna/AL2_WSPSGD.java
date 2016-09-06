package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

public class AL2_WSPSGD {

	public static void main(String[] args) {

		AlHelper ah = new AlHelper();
		
		String StreamSetPath = public_data.GeneDataPathPfx;	// Data set file location 
		int TotalSeqCount = public_data.SqNum;					// Total length of data set 
		int SetWIndex = 1;								// Sequence number for current processing(File name)
		int delayCons = public_data.DelayCount;		// Delay constraint 
		int sprsHold = public_data.SprsHold;			// The suppression threshold 
		double ArgDistance = 0;							// Average distance 
		DisMatrix dm = null;
		int[] outArgIndex = public_data.OutArgIndex;
		int outTotalCount = outArgIndex.length;
		int outCount = 0;
		List<Double> outDisList = new ArrayList<Double>();
		
		List<Integer> SetWList = new ArrayList<Integer>();		// Data set waiting to release
		List<Integer> SetSList = new ArrayList<Integer>();		// Set of suppression tuple 
		List<Integer> OutGroupList = null;						// Published sequence index set
		List<int[]> OutGroup = null;							// Published sequence group set
		
		// First, a static data processing group is carried out, and the average distance is obtained. 
		int staticSeqCount = public_data.StaticCount;
		System.out.println("Static data processing£¬total number: " + staticSeqCount);
		GroupResult gResult = ah.staticProcessAndGetGroupResult(staticSeqCount);
		OutGroup = gResult.GroupList;
		ArgDistance = gResult.ArgDistance;
		OutGroupList = gResult.EleList;
		
		SetWIndex += staticSeqCount;
		
		System.out.println("= Start incremental data sequence processing ===============");
		
		while(SetWIndex <= TotalSeqCount){
			
			System.out.println("> Current sequence index: " + SetWIndex);

			// Insert the smallest serial number of the set into the SetWList 
			SetWList.add(SetWIndex);

			// Update the data matrix
			if(dm != null){
				System.out.println("Update the data matrix");
				dm = ah.updateDisMatrix(SetWList.size(), SetWList, StreamSetPath);
			}
			else
				dm = new DisMatrix(1);
			
			if(SetWList.size() < delayCons){
				
				// Search for the closest sequence to sequence SetWIndex in SetWList
				System.out.print("- Search for the closest sequence to sequence " + SetWIndex + " : ");
				int nearestPos = ah.findNearestSeqFromMatrix(SetWIndex, SetWList, dm);

				// Not found while == -1
				if(nearestPos != -1){
					int nearestIndex = SetWList.get(nearestPos);
					
					double dis = dm.getDis(SetWList.indexOf(SetWIndex), SetWList.indexOf(nearestIndex));
					System.out.print(nearestIndex + ", Distance(" + SetWIndex + "," + nearestIndex + ") = " + dis);
					if(dis < ArgDistance){
						System.out.print(" < Average Distance: " + ArgDistance + "\n");
						System.out.println("-  Insert a new group into the output list: (" + SetWIndex + "," + nearestIndex + ")");

						// Remove this group from SetWList to the output list
						SetWList.remove(SetWList.indexOf(SetWIndex));
						SetWList.remove(SetWList.indexOf(nearestIndex));
						
						gResult.AddOneResult(new int[]{SetWIndex, nearestIndex}, dis);
					}
					else{
						System.out.print(" > Average Distance" + ArgDistance + "\n");
						System.out.println("  No treatment");
					}
				}
				else
					System.out.print("Not found\n");
			}
			else{
				// Take out the smallest number in the set
				int minIndex = ah.findMinmunSeqFromList(SetWList);
				SetWList.remove(SetWList.indexOf(minIndex));

				System.out.println("> Waiting set: " + String.valueOf(SetWList) + " reaches the maximum delay constraint, take out the smallest number in the set: " + minIndex);
				
				if(SetSList.size() < sprsHold){
					System.out.println("  Insert the sequence " + minIndex + " into the set of suppression tuple");
					SetSList.add(minIndex);
					System.out.println("  The current set of suppression tuple: " + String.valueOf(SetSList));
				}
				else{
					System.out.println("  Waiting set size has reached a threshold value");
					System.out.println("- Compare between the sequence and the published sequence set, get the distance list");
					
					// Compare between the sequence and the published sequence set, get the distance list
					DisList disList = ah.runClustalAndGetDisList(minIndex, OutGroupList);

					System.out.print("- Search for the closest sequence in the output list to sequence " + minIndex +" : ");
					
					// Search for the closest sequence to sequence SetWIndex in the published set
					int nearestIndex = ah.findNearestSeqFromList(OutGroupList, disList);
					
					System.out.print(nearestIndex);
					
					// Get the group of this sequence 
					int[] group = ah.getGroupFromOutList(nearestIndex, OutGroup);
					
					System.out.print(" The group of this sequence: " + ah.getGroupStr(group) + "\n");
					
					// If the length of the group is 2, insert and update
					if(group.length == 2){
						System.out.println("> Remove the group: " + ah.getGroupStr(group) + "");
						gResult.RemoveOneResult(group);
						
						int[] insert = new int[]{group[0], group[1], minIndex};
						double dis = ah.getThreeTupleDis(insert); 
						
						gResult.AddOneResult(insert, dis);
						
						System.out.println("- Insert the new group£º" + ah.getGroupStr(insert));
					}
					else if(group.length == 3){		// If the length of the group is 3, split and update
						
						// Find the minimum distance and the combination of the four sequences. 
						int[] newGroup = new int[]{group[0], group[1], group[2], minIndex};
						GroupResult pairGroup = ah.findMinDisFrom4SeqGroup(newGroup);
						int[] ngroup1 = pairGroup.GroupList.get(0);
						int[] ngroup2 = pairGroup.GroupList.get(1);
						
						System.out.println("> Remove the group:" + ah.getGroupStr(group));
						gResult.RemoveOneResult(group);
						
						gResult.AddOneResult(ngroup1, pairGroup.DisList.get(0));
						gResult.AddOneResult(ngroup2, pairGroup.DisList.get(1));
						
						System.out.println("- Insert the new group: " + ah.getGroupStr(ngroup1) + ", " + ah.getGroupStr(ngroup2));
					}
				}				
			}
			System.out.print("  The current published group result: ");
			ah.OutPrintGroup(gResult);
			System.out.println("  The current published element list: " + String.valueOf(OutGroupList));
			System.out.println("  The current waiting sequence list: " + String.valueOf(SetWList));
			System.out.println("  The current set of supression tuple: " + String.valueOf(SetSList));
			
			if(outCount < outTotalCount){
				int num = OutGroupList.size() - staticSeqCount;
				if(num == outArgIndex[outCount]){
					System.out.print("\n> ==== The current output No." + outArgIndex[outCount] + " sequence, the published set: ");
					System.out.println("  The current waiting sequence list: " + String.valueOf(SetWList));
					double dis = ah.OutPrintGroup(gResult);
					outDisList.add(dis);
					outCount++;
				}
			}
			
			SetWIndex++;	// Point to the next sequence 
		}
		System.out.print("\n> Data processing complete, the published set: ");
		System.out.println("  The waiting sequence list: " + String.valueOf(SetWList));
		System.out.println("  The set of supression tuple: " + String.valueOf(SetSList));
		ah.OutPrintGroup(gResult);

		System.out.println("  The average distance output set: " + String.valueOf(outDisList));
		new FileHelper().writeOutDisListToFile(outDisList, public_data.LogPfx + public_data.logName);
		
		System.exit(0);
	}
}
