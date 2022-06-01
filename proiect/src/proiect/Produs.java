package proiect;

public class Produs {
	private String numeProdus;
	private Double pretMinim;
	private Double pretMaxim;
	
	public Produs() {
	}

	public Produs(String numeProdus, Double pretMinim) {
		this.numeProdus = numeProdus;
		this.pretMinim = pretMinim;
		this.pretMaxim = pretMinim;
	}

	public String getNumeProdus() {
		return numeProdus;
	}

	public void setNumeProdus(String numeProdus) {
		this.numeProdus = numeProdus;
	}

	public Double getPretMinim() {
		return pretMinim;
	}

	public void setPretMinim(Double pretMinim) {
		this.pretMinim = pretMinim;
	}

	public Double getPretMaxim() {
		return pretMaxim;
	}

	public void setPretMaxim(Double pretMaxim) {
		this.pretMaxim = pretMaxim;
	}

	@Override
	public String toString() {
		return "Produs [numeProdus=" + numeProdus + ", pretMinim=" + pretMinim + ", pretMaxim=" + pretMaxim + "]";
	}
	
	
	
}
