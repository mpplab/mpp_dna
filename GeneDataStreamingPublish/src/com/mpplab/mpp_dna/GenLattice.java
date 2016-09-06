package com.mpplab.mpp_dna;

import java.util.HashMap;

public class GenLattice {
	
	private static HashMap<String, Character> GenMap;
	
	static {
		GenMap = new HashMap<String, Character>();
		
		//1-1
		GenMap.put("AG", 'R'); GenMap.put("AT", 'W'); GenMap.put("AC", 'M');
		GenMap.put("GT", 'K'); GenMap.put("GC", 'S'); GenMap.put("TC", 'Y');
		
		//2-2
		GenMap.put("RW", 'D'); GenMap.put("RM", 'V'); GenMap.put("RK", 'D');
		GenMap.put("RS", 'V'); GenMap.put("RY", 'N'); GenMap.put("WM", 'H');
		GenMap.put("WK", 'D'); GenMap.put("WS", 'N'); GenMap.put("WY", 'H');
		GenMap.put("MK", 'N'); GenMap.put("MS", 'V'); GenMap.put("MY", 'H');
		GenMap.put("KS", 'B'); GenMap.put("KY", 'B'); GenMap.put("SY", 'B');
		
		//3-3
		GenMap.put("DV", 'N'); GenMap.put("DH", 'N'); GenMap.put("DB", 'N');
		GenMap.put("D-", 'N'); GenMap.put("VH", 'N'); GenMap.put("VB", 'N');
		GenMap.put("V-", 'N'); GenMap.put("HB", 'N'); GenMap.put("H-", 'N');
		GenMap.put("B-", 'N');
		
		//1-2
		GenMap.put("AR", 'R'); GenMap.put("AW", 'W'); GenMap.put("AM", 'M');
		GenMap.put("AK", 'D'); GenMap.put("AS", 'V'); GenMap.put("AY", 'H');
		GenMap.put("GR", 'R'); GenMap.put("GW", 'D'); GenMap.put("GM", 'H');
		GenMap.put("GK", 'K'); GenMap.put("GS", 'S'); GenMap.put("GY", 'B');
		GenMap.put("TR", 'D'); GenMap.put("TW", 'W'); GenMap.put("TM", 'H');
		GenMap.put("TK", 'K'); GenMap.put("TS", 'B'); GenMap.put("TY", 'Y');
		GenMap.put("CR", 'V'); GenMap.put("CW", 'H'); GenMap.put("CM", 'M');
		GenMap.put("CK", 'B'); GenMap.put("CS", 'S'); GenMap.put("CY", 'Y');
		
		//1-3
		GenMap.put("AD", 'D'); GenMap.put("AV", 'V'); GenMap.put("AH", 'H');
		GenMap.put("AB", 'N'); GenMap.put("GD", 'D'); GenMap.put("GV", 'V');
		GenMap.put("GH", 'N'); GenMap.put("GB", 'B'); GenMap.put("TD", 'D');
		GenMap.put("TV", 'N'); GenMap.put("TH", 'H'); GenMap.put("TB", 'B');
		GenMap.put("CD", 'N'); GenMap.put("CV", 'V'); GenMap.put("CH", 'H');
		GenMap.put("CB", 'B'); GenMap.put("A-", 'N'); GenMap.put("G-", 'N');
		GenMap.put("T-", 'N'); GenMap.put("C-", 'N');
		
		//1-4
		GenMap.put("AN", 'N'); GenMap.put("GN", 'N'); GenMap.put("TN", 'N');
		GenMap.put("CN", 'N');
		
		//2-3
		GenMap.put("RD", 'D'); GenMap.put("WD", 'D'); GenMap.put("MD", 'N');
		GenMap.put("KD", 'D'); GenMap.put("SD", 'N'); GenMap.put("YD", 'N');
		GenMap.put("RV", 'V'); GenMap.put("WV", 'N'); GenMap.put("MV", 'V');
		GenMap.put("KV", 'N'); GenMap.put("SV", 'V'); GenMap.put("YV", 'N');
		GenMap.put("RH", 'N'); GenMap.put("WH", 'H'); GenMap.put("MH", 'H');
		GenMap.put("KH", 'N'); GenMap.put("SH", 'N'); GenMap.put("YH", 'H');
		GenMap.put("RB", 'N'); GenMap.put("WB", 'N'); GenMap.put("MB", 'N');
		GenMap.put("KB", 'B'); GenMap.put("SB", 'B'); GenMap.put("YB", 'B');
		GenMap.put("R-", 'N'); GenMap.put("W-", 'N'); GenMap.put("M-", 'N');
		GenMap.put("K-", 'N'); GenMap.put("S-", 'N'); GenMap.put("Y-", 'N');
		
		//2-4
		GenMap.put("RN", 'N'); GenMap.put("WN", 'N'); GenMap.put("MN", 'N');
		GenMap.put("KN", 'N'); GenMap.put("SN", 'N'); GenMap.put("YN", 'N');
		
		//3-4
		GenMap.put("DN", 'N'); GenMap.put("VN", 'N'); GenMap.put("HN", 'N');
		GenMap.put("BN", 'N'); GenMap.put("-N", 'N');
	}
	
	// Calculate the generalization of base a and base B 
	public static char CalDistance(char A, char B)
	{
		String str = String.valueOf(A) + String.valueOf(B);
		String strC = String.valueOf(B) + String.valueOf(A);
		if (A == B)
			return A;
		else
		{
			if(GenMap.get(str)!=null)
				return GenMap.get(str);
			else if(GenMap.get(strC)!=null)
				return GenMap.get(strC);
			else
				return 'N';
		}
	}
}
