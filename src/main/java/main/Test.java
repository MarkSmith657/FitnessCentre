package main;

//COPY AND PASTED FROM PREVIOUS CA ATTEMPT COMMUNITYFITNESS CENTRE 
//https://github.com/MarkSmith657/CommunityFitnessCentre

import dao.GenericDAO;
import entities.SmithFitnessCentre;
import entities.Members;
import entities.MembershipPlan;
import entities.Payments;

import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		
		
		// adds user test user to db 
		GenericDAO dao = new GenericDAO(); 
		
		SmithFitnessCentre centre = new SmithFitnessCentre("Smiths Fitness", new ArrayList<>()); // pass in name and ArrayList
		dao.persist(centre);
        System.out.println("FitnessCentre persisted: " + centre.getFitnessCentreName());
		
		MembershipPlan plan = new MembershipPlan("Annual Gym", 360.00);
		dao.persist(plan);
        System.out.println("MembershipPlan persisted: " + plan.getDescription());
        
        Payments p1 = new Payments ("2025-01-10", 100.00);
        Payments p2 = new Payments ("2025-06-13", 100.00);
        Payments p3 = new Payments ("2025-12-1", 160.00);
        
        dao.persist(p1);
        dao.persist(p2);
        dao.persist(p3);
        System.out.println("Payments persisted.");
        
        List<Payments> bryanPayments = new ArrayList<>();
        bryanPayments.add(p1);
        bryanPayments.add(p2);
        bryanPayments.add(p3);
        
        Members bryan = new Members ("Bryan Nnadi", "FS12345", 87432567, "Address Confidential 123", "Weight Loss", plan);
        bryan.setPayments(bryanPayments);
        
        dao.persist(bryan);
        System.out.println("Members persisted: " + bryan.getName());
        
        centre.getMembers().add(bryan);
        dao.merge(centre);
        System.out.println("Members added to FitnessCentre: " + centre.getFitnessCentreName());
        
        //Merge example if wanted 
        // bryan.setFitnessGoals("test merge");
        dao.close();
        System.out.println("Test complete. All entities persisted successfully.");
	}

}