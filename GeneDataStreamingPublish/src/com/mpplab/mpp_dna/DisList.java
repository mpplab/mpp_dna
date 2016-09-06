package com.mpplab.mpp_dna;

/*
 * Introduction: Construct and operate the distance list
 * 				A case of one to multiple sequence alignment
 * 
 * */
public class DisList {

	public int queryIndex;
	double[] dl;
	public int sqNum;	//	Sequence number
	
	// Constructed function
	public DisList(int sqNum){
		dl = new double[sqNum];
		this.sqNum = sqNum;
	}
	
	// Constructed function
	// Construct the distance matrix
	public DisList(int sqNum, SeqTable st){
		dl = new double[sqNum];
		this.sqNum = sqNum;
		for(int i = 1; i < sqNum + 1; i++){
			dl[i - 1] = st.CalDis(0, i);
		}
	}
	
	// Get the distance between sequences 
	public double getDis(int index)
	{
		if(index < this.sqNum){
			return dl[index];
		}
		else{
			return public_data.MAXDIS;
		}
	}
	
	// Sets the value of the (x, y) in the distance matrix 
	public void setDis(int index, double dis)
	{
		if(index < this.sqNum){
			dl[index] = dis;
		}
		else{
			System.out.println(" Sets the matrix position to exceed the number of sequence boundaries! ");
			System.exit(0);
		}
	}
	
	// Output the distance matrix
	public void outDisTable()
	{
		System.out.println(String.valueOf(this.dl));
	}
}
