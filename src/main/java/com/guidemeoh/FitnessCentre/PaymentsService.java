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
    public Payments getMemberById(@PathParam("id") int id) {
        return dao.find(Payments.class, id);
    }

    @POST
    @Path("/payments")
    @Consumes("application/json") // client sees in json 
    @Produces("application/json") // server displays json to client 
    public Payments addPayments(Payments payments) {
        dao.persist(payments);  // saves the new payments entity
        return payments;        // returns the saved plan as confirmation
    }

    @PUT
    @Path("/payments/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Payments updateMembershiPlan(@PathParam("id") int id, Payments updated) {
    	Payments existing = dao.find(Payments.class, id); // look up existing record
        if (existing == null) {
       
        }

       existing.setPaymentAmount(updated.getPaymentAmount());
       existing.setPaymentDate(updated.getPaymentDate());

        dao.merge(existing);
        return existing; // returns payments as json
    }

    @DELETE
    @Path("/payments/{id}")
    @Produces("application/json")
    public String deletePayments(@PathParam("id") int id) {
        Payments member = dao.find(Payments.class, id);
        if (member == null) {
          
        }
        dao.remove(member);
        return "Payment deleted successfully.";
    }
}
