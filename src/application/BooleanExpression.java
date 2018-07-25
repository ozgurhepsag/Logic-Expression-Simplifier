package application;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BooleanExpression {
	private String variables[];
	private String simpleVars[];
	private String simpleexp;
	private boolean isStandard;
	private String standardSOP;
	private String fullState;
	private String[] states;
	private int variableCount;
	private int[][] truthTable;
	
	public BooleanExpression(String fullState) {
		super();
		this.fullState = fullState;
		splitFullState(fullState);
		variableCountDecider();		
		isStandard = isStandard();
		
		truthTable = new int[(int) Math.pow(2, variableCount)][variableCount + 1];
		generateTruthTable();
		
		if (isStandard == false) {
			generateStandardSOP();
		} 
		else {
			standardSOP = fullState;
			fillTable();
		}
		
		decideVariables();
		simplification();
		
		
	}
	
	public BooleanExpression() {
		super();
		this.fullState = "";
	}
	
	public void splitFullState(String fullstate){
		
		fullstate = fullstate.replaceAll("[+]", "");
		fullstate = fullstate.replaceAll("[=]", "");
		
		states = fullstate.split(" +");
		
	}
	
	public void fillTable(){
		
		int index = 0;

		for (int i = 1; i < states.length; i++) { // states[0] = F

			String state = states[i];
			String items[] = state.split("[.]");

			for (int j = 0; j < variableCount; j++) {

				if (items[j].length() == 1) // A , B , C , D which is 1
					index += (int) Math.pow(2, variableCount - 1 - j);

			}

			truthTable[index][variableCount] = 1;
			index = 0;

		}
		
	}
	
	public void generateTruthTable(){
		
		int value = 0;
		
		for (int i = 0; i < truthTable[i].length - 1; i++) {
			int mod = ((int) Math.pow(2, variableCount - 1 - i) );
			
			for (int j = 0; j < truthTable.length; j++) {
				
				if(j != 0 && j % mod == 0){
					if(value == 0)
						value = 1;
					else
						value = 0;
				}					
				
				truthTable[j][i] = value;
				
			}
			value = 0;
			mod = mod / 2;
		}
			
	}
	
	public void generateStandardSOP(){
		
		ArrayList<String> nonstandards = new ArrayList<>();
		ArrayList<String> standards = new ArrayList<>();
		
		for (int i = 1; i < states.length; i++) {
			
			String state = states[i];
			state = state.replaceAll("'", "");
			String items[] = state.split("[.]");
			
			if(items.length != variableCount)
				nonstandards.add(states[i]);
			else
				standards.add(states[i]);
			
		}
		
		String stt = standards.get(0);
		
		stt = stt.replaceAll("'", "");
		stt = stt + ".F";
		variables = stt.split("[.]");
		
		
		for (int i = 0; i < nonstandards.size(); i++) {
			
			for (int j = 0; j < truthTable.length; j++) {			
				
				String state = nonstandards.get(i);
				String items[] = state.split("[.]");
				boolean flag = true;
				
				for (int j2 = 0; j2 < items.length ; j2++) {
					
					int index = -1;
					
					if(items[j2].equalsIgnoreCase(variables[0]) || items[j2].equalsIgnoreCase(variables[0] + "'"))
						index = 0;
					else if(items[j2].equalsIgnoreCase(variables[1]) || items[j2].equalsIgnoreCase(variables[1] + "'"))
						index = 1;
					else if(items[j2].equalsIgnoreCase(variables[2]) || items[j2].equalsIgnoreCase(variables[2] + "'"))
						index = 2;
					else if(items[j2].equalsIgnoreCase(variables[3]) || items[j2].equalsIgnoreCase(variables[3] + "'"))
						index = 3;
					
					
					if((items[j2].equalsIgnoreCase(variables[index]) && truthTable[j][index] == 0 ) 
							|| (items[j2].equalsIgnoreCase(variables[index] + "'") && truthTable[j][index] == 1 )){
						flag = false;
					}
					/*else if((items[j2].equalsIgnoreCase(variables[1]) && truthTable[j][index] == 0 ) 
							|| (items[j2].equalsIgnoreCase(variables[1] + "'") && truthTable[j][index] == 1 )){
						flag = false;
					}
					else if((items[j2].equalsIgnoreCase(variables[2]) && truthTable[j][index] == 0 ) 
							|| (items[j2].equalsIgnoreCase(variables[2] + "'") && truthTable[j][index] == 1 )){
						flag = false;
					}
					else if((items[j2].equalsIgnoreCase(variables[3]) && truthTable[j][index] == 0 ) 
							|| (items[j2].equalsIgnoreCase(variables[3] + "'") && truthTable[j][index] == 1 )){
						flag = false;
					}*/
													
				}
				
				if(flag == true)
					truthTable[j][variableCount] = 1;
				
				flag = true;
			}			
			
		}
		
		int index = 0;

		for (int i = 0; i < standards.size(); i++) { // states[0] = F

			String state = standards.get(i);
			String items[] = state.split("[.]");

			for (int j = 0; j < variableCount; j++) {

				if (items[j].length() == 1) // A , B , C , D which is 1
					index += (int) Math.pow(2, variableCount - 1 - j);

			}

			truthTable[index][variableCount] = 1;
			index = 0;

		}
		
		TruthTable tt = new TruthTable(getVariables());
		tt.setTable(truthTable);
		standardSOP = tt.booleanExp();
		
	}
	
	public boolean isStandard(){
		
		boolean standard = true;
		
		for (int i = 1; i < states.length; i++) {
			
			String state = states[i];
			state = state.replaceAll("'", "");
			String items[] = state.split("[.]");
					
			if(items.length != variableCount){
				standard = false;				
			}
			
		}
		
		return standard;
	}
	
	public void variableCountDecider(){
		
		int max = 0;
		
		for (int i = 1; i < states.length; i++) { // states[0] = F
			String state = states[i];
			state = state.replaceAll("'", "");
			String items[] = state.split("[.]");
			
			if(max < items.length){
				max = items.length;
			}
			
		}
		
		variableCount = max;
		
	}
	
	public ObservableList<TruthTableRow> getTableView(){
		
		ObservableList<TruthTableRow> truthtable = FXCollections.observableArrayList();        
		
		for (int i = 0; i < truthTable.length; i++) {
			TruthTableRow tt = null;
			
			if(truthTable[i].length == 5){
				tt = new TruthTableRow(Integer.toString(truthTable[i][0]), Integer.toString(truthTable[i][1]), 
						Integer.toString(truthTable[i][2]), Integer.toString(truthTable[i][3]), Integer.toString(truthTable[i][4]));
            }
            if(truthTable[i].length == 4){
            	tt = new TruthTableRow(Integer.toString(truthTable[i][0]), Integer.toString(truthTable[i][1]), 
						Integer.toString(truthTable[i][2]), Integer.toString(truthTable[i][3]));
            }
            if(truthTable[i].length == 3){
            	tt = new TruthTableRow(Integer.toString(truthTable[i][0]), Integer.toString(truthTable[i][1]), 
						Integer.toString(truthTable[i][2]));
            }
            if(truthTable[i].length == 2){
            	tt = new TruthTableRow(Integer.toString(truthTable[i][0]), Integer.toString(truthTable[i][1]));
            }
            
            truthtable.add(tt);
			
		}		
		
		return truthtable;
	}
	
	public void simplification(){	
		Quine qn = new Quine(standardSOP.substring(4), variables);
		simpleexp = qn.getSimpleStatement();	
	}
	
	public TruthTable ttOfSimpleBE(){
		
		ArrayList<String> inputs = new ArrayList<>();
		boolean isSameTable = false;
		
		String temp = simpleexp.substring(4);
		
		temp = temp.replaceAll("[+]", "");
		temp = temp.replaceAll("[=]", "");

		String[] substts = temp.split(" +");
		
		
		for (int i = 0; i < substts.length; i++) {
			
			String temp2 = substts[i].replaceAll("'", "");
			String[] letters = temp2.split("[.]");
			
			for (int j = 0; j < letters.length; j++) {
				
				boolean flag = false;
				
				for (int k = 0; k < inputs.size(); k++) {
					
					if(inputs.get(k).equals(letters[j])){
						flag = true;
						break;
					}
									
					
				}
				
				if(flag == false)
					inputs.add(letters[j]);				
				
			}			
			
		}
		
		inputs.add("F");
		
		String[] tempVars = new String[inputs.size()];
		simpleVars = tempVars;
		
		for (int i = 0; i < tempVars.length; i++) {
			tempVars[i] = inputs.get(i);
		}
		
		TruthTable tempTT = new TruthTable(tempVars);
		
		for (int i = 0; i < substts.length; i++) {
			
			
			for (int j = 0; j < tempTT.getTable().length; j++) {
				
				String state = substts[i];
				String items[] = state.split("[.]");
				boolean flag = true;
				
				for (int j2 = 0; j2 < items.length ; j2++) {
					
					int index = -1;
					int index2 = -1;
					
					if(items[j2].equalsIgnoreCase(variables[0]) || items[j2].equalsIgnoreCase(variables[0] + "'"))
						index = 0;
					else if(items[j2].equalsIgnoreCase(variables[1]) || items[j2].equalsIgnoreCase(variables[1] + "'"))
						index = 1;
					else if(items[j2].equalsIgnoreCase(variables[2]) || items[j2].equalsIgnoreCase(variables[2] + "'"))
						index = 2;
					else if(items[j2].equalsIgnoreCase(variables[3]) || items[j2].equalsIgnoreCase(variables[3] + "'"))
						index = 3;
					
					if(items[j2].equalsIgnoreCase(tempVars[0]) || items[j2].equalsIgnoreCase(tempVars[0] + "'"))
						index2 = 0;
					else if(items[j2].equalsIgnoreCase(tempVars[1]) || items[j2].equalsIgnoreCase(tempVars[1] + "'"))
						index2 = 1;
					else if(items[j2].equalsIgnoreCase(tempVars[2]) || items[j2].equalsIgnoreCase(tempVars[2] + "'"))
						index2 = 2;
					else if(items[j2].equalsIgnoreCase(tempVars[3]) || items[j2].equalsIgnoreCase(tempVars[3] + "'"))
						index2 = 3;
					
					
					if((items[j2].equalsIgnoreCase(variables[index]) && tempTT.getTable()[j][index2] == 0 ) 
							|| (items[j2].equalsIgnoreCase(variables[index] + "'") && tempTT.getTable()[j][index2] == 1 )){
						flag = false;
					}
													
				}
				
				if(flag == true)
					tempTT.getTable()[j][inputs.size() - 1] = 1;
				
				flag = true;
				
				
			}		
			
			
		}
		
		return tempTT;
		
		
	}

	public String getFullState() {
		return fullState;
	}

	public void setFullState(String fullState) {
		this.fullState = fullState;
	}

	public String[] getStates() {
		return states;
	}

	public void setStates(String[] states) {
		this.states = states;
	}

	public void setTruthTable(int[][] truthTable) {
		this.truthTable = truthTable;
	}

	public int[][] getTruthTable() {
		return truthTable;
	}

	public int getVariableCount() {
		return variableCount;
	}

	public void setVariableCount(int variableCount) {
		this.variableCount = variableCount;
	}
	
	public String getStandardSOP() {
		return standardSOP;
	}

	public void setStandardSOP(String standardSOP) {
		this.standardSOP = standardSOP;
	}

	public void decideVariables(){
		String full = standardSOP.substring(4);
		full = full.replaceAll("[+]", "");
		String[] sop = full.split(" +");
		
		sop[0] = sop[0].replaceAll("'", "");
		sop[0] = sop[0] + ".F";
		variables = sop[0].split("[.]");
	}
	
	public String[] getVariables(){			
		return variables;
	}

	public String getSimpleexp() {
		return simpleexp;
	}

	public String[] getSimpleVars() {
		return simpleVars;
	}

	public void setSimpleVars(String[] simpleVars) {
		this.simpleVars = simpleVars;
	}	
	
}
