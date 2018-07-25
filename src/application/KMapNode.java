package application;

import java.util.ArrayList;

public class KMapNode {
	private int value; // -1 -> dont care
	private int indice;
	private ArrayList<Integer> colorid;
	private boolean isProcessed;
	
	public KMapNode(int indice, int colorid) {
		super();
		this.colorid = new ArrayList<>();
		this.indice = indice;
		this.colorid.add(colorid);
		this.value = 0;
		this.isProcessed = false;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public ArrayList getColorid() {
		return colorid;
	}

	public void addColor(int colorid) {
		
		if(this.colorid.get(0) == 0){
			this.colorid.remove(0);
		}
		
		this.colorid.add(colorid);
		
	}
	
}
