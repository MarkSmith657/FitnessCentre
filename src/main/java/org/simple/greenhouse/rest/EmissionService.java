
package org.simple.greenhouse.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import org.simple.greenhouse.dao.EmissionRecordDao;     
import org.simple.greenhouse.dao.UserDao;               
import org.simple.greenhouse.entities.EmissionRecord;
import org.simple.greenhouse.entities.Users;

import java.util.List;

@Path("/emissions")                                     // base path for all emission endpoints
@Produces("application/json")
@Consumes("application/json")
public class EmissionService {

    @Inject
    EmissionRecordDao emissionDao;                      // injected dao talks to emissions table

    @Inject
    UserDao userDao;                                    // injected dao talks to users table for emmision approvals 

    @GET
    public List<EmissionRecord> getAllEmissions() {
        return emissionDao.findAll(); // gets all emmisions 
    }

    @GET
    @Path("/{id}")
    public Response getEmissionById(@PathParam("id") int id) { // gets emission based on id and returns error if none found 
        EmissionRecord record = emissionDao.findById(id);
        if (record == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
        return Response.ok(record).build();
    }

    @GET
    @Path("/category/{code}")                           // view emissions by category code
    public List<EmissionRecord> getByCategory(@PathParam("code") String code) { // scan through emission list and get emission by category code 
        return emissionDao.findByCategory(code.trim()); 
    }

    @POST
    @Transactional                                      
    public Response createEmission(EmissionRecord record) { // create emission and save it to db as record 
        emissionDao.save(record);
        return Response
                .status(Response.Status.CREATED)
                .entity(record)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEmission(@PathParam("id") int id, EmissionRecord updated) { // gets emission by id to update it 
        EmissionRecord existing = emissionDao.findById(id); // finds exisitng record 
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // returns error if no emission found 
        }

        // update allowed fields on the existing record
        existing.setYear(updated.getYear());
        existing.setScenario(updated.getScenario());
        existing.setValue(updated.getValue());
        existing.setGasUnit(updated.getGasUnit());
        existing.setCategoryCode(updated.getCategoryCode());
        existing.setCategoryDescription(updated.getCategoryDescription());
        existing.setSourceType(updated.getSourceType());
        existing.setApproved(updated.getApproved());
        emissionDao.update(existing);
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteEmission(@PathParam("id") int id) { // deletes emission by id 
        EmissionRecord existing = emissionDao.findById(id); // uses dao find method to find emission in db
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // if not found return error 
        }
        emissionDao.delete(existing);
        return Response.noContent().build();            
    }

    @POST
    @Path("/{id}/approve")                              // finds approved emissions 
    @Transactional
    public Response approveEmission(@PathParam("id") int emissionId, @QueryParam("userId") int userId) { // get emission id then user 

        EmissionRecord record = emissionDao.findById(emissionId); // make sure it exists 
        if (record == null) { 
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Users user = userDao.findById(userId);
        if (user == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("invalid user id")
                    .build();
        }

        // mark emission as approved 
        record.setApproved("Approved");
        emissionDao.update(record); // update emission record 

        // link emission to user in one-to-many list
        user.getApprovedEmissions().add(record);
        userDao.update(user);

        return Response.ok(record).build();
    }
}
