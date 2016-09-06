package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

public class AL4_DNALA {

	public static void main(String[] args) {
		
		AlHelper ah = new AlHelper();
		
		int[] outArray = public_data.OutArgIndex;
		List<Double> outDisList = new ArrayList<Double>();

		for(int oi = 0; oi < outArray.length; oi++){
		
			int TotalSeqCount = public_data.SqNum;			// Total length of data set 
			if(TotalSeqCount > public_data.SqNum) break;
			
			GroupResult gResult = null;		// Group result
						
			// First, a static data processing group is carried out, and the average distance is obtained. 
			System.out.println("Static data processing£¬total number: " + TotalSeqCount);
			gResult = ah.staticProcessAndGetGroupResult(TotalSeqCount);					
			System.out.print("  The current published group result: ");
			ah.OutPrintGroup(gResult);
			System.out.println("  The current published element list: " + String.valueOf(gResult.EleList));
			double dis = ah.OutPrintGroup(gResult);
			outDisList.add(dis);			
			System.out.print("\n> Data processing complete, the published set: ");			
			ah.OutPrintGroup(gResult);
		}
		
		System.out.println("  The average distance output set: " + String.valueOf(outDisList));
		
		System.exit(0);
	}
}
