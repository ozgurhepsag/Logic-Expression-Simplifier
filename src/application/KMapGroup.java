package application;

public class KMapGroup {
	String subState;
	int colorid;
	
	public KMapGroup(String subState, int colorid) {
		super();
		this.subState = subState;
		this.colorid = colorid;
	}

	public String getSubState() {
		return subState;
	}

	public void setSubState(String subState) {
		this.subState = subState;
	}

	public int getColorid() {
		return colorid;
	}

	public void setColorid(int colorid) {
		this.colorid = colorid;
	}

}
