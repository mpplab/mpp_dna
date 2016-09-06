package com.mpplab.mpp_dna;

import java.util.ArrayList;
import java.util.List;

public class GroupResult {

	public List<int[]> GroupList;
	public List<Integer> EleList;
	public List<Double> DisList;
	public double ArgDistance = 0;
	
	public GroupResult(){
		GroupList = new ArrayList<int[]>();
		EleList = new ArrayList<Integer>();
		DisList = new ArrayList<Double>();
	}
	
	// Add a group to the result
	public void AddOneResult(int[] group, double dis){
		this.GroupList.add(group);
		for(int i = 0; i < group.length; i++){
			this.EleList.add(group[i]);
		}
		this.DisList.add(dis);
	}
	
	// Remove a group from the result
	public void RemoveOneResult(int[] group){
		int rindex = GroupList.indexOf(group);
		this.GroupList.remove(rindex);
		this.DisList.remove(rindex);
		for(int i = 0; i < group.length; i++){
			this.EleList.remove(EleList.indexOf(group[i]));
		}
	}
}
