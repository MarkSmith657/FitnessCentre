package com.guidemeoh.FitnessCentre;

//COPY AND PASTED FROM PREVIOUS CA ATTEMPT COMMUNITYFITNESS CENTRE 
//https://github.com/MarkSmith657/CommunityFitnessCentre

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import dao.GenericDAO;
import entities.Members;
import entities.Payments;

@Path("smithfitnesscentre")
public class MembersService {
	
    private GenericDAO dao = new GenericDAO(); // create instance of genericDAO in order to use db functionality 

    @GET
    @Path("/hello")
    @Produces("application/json")
    public String hello() {
        return "Welcome to the Community Fitness Centre REST API!";
    }

    @GET
    @Path("/members")
    @Produces("application/json")
    public List<Members> getAllMembers() { // gets list of members from members class 
        return dao.findAll(Members.class);
    }

    @GET
    @Path("/members/{id}")
    @Produces("application/json")
    public Members getMemberById(@PathParam("id") int id) {
        return dao.find(Members.class, id);
    }

    @POST
    @Path("/members")
    @Consumes("application/json") // client sees in json 
    @Produces("application/json") // server displays json to client 
    public Members addMember(Members member) {
        dao.persist(member);  // saves the new member entity
        return member;        // returns the saved member as confirmation
    }
    
    @POST
    @Path("/members/{id}/payments")
    @Consumes("application/json")
    @Produces("application/json") // going to use members object here instead of payments so i cann return the fully updated member instead of just a payment
    public Members addPaymentToMember(@PathParam("id") int memberId, Payments newPayment) { // json input is connected to newPayment
        Members member = dao.find(Members.class, memberId); //looks up member by id 
        if (member == null) return null; // if no member is found return null

        dao.persist(newPayment); // save payment and add it to db
        member.getPayments().add(newPayment); // links payment to member table and updates joined table 
        dao.merge(member); // updates memeber to take in new values 

        return member;
    }
    
    @DELETE
    @Path("/members/{memberId}/payments/{paymentId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Members deleteMembersPayment(@PathParam("memberId") int memberId, @PathParam("paymentId") int paymentId) {
    	Members member = dao.find(Members.class, memberId);//looks up member by id
    	Payments payment = dao.find(Payments.class, paymentId); // looks up member by id
    	if (member == null || payment == null ) return null; // if either member or payment id is invalid return null 
    	
    	Payments targetedPayment = null; // create a variable delete payment and set it = null 
    	for (Payments p : member.getPayments()) { // for each payment (p) inside the list member.getPayments 
    		if (p.getId() == paymentId) { // get the payment id associated to the payment 
    			targetedPayment = p; // and delete the payment 
    			break;
    		}
    	}
    	
    	if (targetedPayment !=null ) { // if payment isnt null 
    		member.getPayments().remove(targetedPayment); // get the payment from the member payment list and remove the targeted payment 
    		dao.merge(member); // update the member object 
    	}
    	dao.remove(payment); // remove the payment from Payment 
    	return member; 
    }


    @PUT
    @Path("/members/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Members updateMember(@PathParam("id") int id, Members updated) {
        Members existing = dao.find(Members.class, id); // look up existing record
        if (existing == null) return null;

        // Update editable fields from members class
        // Changes - made sure that if fields are returned null in json they keep already existing values rather than returning null/0
        if(updated.getName() != null) existing.setName(updated.getName());
        if(updated.getPhoneNumber() != 0) existing.setPhoneNumber(updated.getPhoneNumber());
        if(updated.getAddress() !=null ) existing.setAddress(updated.getAddress());
        if(updated.getFitnessGoals() != null )existing.setFitnessGoal(updated.getFitnessGoals());

        dao.merge(existing);
        return existing; // returns member as json
    }

    @DELETE
    @Path("/members/{id}")
    @Produces("application/json")
    public String deleteMember(@PathParam("id") int id) {
        Members member = dao.find(Members.class, id);
        if (member == null) {
        	return "Member with ID " + id + " not found.";
        }
        dao.remove(member);
        return "Member deleted successfully.";
    }
    
    
}

