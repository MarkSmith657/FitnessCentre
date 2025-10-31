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

    @PUT
    @Path("/members/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Members updateMember(@PathParam("id") int id, Members updated) {
        Members existing = dao.find(Members.class, id); // look up existing record
        if (existing == null) {
       
        }

        // Update editable fields from members class
        existing.setName(updated.getName());
        existing.setPhone(updated.getPhoneNumber());
        existing.setAddress(updated.getAddress());
        existing.setFitnessGoal(updated.getFitnessGoals());

        dao.merge(existing);
        return existing; // returns member as json
    }

    @DELETE
    @Path("/members/{id}")
    @Produces("application/json")
    public String deleteMember(@PathParam("id") int id) {
        Members member = dao.find(Members.class, id);
        if (member == null) {
          
        }
        dao.remove(member);
        return "Member deleted successfully.";
    }
}

