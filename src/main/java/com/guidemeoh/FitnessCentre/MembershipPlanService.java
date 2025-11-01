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

@Path("smithfitnesscentre")
public class MembershipPlanService {
	
    private GenericDAO dao = new GenericDAO(); // create instance of genericDAO in order to use db functionality 

    @GET
    @Path("/membershipplan")
    @Produces("application/json")
    public List<MembershipPlan> getAllPlans() { // gets list of plan from members class 
        return dao.findAll(MembershipPlan.class);
    }

    @GET
    @Path("/membershipplan/{id}")
    @Produces("application/json")
    public MembershipPlan getMemberById(@PathParam("id") int id) {
        return dao.find(MembershipPlan.class, id);
    }

    @POST
    @Path("/membershipplan")
    @Consumes("application/json") // client sees in json 
    @Produces("application/json") // server displays json to client 
    public MembershipPlan addMembershipPlan(MembershipPlan memberplan) {
        dao.persist(memberplan);  // saves the new plan entity
        return memberplan;        // returns the saved plan as confirmation
    }

    @PUT
    @Path("/membershipplan/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public MembershipPlan updateMembershiPlan(@PathParam("id") int id, MembershipPlan updated) {
    	MembershipPlan existing = dao.find(MembershipPlan.class, id); // look up existing record
    	 if (existing == null) return null;
    	 
    	// Update editable fields from members class
        // Changes - made sure that if fields are returned null in json they keep already existing values rather than returning null/0
        if(updated.getDescription() != null )existing.setDescription(updated.getDescription());
        if(updated.getTotalCost() != 0.0 )existing.setTotalCost(updated.getTotalCost());

        dao.merge(existing);
        return existing; // returns plan as json
    }

    @DELETE
    @Path("/membershipplan/{id}")
    @Produces("application/json")
    public String deleteMembershipPlan(@PathParam("id") int id) {
        MembershipPlan member = dao.find(MembershipPlan.class, id);
        if (member == null) {
          
        }
        dao.remove(member);
        return "Membership Plan deleted successfully.";
    }
}