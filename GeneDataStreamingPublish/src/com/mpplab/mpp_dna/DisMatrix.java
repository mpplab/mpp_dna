package com.mpplab.mpp_dna;

import java.util.List;

/*
 * Introduction: Construct and operate the distance list
 *       		 Only record the case of x <= y because of symmetry of the matrix
 * */
public class DisMatrix {
	public int sqNum;	// sequence number
	double[][] dm;
	
	// Construction function
	public DisMatrix(int sqNum){
		dm = new double[sqNum][sqNum];
		this.sqNum = sqNum;
	}
	
	// Construction function
	public DisMatrix(int sqNum, SeqTable st){
		dm = new double[sqNum][sqNum];
		this.sqNum = sqNum;
		for(int i = 0; i < sqNum; i++)
		{
			for(int j = i; j < sqNum; j++)
			{
				if(i == j) dm[i][j] = public_data.MAXDIS;
				else dm[i][j] = st.CalDis(i, j);
			}
		}
	}
	
	// Get the distance matrix by file
	public DisMatrix(int sqNum, String file){
		FileHelper hp = new FileHelper();
		dm = new double[sqNum][sqNum];
		this.sqNum = sqNum;
		
		List<Float> fileRead = hp.readDisFromFile(file);
		int index = 0;
		for(int i = 0; i < sqNum; i++)
		{
			for(int j = i; j < sqNum; j++)
			{
				if(i == j) dm[i][j] = public_data.MAXDIS;
				else{
					dm[i][j] = fileRead.get(index++);
				}
			}
		}
	}
	
	// Get distance by index
	public double getDis(int xIndex, int yIndex)
	{
		if(xIndex < this.sqNum && yIndex < this.sqNum){
			if(xIndex > yIndex) return dm[yIndex][xIndex];
			else return dm[xIndex][yIndex];
		}
		else{
			return public_data.MAXDIS;
		}
	}
	
	// Set the distance of (x,y) to dis
	public void setDis(int xIndex, int yIndex, double dis)
	{
		if(xIndex < this.sqNum && yIndex < this.sqNum){
			dm[xIndex][yIndex] = dis;
		}
		else{
			System.out.println(" Sets the matrix position to exceed the number of sequence boundaries! ");
			System.exit(0);
		}
	}
	
	// Output the distance matrix
	public void outDisTable()
	{
		for(int i = 0; i < sqNum; i++)
		{
			for(int j = 0; j < sqNum; j++)
			{
				System.out.printf("%-18s", "[" + i + "," + j + "]:" + String.format(" %.8f", dm[i][j]));
			}
			System.out.print("\r\n");
		}
	}
}
