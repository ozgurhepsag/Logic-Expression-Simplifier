package application;

public class TruthTableRow {

    private String A;
    private String B;
    private String C;
    private String D;
    private String F;

    public TruthTableRow(){
        this.A = "";
        this.B = "";
        this.C = "";
        this.D = "";
        this.F = "";
    } 

    public TruthTableRow(String a, String b, String c, String d, String f) {
		super();
		A = a;
		B = b;
		C = c;
		D = d;
		F = f;
	}

	public TruthTableRow(String a, String b, String c, String f) {
		super();
		A = a;
		B = b;
		C = c;
		F = f;
	}

	public TruthTableRow(String a, String b, String f) {
		super();
		A = a;
		B = b;
		F = f;
	}
	
	public TruthTableRow(String a,String f) {
		super();
		A = a;
		F = f;
	}

	public String getA() {
        return A;
    }

    public void setA(String A) {
        this.A = A;
    }

    public String getB() {
        return B;
    }

    public void setB(String B) {
        this.B = B;
    }

    public String getC() {
        return C;
    }

    public void setC(String C) {
        this.C = C;
    }
    
    public String getD() {
        return D;
    }

    public void setD(String D) {
        this.D = D;
    }
    
    public String getF() {
        return F;
    }

    public void setF(String F) {
        this.F = F;
    }

}