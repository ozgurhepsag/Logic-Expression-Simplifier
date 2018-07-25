package application;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TruthTable {
	
	private ArrayList<String> dontcares;
	private String exp;
	private int variableCount;
	private String[] heads;
	private int[][] table;
	
	public TruthTable(String[] heads) {
		super();
		dontcares = new ArrayList<>();
		this.heads = heads;
		variableCount = heads.length - 1;
		createTable();
	}
	
	public void createTable(){
		
		table = new int[(int) Math.pow(2, variableCount)][variableCount + 1];
		
		int value = 0;
		
		for (int i = 0; i < table[i].length - 1; i++) {
			int mod = ((int) Math.pow(2, variableCount - 1 - i) );
			
			for (int j = 0; j < table.length; j++) {
				
				if(j != 0 && j % mod == 0){
					if(value == 0)
						value = 1;
					else
						value = 0;
				}					
				
				table[j][i] = value;
				
			}
			value = 0;
			mod = mod / 2;
		}
		
		for (int i = 0; i < table.length; i++) {
			table[i][variableCount]	= 0;			
		}
		
	}
	
	public String booleanExp(){
		
		exp = heads[heads.length - 1] + " = ";

		
		for (int i = 0; i < table.length; i++) {
			
			if(table[i][heads.length - 1] == 1){
				String state = "";
				
				for (int j = 0; j < heads.length - 1; j++) {
					
					if(table[i][j] == 0)
						state = state + heads[j] + "'";
					else if(table[i][j] == 1)
						state = state + heads[j];	
	
					state = state + ".";										
				}				
				state = state.substring(0, state.length() - 1);
				exp = exp + state + " + ";
				
			}			
						
		}
		
		exp = exp.substring(0, exp.length() - 3);		
		return exp;
		
	}
	
	public ObservableList<TruthTableRow> getTableView(){
		
		ObservableList<TruthTableRow> truthtable = FXCollections.observableArrayList();        
		
		for (int i = 0; i < table.length; i++) {
			TruthTableRow tt = null;
			
			if(table[i].length == 5){
				if(table[i][4] == -1)
					tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]), 
							Integer.toString(table[i][2]), Integer.toString(table[i][3]), "X");
				else
					tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]), 
							Integer.toString(table[i][2]), Integer.toString(table[i][3]), Integer.toString(table[i][4]));
					
            }
            if(table[i].length == 4){
            	if(table[i][3] == -1)
            		tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]), 
            				Integer.toString(table[i][2]), "X");
            	else
            		tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]), 
            				Integer.toString(table[i][2]), Integer.toString(table[i][3]));
            }
            if(table[i].length == 3){
            	if(table[i][2] == -1)
            		tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]), 
            				"X");
            	else
            		tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]), 
            				Integer.toString(table[i][2]));
            }
            if(table[i].length == 2){
            	if(table[i][1] == -1)
            		tt = new TruthTableRow(Integer.toString(table[i][0]), "X");
            	else
            		tt = new TruthTableRow(Integer.toString(table[i][0]), Integer.toString(table[i][1]));
            }
            
            truthtable.add(tt);
			
		}		
		
		return truthtable;
	}
	
	public void createDontcares() {
		
		for (int i = 0; i < table.length; i++) {
			
			if(table[i][heads.length - 1] == -1){
				String state = "";
				
				for (int j = 0; j < heads.length - 1; j++) {
					
					if(table[i][j] == 0)
						state = state + heads[j] + "'";
					else if(table[i][j] == 1)
						state = state + heads[j];	
	
					state = state + ".";										
				}				
				state = state.substring(0, state.length() - 1);
				dontcares.add(state);
				
			}			
						
		}
		
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public int getVariableCount() {
		return variableCount;
	}

	public void setVariableCount(int variableCount) {
		this.variableCount = variableCount;
	}

	public String[] getHeads() {
		return heads;
	}

	public void setHeads(String[] heads) {
		this.heads = heads;
	}

	public int[][] getTable() {
		return table;
	}

	public void setTable(int[][] table) {
		this.table = table;
	}

	public ArrayList<String> getDontcares() {
		return dontcares;
	}

	public void setDontcares(ArrayList<String> dontcares) {
		this.dontcares = dontcares;
	}
	
}
