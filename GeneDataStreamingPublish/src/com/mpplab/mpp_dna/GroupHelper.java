package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

public class GroupHelper {
	
	List<int[]> result;
	
	public GroupHelper(){}

	// Initialize the result list
	private void initResult(int sqNum){
		result = new ArrayList<int[]>();
		int loop = sqNum / 2;
		int hasRemainder = sqNum % 2;
		
		if(hasRemainder == 0){
			for(int i = 0; i < loop; i++){
				int[] add = new int[]{0, 0};
				result.add(add);
			}
		}
		else{
			for(int i = 0; i < loop - 1; i++){
				int[] add = new int[]{0, 0};
				result.add(add);
			}
			int[] add = new int[]{0, 0, 0};
			result.add(add);
		}
	}
	
	// Greedy group clustering by distance matrix
	// Traversing distance matrix, every time the minimum distance points in a group 
	public List<int[]> getGreedyGroupByMatrix(DisMatrix dm){
		
		int sqNum = dm.dm.length;
		double[][] matrix = new double[sqNum][sqNum];
		
		// Copy the matrix
		for(int i = 0; i < sqNum; i++)
			for(int j = 0; j < sqNum; j++)
				matrix[i][j] = dm.dm[i][j];
				
		int loop = sqNum / 2;	//loop time
		
		initResult(sqNum);		// Initialize the result list
		
		for(int si = 0; si < loop; si++) {

			if(si != loop - 1)
			{
				double min = 0;
				int mini = -1, minj = -1;
				
				//  Finding the minimum value (group) in the current matrix with the exception of the node that is not available. 
				for(int i = 0; i < sqNum; i++){
					for(int j = i + 1; j < sqNum; j++){
						double dis = matrix[i][j]; 
						if(dis != public_data.MAXDIS){	// skip the case i==j
							if(mini == -1 && minj == -1){
								min = dis;
								mini = i;
								minj = j;
							}
							else if(dis < min){
								min = dis;
								mini = i;
								minj = j;
							}
						}
					}
				}
				//Insert into the result
				int[] g = new int[2];
				g[0] = mini; g[1] = minj;
				result.set(si, g);
				
				// Set the row, the column is not available 
				for(int sqi = 0; sqi < sqNum; sqi++){
					matrix[mini][sqi] = public_data.MAXDIS;
				}
				for(int sqi = 0; sqi < sqNum; sqi++){
					matrix[sqi][mini] = public_data.MAXDIS;
				}
				for(int sqi = 0; sqi < sqNum; sqi++){
					matrix[minj][sqi] = public_data.MAXDIS;
				}
				for(int sqi = 0; sqi < sqNum; sqi++){
					matrix[sqi][minj] = public_data.MAXDIS;
				}
			}
			else{
				// Put the remaining in a group 
				int[] add = new int[sqNum - (loop-1)*2];
				int index = 0;
				
				List<Integer> temp = new ArrayList<Integer>();
				for(int i = 0; i < loop - 1; i++){
					for(int j = 0; j < 2; j++){
						temp.add(result.get(i)[j]);
					}
				}
				for(int i = 0; i < sqNum; i ++){
					if(!temp.contains(i)){
						add[index++] = i;
					}						
				}
				result.set(si, add);
			}
		}
		outGroup(dm);
		
		return result;
	}
	
	// Group clustering based on MWM
	public List<int[]> getMWMGroupByMatrix(DisMatrix dm){

		int sqNum = dm.dm.length;
		
		double[][] matrix = new double[sqNum][sqNum];
		
		// Copy the matrix
		for(int i = 0; i < sqNum; i++)
			for(int j = 0; j < sqNum; j++)
				matrix[i][j] = dm.dm[i][j];
		
		// Find the maximum distance 
		double maxDis = 0;
		for(int i = 0; i < sqNum; i++){
			for(int j = i+1; j < sqNum; j++){
				if(matrix[i][j] > maxDis){
					maxDis = matrix[i][j];
				}
			}
		}
		// Generate reverse matrix: The distance is changed to the Maximum Distance - original distance 
		// The maximum weight matching of the inverse matrix is the maximum matching of the minimum weight of the original matrix. 
		List<double[]> cMatrix = new ArrayList<double[]>();
		for(int i = 0; i < sqNum; i++){
			for(int j = i+1; j < sqNum; j++){
				double[] current = new double[3];
				current[0] = i;
				current[1] = j;
				current[2] = maxDis - matrix[i][j];
				cMatrix.add(current);
			}
		}
		
		String inputFile = public_data.GroupDisFile;
		String outputFile = public_data.GroupResFile;
		
		// Write the distance matrix to file 
		new FileHelper().writeMatrixToFile(cMatrix, inputFile);
		
		// Start MWM program for grouping 
		MWMatching mwm = new MWMatching(inputFile, outputFile);
		result = mwm.calculate();		
		outGroup(dm);
				
		return result;
	}
	
	// Find the average distance according to the results of the group
	public double getArgDisByGroupAndMatrix(List<int[]> groupList, DisMatrix dm){
		
		double ret = 0;
		
		for(int i = 0; i < groupList.size(); i++){
			int[] curGroup = groupList.get(i);
			if(curGroup.length == 2){
				ret += dm.getDis(curGroup[0], curGroup[1]);
			}
			else if(curGroup.length == 3){
				AlHelper ah = new AlHelper();
				ret += ah.getThreeTupleDis(curGroup);
			}
		}
		
		ret = ret / groupList.size();
		
		return ret;
	}
	
	// Output the result of clustering
	private void outGroup(DisMatrix dm){
		float sum = 0;
		int count = 0;
		for(int i = 0; i < result.size(); i++) {
			int[] g = result.get(i);
			System.out.print("No."+ String.valueOf(count+1) + ": ");
			for(int j = 0; j < g.length; j++) {
				System.out.print(g[j] + " ");
			}
			if(g.length == 2){
				System.out.print(String.format(": %.8f", dm.getDis(g[0], g[1])));
				sum += dm.getDis(g[0], g[1]);
				count++;
			}
			System.out.print("\r\n");
		}
		System.out.println(String.format("Average distance: %.8f", sum / count));
	}
}
