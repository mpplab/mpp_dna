package com.mpplab.mpp_dna;

import java.util.HashMap;

// Static records of the distance between the two bases 
public class BDistance {
	
	public static HashMap<String, Integer> NodesDis;
	
	static{
		NodesDis = new HashMap<String, Integer>();
		
		//1-1
		NodesDis.put("AG", 2); NodesDis.put("AT", 2); NodesDis.put("AC", 2);
		NodesDis.put("GT", 2); NodesDis.put("GC", 2); NodesDis.put("TC", 2);
		
		//2-2
		NodesDis.put("RW", 2); NodesDis.put("RM", 2); NodesDis.put("RK", 2);
		NodesDis.put("RS", 2); NodesDis.put("RY", 2); NodesDis.put("WM", 2);
		NodesDis.put("WK", 2); NodesDis.put("WS", 2); NodesDis.put("WY", 2);
		NodesDis.put("MK", 2); NodesDis.put("MS", 2); NodesDis.put("MY", 2);
		NodesDis.put("KS", 2); NodesDis.put("KY", 2); NodesDis.put("SY", 2);
		
		//3-3
		NodesDis.put("DV", 2); NodesDis.put("DH", 2); NodesDis.put("DB", 2);
		NodesDis.put("D-", 2); NodesDis.put("VH", 2); NodesDis.put("VB", 2);
		NodesDis.put("V-", 2); NodesDis.put("HB", 2); NodesDis.put("H-", 2);
		NodesDis.put("B-", 2);
		
		//1-2
		NodesDis.put("AR", 1); NodesDis.put("AW", 1); NodesDis.put("AM", 1);
		NodesDis.put("AK", 3); NodesDis.put("AS", 3); NodesDis.put("AY", 3);
		NodesDis.put("GR", 1); NodesDis.put("GW", 3); NodesDis.put("GM", 3);
		NodesDis.put("GK", 1); NodesDis.put("GS", 1); NodesDis.put("GY", 3);
		NodesDis.put("TR", 3); NodesDis.put("TW", 1); NodesDis.put("TM", 3);
		NodesDis.put("TK", 1); NodesDis.put("TS", 3); NodesDis.put("TY", 1);
		NodesDis.put("CR", 3); NodesDis.put("CW", 3); NodesDis.put("CM", 1);
		NodesDis.put("CK", 3); NodesDis.put("CS", 1); NodesDis.put("CY", 1);
		
		//1-3
		NodesDis.put("AD", 2); NodesDis.put("AV", 2); NodesDis.put("AH", 2);
		NodesDis.put("AB", 4); NodesDis.put("GD", 2); NodesDis.put("GV", 2);
		NodesDis.put("GH", 4); NodesDis.put("GB", 2); NodesDis.put("TD", 2);
		NodesDis.put("TV", 4); NodesDis.put("TH", 2); NodesDis.put("TB", 2);
		NodesDis.put("CD", 4); NodesDis.put("CV", 2); NodesDis.put("CH", 2);
		NodesDis.put("CB", 2); NodesDis.put("A-", 4); NodesDis.put("G-", 4);
		NodesDis.put("T-", 4); NodesDis.put("C-", 4);
		
		//1-4
		NodesDis.put("AN", 3); NodesDis.put("GN", 3); NodesDis.put("TN", 3);
		NodesDis.put("CN", 3);
		
		//2-3
		NodesDis.put("RD", 1); NodesDis.put("WD", 1); NodesDis.put("MD", 3);
		NodesDis.put("KD", 1); NodesDis.put("SD", 3); NodesDis.put("YD", 3);
		NodesDis.put("RV", 1); NodesDis.put("WV", 3); NodesDis.put("MV", 1);
		NodesDis.put("KV", 3); NodesDis.put("SV", 1); NodesDis.put("YV", 3);
		NodesDis.put("RH", 3); NodesDis.put("WH", 1); NodesDis.put("MH", 1);
		NodesDis.put("KH", 3); NodesDis.put("SH", 3); NodesDis.put("YH", 1);
		NodesDis.put("RB", 3); NodesDis.put("WB", 3); NodesDis.put("MB", 3);
		NodesDis.put("KB", 1); NodesDis.put("SB", 1); NodesDis.put("YB", 1);
		NodesDis.put("R-", 3); NodesDis.put("W-", 3); NodesDis.put("M-", 3);
		NodesDis.put("K-", 3); NodesDis.put("S-", 3); NodesDis.put("Y-", 3);
		
		//2-4
		NodesDis.put("RN", 2); NodesDis.put("WN", 2); NodesDis.put("MN", 2);
		NodesDis.put("KN", 2); NodesDis.put("SN", 2); NodesDis.put("YN", 2);
		
		//3-4
		NodesDis.put("DN", 1); NodesDis.put("VN", 1); NodesDis.put("HN", 1);
		NodesDis.put("BN", 2); NodesDis.put("-N", 1);
	}
		
	// Calculate the distance between the base a and the base B 
	public static int CalDistance(char A, char B)
	{
		String str = String.valueOf(A) + String.valueOf(B);
		String strC = String.valueOf(B) + String.valueOf(A);
		if (A == B)
			return 0;
		else
		{
			if(NodesDis.get(str)!=null)
				return NodesDis.get(str);
			else if(NodesDis.get(strC)!=null)
				return NodesDis.get(strC);
			else
				return public_data.MAXDIS;
		}
	}
}
