package DataObjects;
import java.util.Date;

public class Expanse {
	
	//testing
	private Date date_driven_t;
	private Date date_payment_t;
	
	private String date_driven;
	private String date_payment;
	private String amountToPay;
	
	
	// uses Date, Date, String
	public Expanse(Date date_driven_t, Date date_payment_t, String amountToPay) {
		this.date_driven_t = date_driven_t;
		this.date_payment_t = date_payment_t;
		this.amountToPay = amountToPay;
	}
	
	// uses String, String, String
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

	//TODO toString or toFile
	@Override
	public String toString() {
		return "Expanse [date_driven=" + date_driven + ", date_payment=" + date_payment + ", amountToPay=" + amountToPay
				+ "]";
	}
	
	
	

}
