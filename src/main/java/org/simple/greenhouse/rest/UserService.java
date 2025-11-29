package org.simple.greenhouse.rest;

import jakarta.inject.Inject;                            
import jakarta.transaction.Transactional;               
import jakarta.ws.rs.*;                                                      
import jakarta.ws.rs.core.Response;                     

import org.simple.greenhouse.dao.UserDao;              
import org.simple.greenhouse.entities.Users;               

import java.util.List;

@Path("/users") // base path for all users 
@Produces("application/json")
@Consumes("application/json")
public class UserService {
	
	@Inject                                              // quarkus injects our dao here
    UserDao userDao;                                     // dao used to talk to the database

    @POST                                               
    @Path("/register")                                   // endpoint for registering a new user
    @Transactional                                       // db write operation, needs transaction
    public Response register(Users user) {               // user is read from request json body
        userDao.save(user);                              // persist new user using dao
        return Response
                .status(Response.Status.CREATED)         // return created response
                .entity(user)                            // include created user in response body
                .build();

}
    
    @POST
    @Path("/login")
    public Response login(Users requestUser) { // get user username and password from entity user 

        Users user = userDao.findByUsername(requestUser.getUsername());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!user.getPasswordHash().equals(requestUser.getPasswordHash())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(user).build();
    }



    @GET                                                 
    public List<Users> getAllUsers() {                  
        return userDao.findAll();                       
    }

    @GET                                                
    @Path("/{id}")                                       
    public Response getUserById(@PathParam("id") int id) {
        Users user = userDao.findById(id);               // load user by id
        if (user == null) {                          
            return Response.status(Response.Status.NOT_FOUND).build(); 
            }
        return Response.ok(user).build();                
    }

    @PUT                                               
    @Path("/{id}")                                       
    @Transactional                                      
    public Response updateUser(@PathParam("id") int id, Users updated) {          // updated user data from request body
        Users existingUser = userDao.findById(id);           // load existing user
        if (existingUser == null) {                          // if not found return error 
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }

        // update the fields we allow to be changed
        existingUser.setUsername(updated.getUsername());     // update username
        existingUser.setPasswordHash(updated.getPasswordHash()); // update password 

        userDao.update(existingUser);                        // merge changes via dao 
        return Response.ok(existingUser).build();           
    }

    @DELETE                                              
    @Path("/{id}")                                      
    @Transactional                                       
    public Response deleteUser(@PathParam("id") int id) {
        Users user = userDao.findById(id);               // find user by id
        if (user == null) {                              // if not found return error
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
        userDao.delete(user);                            // remove user via dao
        return Response.noContent().build();             
    }

}
