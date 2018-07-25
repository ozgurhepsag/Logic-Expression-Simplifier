package application;

import java.util.ArrayList;

public class Quine {
	private String statement;
	private String simpleStatement;
	private String[] variables;
	private String[] substts;
	private int bits;
	private ArrayList<ArrayList<QuineNode>> table;
	private ArrayList<QuineNode> result;
	private ArrayList<Integer> indices;
	private ArrayList<Integer> indicesFreq;

	public Quine(String statement, String[] variables) {
		this.statement = statement;
		this.variables = variables;
		table = new ArrayList<>();
		result = new ArrayList<>();
		indices = new ArrayList<>();
		indicesFreq = new ArrayList<>();
		String temp = statement.replaceAll("[+]", "");
		substts = temp.split(" +");
		QuineNode qn = new QuineNode(substts[0]);
		bits = qn.getBinary().length();

		for (int i = 0; i < bits + 1; i++) {
			ArrayList<QuineNode> row = new ArrayList<>();
			table.add(row);
		}

		createFirstNodes();
		operations();
		indiceSimplification1();
		indiceSimplification2();
		createFunc();
	}

	public void createFirstNodes() {

		for (int i = 0; i < substts.length; i++) {

			QuineNode qn = new QuineNode(substts[i]);
			table.get(qn.getOnes()).add(qn);

		}

	}

	public void operations() {

		boolean flag = false;

		for (int i = 0; i < table.size() - 1; i++) {

			for (int j = 0; j < table.get(i).size(); j++) {

				if (table.get(i).get(j).isAfterstart() == true)
					continue;

				for (int k = 0; k < table.get(i + 1).size(); k++) {

					if (table.get(i + 1).get(k).isAfterstart() == true) {

						if (k == table.get(i + 1).size() - 1 && table.get(i).get(j).isUsed() == true) {
							table.get(i).remove(j);
							j--;
						}

						continue;
					}

					int difference = 0;

					for (int l = 0; l < bits; l++) {

						if (!(table.get(i).get(j).getBinary().substring(l, l + 1)
								.equalsIgnoreCase(table.get(i + 1).get(k).getBinary().substring(l, l + 1))))
							difference++;

					}

					if (difference == 1) {
						String newbinary = "";

						for (int l = 0; l < bits; l++) {

							if (!(table.get(i).get(j).getBinary().substring(l, l + 1)
									.equalsIgnoreCase(table.get(i + 1).get(k).getBinary().substring(l, l + 1))))
								newbinary = table.get(i).get(j).getBinary().substring(0, l) + "X"
										+ table.get(i).get(j).getBinary().substring(l + 1);

						}

						QuineNode qn = new QuineNode();
						qn.setBinary(newbinary);

						table.get(i).get(j).setUsed(true);
						table.get(i + 1).get(k).setUsed(true);

						ArrayList<Integer> all = new ArrayList<>();

						for (int l = 0; l < table.get(i).get(j).getIndices().size(); l++) {
							all.add(table.get(i).get(j).getIndices().get(l));
						}

						for (int l = 0; l < table.get(i + 1).get(k).getIndices().size(); l++) {
							all.add(table.get(i + 1).get(k).getIndices().get(l));
						}

						qn.setIndices(all);

						table.get(i + 1).add(qn);

						flag = true;

					}

					if (k == table.get(i + 1).size() - 1 && table.get(i).get(j).isUsed() == true) {
						table.get(i).remove(j);
						j--;
					}

				}

			}

			if (i == table.size() - 2) {
				for (int j = 0; j < table.get(i + 1).size(); j++) {

					if (table.get(i + 1).get(j).isAfterstart() == true)
						continue;

					if (table.get(i + 1).get(j).isUsed() == true) {
						table.get(i + 1).remove(j);
						j--;
					} else if (table.get(i + 1).get(j).isUsed() == false) {
						result.add(table.get(i + 1).get(j));
						table.get(i + 1).remove(j);
						j--;
					}
				}
			}

			for (int j = 0; j < table.get(i).size(); j++) {

				if (table.get(i).get(j).isAfterstart() == true)
					continue;

				if (table.get(i).get(j).isUsed() == false) {
					result.add(table.get(i).get(j));
					table.get(i).remove(j);
					j--;
				}

			}

			if (flag == true && i == table.size() - 2) {
				i = 0;
				flag = false;

				for (int j = 0; j < table.size(); j++) {
					for (int j2 = 0; j2 < table.get(j).size(); j2++) {

						table.get(j).get(j2).setAfterstart(false);

					}
				}
			}
		}

	}	
	
	public void indiceSimplification1(){

		for (int i = 0; i < result.size(); i++) {

			for (int j = 0; j < result.size(); j++) {

				if (i == j)
					continue;

				if (result.get(i).getIndices().size() == result.get(j).getIndices().size()) {

					int similarity = 0;

					for (int k = 0; k < result.get(i).getIndices().size(); k++) {

						for (int k2 = 0; k2 < result.get(j).getIndices().size(); k2++) {

							if (result.get(i).getIndices().get(k) == result.get(j).getIndices().get(k2)) {
								similarity++;
								break;
							}

						}

					}

					if (similarity == result.get(i).getIndices().size()) {
						result.remove(j);
						j--;
					}
					
				}

			}

		}

	}
	
	public void indiceSimplification2(){
		
		updateIndiceFreq();
		
		for (int i = 0; i < result.size(); i++) {
			
			boolean isRedundant = true;
			
			for (int j = 0; j < result.get(i).getIndices().size(); j++) {
				
				int tempind = result.get(i).getIndices().get(j);
				
				for (int k = 0; k < indices.size(); k++) {
					
					if(indices.get(k) == tempind && indicesFreq.get(k) < 2){
						isRedundant = false;
					}
					
				}				
				
			}
			
			if(isRedundant == true){
				result.remove(i);
				updateIndiceFreq();
				i--;
			}		
			
		}
	
		
	}
	
	public void updateIndiceFreq(){
		
		indices.clear();
		indicesFreq.clear();
		
		for (int i = 0; i < result.size(); i++) {
			
			for (int j = 0; j < result.get(i).getIndices().size(); j++) {
				
				boolean flag = false;
				
				for (int j2 = 0; j2 < indices.size(); j2++) {
					
					if(indices.get(j2) == result.get(i).getIndices().get(j)){
						flag = true;
						indicesFreq.set(j2, indicesFreq.get(j2) + 1);
						break;
					}
					
				}
				
				if(flag == false){
					indices.add(result.get(i).getIndices().get(j));
					indicesFreq.add(1);
				}			
				
			}			
			
		}		
		
	}
	
	public void createFunc(){
		
		simpleStatement = variables[variables.length - 1] + " = ";
		
		boolean flag = true;

		for (int i = 0; i < result.size(); i++) {
			String substt = "";
			boolean flag1 = true;

			for (int j = 0; j < result.get(i).getBinary().length(); j++) {

				if (result.get(i).getBinary().substring(j, j + 1).equals("1")){
					substt = substt + variables[j] + ".";
					flag1 = false;
					}
				else if (result.get(i).getBinary().substring(j, j + 1).equals("0")){
					substt = substt + variables[j] + "'" + ".";
					flag1 = false;
					}

			}
			
			if (flag1 == false) {
				substt = substt.substring(0, substt.length() - 1);
				result.get(i).setState(substt);
				simpleStatement = simpleStatement + result.get(i).getState() + " + ";
				flag = false;
			}



		}
		
		if(flag == false)
			simpleStatement = simpleStatement.substring(0, simpleStatement.length() - 3);
		else
			simpleStatement = simpleStatement + "1";

	}

 	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String[] getSubstts() {
		return substts;
	}

	public void setSubstts(String[] substts) {
		this.substts = substts;
	}

	public int getBits() {
		return bits;
	}

	public void setBits(int bits) {
		this.bits = bits;
	}

	public ArrayList<ArrayList<QuineNode>> getTable() {
		return table;
	}

	public void setTable(ArrayList<ArrayList<QuineNode>> table) {
		this.table = table;
	}

	public ArrayList<QuineNode> getResult() {
		return result;
	}

	public void setResult(ArrayList<QuineNode> result) {
		this.result = result;
	}

	
	public ArrayList<Integer> getIndices() {
		return indices;
	}

	
	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}

	
	public ArrayList<Integer> getIndicesFreq() {
		return indicesFreq;
	}

	
	public void setIndicesFreq(ArrayList<Integer> indicesFreq) {
		this.indicesFreq = indicesFreq;
	}
	

	public String getSimpleStatement() {
		return simpleStatement;
	}
	

	public void setSimpleStatement(String simpleStatement) {
		this.simpleStatement = simpleStatement;
	}

}

