package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

public class AL3_Hybrid {

	public static void main(String[] args) {
		
		AlHelper ah = new AlHelper();
		
		int TotalSeqCount = public_data.SqNum;	// Total length of data set 
		int SIndex = 1;		// Sequence number for current processing(File name)
				
		List<Integer> OutGroupList = null;						// Published sequence index set
		List<int[]> OutGroup = null;							// Published sequence group set
		
		int[] outArgIndex = public_data.OutArgIndex;
		int outTotalCount = outArgIndex.length;
		int outCount = 0;
		List<Double> outDisList = new ArrayList<Double>();
		
		// First, a static data processing group is carried out, and the average distance is obtained. 
		int staticSeqCount = public_data.StaticCount;
		System.out.println("Static data processing£¬total number: " + staticSeqCount);
		GroupResult gResult = ah.staticProcessAndGetGroupResult(staticSeqCount);
		OutGroup = gResult.GroupList;
		OutGroupList = gResult.EleList;
		
		SIndex += staticSeqCount;
		
		System.out.println("= Start incremental data sequence processing ===============");
		
		while(SIndex <= TotalSeqCount){
			
			System.out.println("> Current sequence index: " + SIndex);
			System.out.println("- Compare between the sequence and the published sequence set, get the distance list");
			
			// Compare between the sequence and the published sequence set, get the distance list
			DisList disList = ah.runClustalAndGetDisList(SIndex, OutGroupList);

			System.out.print("- Search for the closest sequence in the output list to sequence " + SIndex +" : ");
			
			// Search for the closest sequence to sequence SIndex in the published set
			int nearestIndex = ah.findNearestSeqFromList(OutGroupList, disList);
			
			System.out.print(nearestIndex);
			
			// Get the group of this sequence 
			int[] group = ah.getGroupFromOutList(nearestIndex, OutGroup);
			
			System.out.print(" The group of this sequence: " + ah.getGroupStr(group) + "\n");
			
			// If the length of the group is 2, insert and update
			if(group.length == 2){
				System.out.println("> Remove the group: " + ah.getGroupStr(group) + "");
				gResult.RemoveOneResult(group);

				int[] insert = new int[]{group[0], group[1], SIndex};
				double dis = ah.getThreeTupleDis(insert); 
				
				gResult.AddOneResult(insert, dis);
				
				System.out.println("- Insert the new group£º" + ah.getGroupStr(insert));
			}
			else if(group.length == 3){		// If the length of the group is 3, split and update

				// Find the minimum distance and the combination of the four sequences. 
				int[] newGroup = new int[]{group[0], group[1], group[2], SIndex};
				GroupResult pairGroup = ah.findMinDisFrom4SeqGroup(newGroup);
				int[] ngroup1 = pairGroup.GroupList.get(0);
				int[] ngroup2 = pairGroup.GroupList.get(1);
				
				System.out.println("> Remove the group: " + ah.getGroupStr(group));
				gResult.RemoveOneResult(group);
				
				gResult.AddOneResult(ngroup1, pairGroup.DisList.get(0));
				gResult.AddOneResult(ngroup2, pairGroup.DisList.get(1));
				
				System.out.println("- Insert the new group: " + ah.getGroupStr(ngroup1) + ", " + ah.getGroupStr(ngroup2));
			}

			System.out.print("  The current published group result: ");
			ah.OutPrintGroup(gResult);
			System.out.println("  The current published element list: " + String.valueOf(OutGroupList));
			
			if(outCount < outTotalCount){
				int num = OutGroupList.size() - staticSeqCount;
				if(num == outArgIndex[outCount]){
					System.out.print("\n> ==== The current output No." + outArgIndex[outCount] + " sequence, the published set: ");
					double dis = ah.OutPrintGroup(gResult);
					outDisList.add(dis);
					outCount++;
				}
			}
			
			SIndex++;	// Point to the next sequence 
		}
		
		System.out.print("\n> Data processing complete, the published set: ");
		ah.OutPrintGroup(gResult);

		System.out.println("  The average distance output set: " + String.valueOf(outDisList));
		new FileHelper().writeOutDisListToFile(outDisList, public_data.LogPfx + public_data.logName);
		
		System.exit(0);
	}
}
