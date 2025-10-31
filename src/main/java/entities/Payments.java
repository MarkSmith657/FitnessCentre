package entities;

//COPY AND PASTED FROM PREVIOUS CA ATTEMPT COMMUNITYFITNESS CENTRE 
//https://github.com/MarkSmith657/CommunityFitnessCentre

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity 
@XmlRootElement(name = "payments")
public class Payments {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	private String paymentDate;
	private double paymentAmount;
	
	public Payments() {}
	
	public Payments(String paymentDate, double paymentAmount) {
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}


	}