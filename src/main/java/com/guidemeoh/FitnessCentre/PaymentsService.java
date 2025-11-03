package com.guidemeoh.FitnessCentre;

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
import entities.MembershipPlan;
import entities.Payments;

@Path("smithfitnesscentre")
public class PaymentsService {
	
    private GenericDAO dao = new GenericDAO(); // create instance of genericDAO in order to use db functionality 

    @GET
    @Path("/payments")
    @Produces("application/json")
    public List<Payments> getAllPayments() { // retrieves all payments from db and stores them in list 
        return dao.findAll(Payments.class);
    }

    @GET
    @Path("/payments/{id}")
    @Produces("application/json")
    public Payments getPaymentsById(@PathParam("id") int id) {
        return dao.find(Payments.class, id);
    }
    
    @PUT
    @Path("/payments/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Payments updatePayments(@PathParam("id") int id, Payments updated) {
    	Payments existing = dao.find(Payments.class, id); // look up existing record
    	if (existing == null) return null;
    	
       // Update editable fields from members class
       // Changes - made sure that if fields are returned null in json they keep already existing values rather than returning null/0
       if(updated.getPaymentAmount() != 0.0) existing.setPaymentAmount(updated.getPaymentAmount());
       if(updated.getPaymentDate() != null) existing.setPaymentDate(updated.getPaymentDate());

        dao.merge(existing);
        return existing; // returns payments as json
    }
    
    
    // DONT USE THESE ANYMORE - CHECK MEMBERSSERVICE FOR NEW METHODS 
    
   /* @POST
    @Path("/payments")
    @Consumes("application/json") // client sees in json 
    @Produces("application/json") // server displays json to client  
    public Payments addPayments(Payments payments) {
        dao.persist(payments);  // saves the new payment entity
        return payments;  // returns the saved payment as confirmation
    } */
    
    /* @DELETE
    @Path("/payments/{id}")
    @Produces("application/json")
    public String deletePayments(@PathParam("id") int id) {
        Payments member = dao.find(Payments.class, id);
        if (member == null) {
          
        }
        dao.remove(member);
        return "Payment deleted successfully.";
    } */
}
