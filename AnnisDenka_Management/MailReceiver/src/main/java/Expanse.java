
public class Expanse {
	
	private String date_driven;
	private String date_payment;
	private String amountToPay;
	
	public Expanse(String date_driven, String date_payment, String amountToPay) {
		this.date_driven = date_driven;
		this.date_payment = date_payment;
		this.amountToPay = amountToPay;
	}

	public String getDate_driven() {
		return date_driven;
	}

	public void setDate_driven(String date_driven) {
		this.date_driven = date_driven;
	}

	public String getDate_payment() {
		return date_payment;
	}

	public void setDate_payment(String date_payment) {
		this.date_payment = date_payment;
	}

	public String getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(String amountToPay) {
		this.amountToPay = amountToPay;
	}
	
	

}
