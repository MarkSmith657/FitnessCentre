package org.simple.greenhouse.dao;

 // CDI scope annotation from Quarkus and marks this class as a bean
 // whose single instance will live for the whole application
import jakarta.enterprise.context.ApplicationScoped;

// CDI annotation that tells Quarkus to inject dependencies for us.
import jakarta.inject.Inject;

// JPA interface used to interact with the persistence context and database.
// replaces thre need for manually creating EntityManagerFactory and EntityManager.
import jakarta.persistence.EntityManager;

// Jakarta annotation that tells Quarkus to wrap the method in a db
// transaction: begin, commit, and rollback on error.
import jakarta.transaction.Transactional;

import java.util.List;

// this class will provide basdic crud operations for any entity 
// we just inject the EM instead of creating it ourselves 
@ApplicationScoped
public class GenericDAO {
	
	@Inject // injects em and creates reference em
	EntityManager em;
	
	public GenericDAO() {}
	
	
     // The @Transactional annotation means Quarkus starts and commits a transaction for us
     
    @Transactional
    public void persist(Object o) {
        em.persist(o);           // Put the new entity 'o' into the persistence context and mark it to be inserted
    }

    @Transactional
    public void remove(Object o) {
        Object managed = o;      // Start by assuming the passed object might already be managed
        if (!em.contains(o)) {   // If the EntityManager does NOT currently manage this instance
            // we merge it to get a managed copy attached to the current persistence context
            managed = em.merge(o);
        }
        em.remove(managed);      
    }


    @Transactional
    public <T> T merge(T o) { // generic entity t allows this method to work with any class
        // returns the managed instance that is attached to the persistence context.
        return em.merge(o);
    }

   // helps to find user by id ( Dont know if have to use this yet just copying my method from last ca )
    public <T> T find(Class<T> entityClass, Object id) {
        // Use em.find to retrieve an entity by its primary key.
        return em.find(entityClass, id);
    }

   
    public <T> List<T> findAll(Class<T> entityClass) {
        // entityClass.getSimpleName() returns just the class name without the package,
        // which matches the JPA entity name by default.
        String jpql = "FROM " + entityClass.getSimpleName();

        // Create and execute the JPQL query telling JPA the expected result type
        return em.createQuery(jpql, entityClass)
                 .getResultList();   // Fetch all results as a List<T>
    }
}


 