package entities;

import javax.persistence.CascadeType;

//COPY AND PASTED FROM PREVIOUS CA ATTEMPT COMMUNITYFITNESS CENTRE 
//https://github.com/MarkSmith657/CommunityFitnessCentre

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@Entity 
@XmlRootElement(name = "members") //  allows this class to be converted to XML
public class Members {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	
	private String name;
	private String membershipId;
	private int phoneNumber;
	private String address;
	private String fitnessGoals;
	
	@ManyToOne(fetch = FetchType.EAGER)// many members can have one plan and we define this in the member class as it makes more sense to have it here then in the membershiplan class
	// want to make sure its only unidirectional 
	private MembershipPlan membershipPlan;
	// changed both fetch types to Eager in order to load membership plans and payments fully 
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true ) // make sense for members to have payments and let hibernate manage the join tables 
	// added cascade to delete payments from members_payments table, also added oprhan removal = true so when a member ( parent ) is deleted all its payments ( children are to )
    private List<Payments> payments = new ArrayList<>();
	
	public Members() {}
	
	 public Members(String name, String membershipId, int phoneNumber, String address, String fitnessGoals, MembershipPlan membershipPlan) {
	        this.name = name;
	        this.membershipId = membershipId;
	        this.phoneNumber = phoneNumber;
	        this.address = address;
	        this.fitnessGoals = fitnessGoals;
	        this.membershipPlan = membershipPlan;
	    }
	 
	 public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getMembershipId() {
	        return membershipId;
	    }

	    public void setMembershipId(String membershipId) {
	        this.membershipId = membershipId;
	    }

	    public int getPhoneNumber() {
	        return phoneNumber;
	    }

	    public void setPhoneNumber(int phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public String getFitnessGoals() {
	        return fitnessGoals;
	    }

	    public void setFitnessGoal(String fitnessGoal) {
	        this.fitnessGoals = fitnessGoal;
	    }

	    public MembershipPlan getMembershipPlan() {
	        return membershipPlan;
	    }

	    public void setMembershipPlan(MembershipPlan membershipPlan) {
	        this.membershipPlan = membershipPlan;
	    }
	    
	    public List<Payments> getPayments() {
	        return payments;
	    }

	    public void setPayments(List<Payments> payments) {
	        this.payments = payments;
	    }
	    
	}