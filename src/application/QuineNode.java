package application;

import java.util.ArrayList;

public class QuineNode {
	private boolean isUsed, afterstart;
	private int ones;
	private String substate;
	private String binary;
	private ArrayList<Integer> indices;
	
	public QuineNode(String substate) {
		indices = new ArrayList<>();
		this.substate = substate;
		ones = 0;
		binary = "";
		isUsed = false;
		afterstart = false;
		operations();
	}
	
	public QuineNode(){
		binary = "";
		isUsed = false;
		afterstart = true;
	}
	
	public void operations(){
		
		String items[] = substate.split("[.]");
		int indice = 0;
			
		for (int i = 0; i < items.length; i++) {
			

			
			if(items[i].length() == 1){
				binary = binary + "1";
				indice += (int) Math.pow(2, items.length - i - 1);
				ones++;
			}
			else if(items[i].length() == 2)
				binary = binary + "0";					
			
		}
		indices.add(indice);
	}	

	
	public boolean isAfterstart() {
		return afterstart;
	}

	public void setAfterstart(boolean afterstart) {
		this.afterstart = afterstart;
	}

	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public String getState() {
		return substate;
	}
	public void setState(String state) {
		this.substate = state;
	}

	public ArrayList<Integer> getIndices() {
		return indices;
	}
	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}

	public int getOnes() {
		return ones;
	}

	public void setOnes(int ones) {
		this.ones = ones;
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
	}
	

}
