package com.guidemeoh.FitnessCentre;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import dao.GenericDAO;
import entities.Members;
import entities.SmithFitnessCentre;

@Path("smithfitnesscentre")
public class SmithFitnessCentreService {
	
	 private GenericDAO dao = new GenericDAO();
	
	 // COPIED FROM MEMBERSSERVICE 
	 
	    @POST
	    @Path("/members")
	    @Consumes("application/json") // client sees in json 
	    @Produces("application/json") // server displays json to client 
	    public SmithFitnessCentre addMember(Members member) {
	    	
	    	SmithFitnessCentre sfc = dao.find(SmithFitnessCentre.class, 1);
	    	if (sfc == null) return null;// find the centre with id 1 and store it in sfc if none exist returnn null
	    	
	    	SmithFitnessCentre managedCentre = dao.merge(sfc); // reattach sfc to managed state
	    	
	    	managedCentre.getMembers().add(member); // add the new member to the centre member list 
	        dao.merge(managedCentre); // update centre so hibernate  saves the new link and mmeber to create join table 

	        return managedCentre;
	    }
	    
	    @DELETE
	    @Path("/members/{id}")
	    @Produces("application/json")
	    public SmithFitnessCentre deleteMember(@PathParam("id") int memberId) {
	    	
	    	SmithFitnessCentre sfc = dao.find(SmithFitnessCentre.class, 1);
	    	if (sfc == null ) return null;
	    	// find the member inside the list and remove it
	        Members toRemove = null;
	        for (Members m : sfc.getMembers()) { // for each member (m) inside the list sfc.getMembers
	            if (m.getId() == memberId) { // get member by id 
	                toRemove = m; // delete the member 
	                break;
	            }
	        }

	        if (toRemove != null) {
	            sfc.getMembers().remove(toRemove);  // remove from the list
	            dao.merge(sfc);                     // hibernate removes from join table and deletes member
	        }
	        
	        return sfc;
	    }

}
