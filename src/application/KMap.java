package application;

import java.util.ArrayList;

public class KMap {
	private String fullStatement;
	private String simpleStatement;
	private int variableCount;
	private String[] variables;
	private String[] minterms;
	private KMapNode[][] map;
	private ArrayList<String> dontcares;
	private ArrayList<KMapGroup> result;
	private ArrayList<String> column;
	private ArrayList<String> row;
	private static int colorid = 1;
	
	public KMap(String fullStatement, ArrayList<String> dontcares) {
		this.dontcares = dontcares;
		result = new ArrayList<>();
		column = new ArrayList<>();
		row = new ArrayList<>();
		this.fullStatement = fullStatement;		
		start();
		
		if(variableCount == 4)
			map3();
		else if(variableCount == 3)
			map2();
		else if(variableCount == 2)
			map1();
		
		result();
	}
	
	public KMap(String fullStatement) {
		this.dontcares= new ArrayList<>();
		result = new ArrayList<>();
		column = new ArrayList<>();
		row = new ArrayList<>();
		this.fullStatement = fullStatement;		
		start();
		
		if(variableCount == 4)
			map3();
		else if(variableCount == 3)
			map2();
		else if(variableCount == 2)
			map1();
		
		result();
		
	}
	
	public void start(){
		
		String statement = fullStatement.substring(4);
		statement = statement.replaceAll("[+]", "");
		minterms = statement.split(" +");
		String temp = minterms[0].replaceAll("'", "");
		variables = temp.split("[.]");
		variableCount = variables.length;
		
		if(variableCount == 4){
			
			column.add(variables[0] + "'." + variables[1] + "'");
			column.add(variables[0] + "'." + variables[1]);
			column.add(variables[0] + "." + variables[1]);
			column.add(variables[0] + "." + variables[1] + "'");
			row.add(variables[2] + "'." + variables[3] + "'");
			row.add(variables[2] + "'." + variables[3]);
			row.add(variables[2] + "." + variables[3]);
			row.add(variables[2] + "." + variables[3] + "'");				
			
		}
		else if(variableCount == 3){
			
			column.add(variables[0] + "'." + variables[1] + "'");
			column.add(variables[0] + "'." + variables[1]);
			column.add(variables[0] + "." + variables[1]);
			column.add(variables[0] + "." + variables[1] + "'");
			row.add(variables[2] + "'");
			row.add(variables[2]);
			
		}
		else if(variableCount == 2){
			column.add(variables[0] + "'");
			column.add(variables[0]);
			row.add(variables[1] + "'");
			row.add(variables[1]);
		}
		
		
	}
	
	public void map1(){ // if we have 2 variables
		
		map = new KMapNode[2][2];
		
		map[0][0] = new KMapNode(0, 0);
		map[0][1] = new KMapNode(1, 0);
		map[1][0] = new KMapNode(2, 0);
		map[1][1] = new KMapNode(3, 0);
		
		prepare1s();
		prepareDontCares();
		check2v1();
		check2v2();
		check1();
		
	}
	
	public void map2(){ // if we have 3 variables
		
		map = new KMapNode[4][2];
		
		map[0][0] = new KMapNode(0, 0);
		map[0][1] = new KMapNode(1, 0);
		map[1][0] = new KMapNode(2, 0);
		map[1][1] = new KMapNode(3, 0);
		map[2][0] = new KMapNode(6, 0);
		map[2][1] = new KMapNode(7, 0);
		map[3][0] = new KMapNode(4, 0);
		map[3][1] = new KMapNode(5, 0);
		
		prepare1s();
		prepareDontCares();
		check4v1();
		check4v3();
		check2v1();
		check2v2();
		check1();

		
	}

	public void map3(){ // if we have 4 variables
		
		map = new KMapNode[4][4];
		
		map[0][0] = new KMapNode(0, 0);
		map[0][1] = new KMapNode(1, 0);
		map[0][2] = new KMapNode(3, 0);
		map[0][3] = new KMapNode(2, 0);
		map[1][0] = new KMapNode(4, 0);
		map[1][1] = new KMapNode(5, 0);
		map[1][2] = new KMapNode(7, 0);
		map[1][3] = new KMapNode(6, 0);
		map[2][0] = new KMapNode(12, 0);
		map[2][1] = new KMapNode(13, 0);
		map[2][2] = new KMapNode(15, 0);
		map[2][3] = new KMapNode(14, 0);
		map[3][0] = new KMapNode(8, 0);
		map[3][1] = new KMapNode(9, 0);
		map[3][2] = new KMapNode(11, 0);
		map[3][3] = new KMapNode(10, 0);
			
		prepare1s();
		prepareDontCares();
		check8v1();
		check8v2();
		check4v1();
		check4v2();
		check4v3();
		check2v1();
		check2v2();
		check1();
	
	}
	
	public void prepare1s(){
		
		for (int i = 0; i < minterms.length; i++) {
			int indice = 0;
			String items[] = minterms[i].split("[.]");
			
			for (int j = 0; j < items.length; j++) {
				
				if(items[j].length() == 1){
					indice += (int) Math.pow(2, items.length - j - 1);
				}			
				
			}
			
			KMapNode node = new KMapNode(indice, 0);
			node.setValue(1);
			
			for (int j = 0; j < map.length; j++) {
				for (int k = 0; k < map[j].length; k++) {
					
					if(map[j][k].getIndice() == node.getIndice()){
						map[j][k] = node;
					}
					
				}
			}
			
		}
	}
	
	public void prepareDontCares(){
		
		for (int i = 0; i < dontcares.size(); i++) {
			int indice = 0;
			String items[] = dontcares.get(i).split("[.]");
			
			for (int j = 0; j < items.length; j++) {
				
				if(items[j].length() == 1){
					indice += (int) Math.pow(2, items.length - j - 1);
				}			
				
			}
			
			KMapNode node = new KMapNode(indice, 0);
			node.setValue(-1);
			
			for (int j = 0; j < map.length; j++) {
				for (int k = 0; k < map[j].length; k++) {
					
					if(map[j][k].getIndice() == node.getIndice()){
						map[j][k] = node;
					}
					
				}
			}
			
		}
	}
	
	public void checkAll() {
		
		boolean isAllZero = false;
		boolean isAllOne = false;
		
		for (int i = 0; i < map.length; i++) { // for 0s
			for (int j = 0; j < map[i].length; j++) {
				
				if(map[i][j].getValue() == 1)
					isAllZero = false;				
				
			}
		}
		
		for (int i = 0; i < map.length; i++) { // for 1s
			for (int j = 0; j < map[i].length; j++) {
				
				if(map[i][j].getValue() == 0)
					isAllOne = false;
								
			}
		}
		
		if (isAllZero) {
			result.add(new KMapGroup("0", colorid));
			colorid++;
		} 
		else if (isAllOne) {
			result.add(new KMapGroup("1", colorid));
			colorid++;
		}
		
		
	}

	public void check8v1() { // soldan saða 8li

		for (int i = 0; i < map.length; i++) {

			boolean isResult = false;
			boolean isNecessary = false;

			if ((map[i][0].getValue() == 1 || map[i][0].getValue() == -1)
					&& (map[(i + 1) % map.length][0].getValue() == 1 || map[(i + 1) % map.length][0].getValue() == -1)
					&& (map[i][1].getValue() == 1 || map[i][1].getValue() == -1)
					&& (map[(i + 1) % map.length][1].getValue() == 1 || map[(i + 1) % map.length][1].getValue() == -1)
					&& (map[i][2].getValue() == 1 || map[i][2].getValue() == -1)
					&& (map[(i + 1) % map.length][2].getValue() == 1 || map[(i + 1) % map.length][2].getValue() == -1)
					&& (map[i][3].getValue() == 1 || map[i][3].getValue() == -1)
					&& (map[(i + 1) % map.length][3].getValue() == 1 || map[(i + 1) % map.length][3].getValue() == -1)) {

				if (map[i][0].isProcessed() == false || map[(i + 1) % map.length][0].isProcessed() == false
						|| map[i][1].isProcessed() == false || map[(i + 1) % map.length][1].isProcessed() == false
						|| map[i][2].isProcessed() == false || map[(i + 1) % map.length][2].isProcessed() == false
						|| map[i][3].isProcessed() == false || map[(i + 1) % map.length][3].isProcessed() == false) {

					isResult = true;

					if ((map[i][0].isProcessed() == false && map[i][0].getValue() == 1) || (map[i][1].isProcessed() == false && map[i][1].getValue() == 1)
							|| (map[i][2].isProcessed() == false  && map[i][2].getValue() == 1)|| (map[i][3].isProcessed() == false && map[i][3].getValue() == 1)
							|| (map[(i + 1) % map.length][0].isProcessed() == false && map[(i + 1) % map.length][0].getValue() == 1 )|| (map[(i + 1) % map.length][1].isProcessed() == false && map[(i + 1) % map.length][1].getValue() == 1)
							|| (map[(i + 1) % map.length][2].isProcessed() == false && map[(i + 1) % map.length][2].getValue() == 1) || (map[(i + 1) % map.length][3].isProcessed() == false && map[(i + 1) % map.length][3].getValue() == 1)) {
						isNecessary = true;
					}

					if (map[i][0].getValue() == 1 && map[i][1].getValue() == -1 && map[i][2].getValue() == -1
							&& map[i][3].getValue() == -1 && map[(i + 1) % map.length][0].getValue() == -1
							&& map[(i + 1) % map.length][1].getValue() == -1 && map[(i + 1) % map.length][2].getValue() == -1
							&& map[(i + 1) % map.length][3].getValue() == -1) {
						isNecessary = false;
					}

				}

			}
			
			if(isResult && isNecessary){
				String term = "";

				String[] str1 = column.get(i).split("[.]");
				String[] str2 = column.get((i + 1) % map.length).split("[.]");
				
				if(str1[0].equals(str2[0])){
					term = term + str1[0];
				}
				else if(str1[1].equals(str2[1])){
					term = term + str1[1];
				}
				
				
				map[i][0].setProcessed(true);
				map[i][0].addColor(colorid);
				map[(i + 1) % map.length][0].setProcessed(true);
				map[(i + 1) % map.length][0].addColor(colorid);
				map[i][1].setProcessed(true);
				map[i][1].addColor(colorid);
				map[(i + 1) % map.length][1].setProcessed(true);
				map[(i + 1) % map.length][1].addColor(colorid);
				map[i][2].setProcessed(true);
				map[i][2].addColor(colorid);
				map[(i + 1) % map.length][2].setProcessed(true);
				map[(i + 1) % map.length][2].addColor(colorid);
				map[i][3].setProcessed(true);
				map[i][3].addColor(colorid);
				map[(i + 1) % map.length][3].setProcessed(true);
				map[(i + 1) % map.length][3].addColor(colorid);
				
				result.add(new KMapGroup(term, colorid));
				colorid++;
				
			}

		}

	}

	public void check8v2() { // 8li yukardan aþaðý
		

		for (int i = 0; i < map[0].length; i++) {

			boolean isResult = false;
			boolean isNecessary = false;

			if ((map[0][i].getValue() == 1 || map[0][i].getValue() == -1)
					&& (map[0][(i + 1) % map[0].length].getValue() == 1 || map[0][(i + 1) % map[0].length].getValue() == -1)
					&& (map[1][i].getValue() == 1 || map[1][i].getValue() == -1)
					&& (map[1][(i + 1) % map[0].length].getValue() == 1 || map[i][(i + 1) % map[0].length].getValue() == -1)
					&& (map[2][i].getValue() == 1 || map[2][i].getValue() == -1)
					&& (map[2][(i + 1) % map[0].length].getValue() == 1 || map[2][(i + 1) % map[0].length].getValue() == -1)
					&& (map[3][i].getValue() == 1 || map[3][i].getValue() == -1)
					&& (map[3][(i + 1) % map[0].length].getValue() == 1 || map[3][(i + 1) % map[0].length].getValue() == -1)) {

				if (map[0][i].isProcessed() == false || map[0][(i + 1) % map[0].length].isProcessed() == false
						|| map[1][i].isProcessed() == false || map[1][(i + 1) % map[0].length].isProcessed() == false
						|| map[2][i].isProcessed() == false || map[2][(i + 1) % map[0].length].isProcessed() == false
						|| map[3][i].isProcessed() == false || map[3][(i + 1) % map[0].length].isProcessed() == false) {

					isResult = true;

					if ((map[0][i].isProcessed() == false && map[0][i].getValue() == 1) || (map[1][i].isProcessed() == false && map[1][i].getValue() == 1)
							|| (map[2][i].isProcessed() == false && map[2][i].getValue() == 1) || (map[3][i].isProcessed() == false && map[3][i].getValue() == 1)
							|| (map[0][(i + 1) % map[0].length].isProcessed() == false && map[0][(i + 1) % map[0].length].getValue() == 1) || (map[1][(i + 1) % map[0].length].isProcessed() == false && map[1][(i + 1) % map[0].length].getValue() == 1)
							|| (map[2][(i + 1) % map[0].length].isProcessed() == false && map[2][(i + 1) % map[0].length].getValue() == 1) || (map[3][(i + 1) % map[0].length].isProcessed() == false && map[3][(i + 1) % map[0].length].getValue() == 1)) {
						isNecessary = true;
					}

					if (map[0][i].getValue() == -1 && map[1][i].getValue() == -1 && map[2][i].getValue() == -1
							&& map[3][i].getValue() == -1 && map[0][(i + 1) % map[0].length].getValue() == -1
							&& map[1][(i + 1) % map[0].length].getValue() == -1 && map[2][(i + 1) % map[0].length].getValue() == -1
							&& map[3][(i + 1) % map[0].length].getValue() == -1) {
						isNecessary = false;
					}

				}

			}
			
			if(isResult && isNecessary){			
				String term = "";

				String[] str1 = row.get(i).split("[.]");
				String[] str2 = row.get((i + 1) % map[0].length).split("[.]");
				
				if(str1[0].equals(str2[0])){
					term = term + str1[0];
				}
				else if(str1[1].equals(str2[1])){
					term = term + str1[1];
				}
				
				map[0][i].setProcessed(true);
				map[0][(i + 1) % map[0].length].setProcessed(true);
				map[1][i].setProcessed(true);
				map[1][(i + 1) % map[0].length].setProcessed(true);
				map[2][i].setProcessed(true);
				map[2][(i + 1) % map[0].length].setProcessed(true);
				map[3][i].setProcessed(true);
				map[3][(i + 1) % map[0].length].setProcessed(true);
				
				map[0][i].addColor(colorid);
				map[0][(i + 1) % map[0].length].addColor(colorid);
				map[1][i].addColor(colorid);;
				map[1][(i + 1) % map[0].length].addColor(colorid);;
				map[2][i].addColor(colorid);
				map[2][(i + 1) % map[0].length].addColor(colorid);;
				map[3][i].addColor(colorid);
				map[3][(i + 1) % map[0].length].addColor(colorid);
				
				result.add(new KMapGroup(term, colorid));
				colorid++;
			}

		}

		
	}

	public void check4v1() { // kare 4lü

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				
				boolean isResult = false;
				boolean isNecessary = false;
				
				if ((map[i][j].getValue() == 1 || map[i][j].getValue() == -1)
						&& (map[(i + 1) % map.length][j].getValue() == 1 || map[(i + 1) % map.length][j].getValue() == -1)
						&& (map[i][(j + 1) % map[i].length].getValue() == 1 || map[i][(j + 1) % map[i].length].getValue() == -1)
						&& (map[(i + 1) % map.length][(j + 1) % map[i].length].getValue() == 1 || map[(i + 1) % map.length][(j + 1) % map[i].length].getValue() == -1)) {

					if (map[i][j].isProcessed() == false || map[(i + 1) % map.length][j].isProcessed() == false
							|| map[i][(j + 1) % map[i].length].isProcessed() == false
							|| map[(i + 1) % map.length][(j + 1) % map[i].length].isProcessed() == false) {
						
						isResult = true;

						if ((map[i][j].isProcessed() == false && map[i][j].getValue() == 1) ||
								(map[(i + 1) % map.length][j].isProcessed() == false && map[(i + 1) % map.length][j].getValue() == 1)
								|| (map[i][(j + 1) % map[i].length].isProcessed() == false && map[i][(j + 1) % map[i].length].getValue() == 1)
								|| (map[(i + 1) % map.length][(j + 1) % map[i].length].isProcessed() == false && map[(i + 1) % map.length][(j + 1) % map[i].length].getValue() == 1)) {
							isNecessary = true;						
						}
						
						if (map[i][j].getValue() == -1 && map[(i + 1) % map.length][j].getValue() == -1
								&& map[i][(j + 1) % map[i].length].getValue() == -1
								&& map[(i + 1) % map.length][(j + 1) % map[i].length].getValue() == -1) {
							isNecessary = false;						
						}

					}
				}
				
				if(isResult && isNecessary){
								
					String term = "";

					String[] str1 = column.get(i).split("[.]");
					String[] str2 = column.get((i + 1) % map.length).split("[.]");
					
					if(str1[0].equals(str2[0])){
						term = term + str1[0] + ".";
					}
					else if(str1[1].equals(str2[1])){
						term = term + str1[1] + ".";
					}
					
					str1 = row.get(j).split("[.]");
					str2 = row.get((j + 1) % map[i].length).split("[.]");
					
					
					if (row.size() == 4) {
						if (str1[0].equals(str2[0])) {
							term = term + str1[0];
						} 
						else if (str1[1].equals(str2[1])) {
							term = term + str1[1];
						}
					}
					else
						term = term.substring(0, term.length() - 1);
					
					map[i][j].setProcessed(true);
					map[(i + 1) % map.length][j].setProcessed(true);
					map[i][(j + 1) % map[i].length].setProcessed(true);
					map[(i + 1) % map.length][(j + 1) % map[i].length].setProcessed(true);
					
					map[i][j].addColor(colorid);
					map[(i + 1) % map.length][j].addColor(colorid);
					map[i][(j + 1) % map[i].length].addColor(colorid);
					map[(i + 1) % map.length][(j + 1) % map[i].length].addColor(colorid);
					
					result.add(new KMapGroup(term, colorid));
					colorid++;
				}
				
			}
		}

	}
		
	public void check4v2() { // soldan saða 4lü

		for (int i = 0; i < map.length; i++) {

			boolean isResult = false;
			boolean isNecessary = false;
			
			if ((map[i][0].getValue() == 1 || map[i][0].getValue() == -1)
					&& (map[i][1].getValue() == 1 || map[i][1].getValue() == -1)
					&& (map[i][2].getValue() == 1 || map[i][2].getValue() == -1)
					&& (map[i][3].getValue() == 1 || map[i][3].getValue() == -1)) {

				if (map[i][0].isProcessed() == false || map[i][1].isProcessed() == false
						|| map[i][2].isProcessed() == false || map[i][3].isProcessed() == false) {

					isResult = true;
					
					if ((map[i][0].isProcessed() == false && map[i][0].getValue() == 1) 
							|| (map[i][1].isProcessed() == false && map[i][1].getValue() == 1) 
							|| (map[i][2].isProcessed() == false && map[i][2].getValue() == 1) 
							|| (map[i][3].isProcessed() == false && map[i][3].getValue() == 1)) {
						isNecessary = true;
					}
					
					if (map[i][0].getValue() == -1 && map[i][1].getValue() == -1
							&& map[i][2].getValue() == -1 && map[i][3].getValue() == -1) {
						isNecessary = false;
					}

				}
			}

			if (isResult && isNecessary) {
				
				map[i][0].setProcessed(true);
				map[i][1].setProcessed(true);
				map[i][2].setProcessed(true);
				map[i][3].setProcessed(true);
				
				map[i][0].addColor(colorid);
				map[i][1].addColor(colorid);
				map[i][2].addColor(colorid);
				map[i][3].addColor(colorid);
				
				result.add(new KMapGroup(column.get(i), colorid));
				colorid++;
			}
		}
	}
	
	
	public void check4v3() { // yukardan aþaðý 4lü

		for (int i = 0; i < map[0].length; i++) {

			boolean isResult = false;
			boolean isNecessary = false;

			if ((map[0][i].getValue() == 1 || map[0][i].getValue() == -1)
					&& (map[1][i].getValue() == 1 || map[1][i].getValue() == -1)
					&& (map[2][i].getValue() == 1 || map[2][i].getValue() == -1)
					&& (map[3][i].getValue() == 1 || map[3][i].getValue() == -1)) {

				if (map[0][i].isProcessed() == false || map[1][i].isProcessed() == false
						|| map[2][i].isProcessed() == false || map[3][i].isProcessed() == false) {

					isResult = true;

					if ((map[0][i].isProcessed() == false && map[0][i].getValue() == 1) 
							|| (map[1][i].isProcessed() == false && map[1][i].getValue() == 1)
							|| (map[2][i].isProcessed() == false && map[2][i].getValue() == 1)
							|| (map[3][i].isProcessed() == false && map[3][i].getValue() == 1)) {
						isNecessary = true;
					}

					if (map[0][i].getValue() == -1 && map[1][i].getValue() == -1 && map[2][i].getValue() == -1
							&& map[3][i].getValue() == -1) {
						isNecessary = false;
					}

				}

				if (isResult && isNecessary) {
					map[0][i].setProcessed(true);
					map[1][i].setProcessed(true);
					map[2][i].setProcessed(true);
					map[3][i].setProcessed(true);
					
					map[0][i].addColor(colorid);;
					map[1][i].addColor(colorid);
					map[2][i].addColor(colorid);
					map[3][i].addColor(colorid);
					
					result.add(new KMapGroup(row.get(i), colorid));
					colorid++;
				}

			}
		}
	}
	

	public void check2v1() { // yana doðru 2

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {

				boolean isResult = false;
				boolean isNecessary = false;

				if ((map[i][j].getValue() == 1 || map[i][j].getValue() == -1)
						&& (map[i][(j + 1) % map[i].length].getValue() == 1
								|| map[i][(j + 1) % map[i].length].getValue() == -1)) {

					if (map[i][j].isProcessed() == false || map[i][(j + 1) % map[i].length].isProcessed() == false) {

						isResult = true;

						if ((map[i][j].isProcessed() == false && map[i][j].getValue() == 1)
								|| (map[i][(j + 1) % map[i].length].isProcessed() == false
										&& map[i][(j + 1) % map[i].length].getValue() == 1)) {
							isNecessary = true;
						}

						if (map[i][j].getValue() == -1 && map[i][(j + 1) % map[i].length].getValue() == -1) {
							isNecessary = false;
						}

					}

				}
				
				if (isResult && isNecessary) {
					map[i][j].setProcessed(true);
					map[i][(j + 1) % map[i].length].setProcessed(true);
					
					map[i][j].addColor(colorid);
					map[i][(j + 1) % map[i].length].addColor(colorid);
					
					if(variableCount == 2 || variableCount == 3){
						result.add(new KMapGroup(column.get(i), colorid));
						colorid++;
					}
					else if(variableCount == 4){
						String[] temp1 = row.get(j).split("[.]");
						String[] temp2 = row.get((j + 1) % map[i].length).split("[.]");
						String term = "";
						
						if(temp1[0].equals(temp2[0])){
							term = term + temp1[0];
						}
						else if(temp1[1].equals(temp2[1])){
							term = term + temp1[1];
						}
						
						result.add(new KMapGroup(column.get(i) + "." + term, colorid));
						colorid++;
						}
				}
				

				
			}
		}

	}
	
	
	public void check2v2() { // aþaðý doðru 2
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {

				boolean isResult = false;
				boolean isNecessary = false;

				if ((map[i][j].getValue() == 1 || map[i][j].getValue() == -1)
						&& (map[(i + 1) % map.length][j].getValue() == 1
								|| map[(i + 1) % map.length][j].getValue() == -1)) {

					if (map[i][j].isProcessed() == false || map[(i + 1) % map.length][j].isProcessed() == false) {

						isResult = true;

						if ((map[i][j].isProcessed() == false && map[i][j].getValue() == 1) || 
								(map[(i + 1) % map.length][j].isProcessed() == false && map[(i + 1) % map.length][j].getValue() == 1)) {
							isNecessary = true;
						}

						if (map[i][j].getValue() == -1 && map[(i + 1) % map.length][j].getValue() == -1) {
							isNecessary = false;
						}

					}

				}
				
				if (isResult && isNecessary) {
					map[i][j].setProcessed(true);
					map[(i + 1) % map.length][j].setProcessed(true);
					
					map[i][j].addColor(colorid);
					map[(i + 1) % map.length][j].addColor(colorid);
					
					if(variableCount == 2 ){
						result.add(new KMapGroup(row.get(j), colorid));
						colorid++;
					}
					else if(variableCount == 4 || variableCount == 3){
						String[] temp1 = column.get(i).split("[.]");
						String[] temp2 = column.get((i + 1) % map.length).split("[.]");
						String term = "";
						
						if(temp1[0].equals(temp2[0])){
							term = term + temp1[0];
						}
						else if(temp1[1].equals(temp2[1])){
							term = term + temp1[1];
						}
						
						result.add(new KMapGroup(row.get(j) + "." + term, colorid));
						colorid++;
						}
					
				}
				
			}
		}
	}

	
	public void check1() {

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				
				boolean isResult = false;
				
				if(map[i][j].getValue() == 1 && map[i][j].isProcessed() == false){
					isResult = true;
				}
				
				if (isResult){
					map[i][j].setProcessed(true);
					map[i][j].addColor(colorid);
					result.add(new KMapGroup(column.get(i) + "." + row.get(j), colorid));
					colorid++;
				}
				
			}
		}
		
	}

	public void result(){
		simpleStatement = "F = ";
		
		for (int i = 0; i < result.size(); i++) {
			simpleStatement = simpleStatement + result.get(i).getSubState() + " + ";
		}
		simpleStatement = simpleStatement.substring(0, simpleStatement.length() - 3);
		
	}
	
	public ArrayList<KMapGroup> getResult() {
		return result;
	}

	public void setResult(ArrayList<KMapGroup> result) {
		this.result = result;
	}

	public String getSimpleStatement() {
		return simpleStatement;
	}

	public void setSimpleStatement(String simpleStatement) {
		this.simpleStatement = simpleStatement;
	}

	public int getVariableCount() {
		return variableCount;
	}

	public void setVariableCount(int variableCount) {
		this.variableCount = variableCount;
	}

	public String[] getVariables() {
		return variables;
	}

	public void setVariables(String[] variables) {
		this.variables = variables;
	}

	
	public KMapNode[][] getMap() {
		return map;
	}

	public void setMap(KMapNode[][] map) {
		this.map = map;
	}

	public ArrayList<String> getDontcares() {
		return dontcares;
	}

	public void setDontcares(ArrayList<String> dontcares) {
		this.dontcares = dontcares;
	}	
	

}
