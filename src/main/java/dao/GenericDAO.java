package dao;

// COPY AND PASTED FROM PREVIOUS CA ATTEMPT COMMUNITYFITNESS CENTRE 
// https://github.com/MarkSmith657/CommunityFitnessCentre


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


// Generic dao class copied from my JPAAuction site 

public class GenericDAO {

    protected static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("SmithFitnessCentre"); // creation of emf and connecting it to persistence.xml

    public GenericDAO() {}
    
    // crud operations created below of adding removing and merging objects into db 

    public void persist(Object o) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
        } finally {
            em.close(); // after close each object o is detached 
        }
    }
    
    public void remove(Object o) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Object attached = em.merge(o); // attach to persistence context so object can be managed again by em
            em.remove(attached);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    

    public <T> T merge(T o) { // T is a type parametre which allows a method to be generic so it can work with any object 
    	// so if i call update member it will become Member merge and same for payment etc 
        EntityManager em = emf.createEntityManager();
        T mergedEntity; //  declaring variable mergedEntity which is of type T done outisde try block for later use 
        try {
            em.getTransaction().begin();
            mergedEntity = em.merge(o); // storing value of T into mergedEntity variable 
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return mergedEntity;
    }

    
    public <T> List<T> findAll(Class<T> entityClass) {
    	EntityManager em = emf.createEntityManager();
    	try {
    		return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass) // just returns name of class without package eg just returns "Member" 
                    .getResultList();
    	} finally {
    		em.close();
    	}
    }
    
    public <T> T find(Class<T> entityClass, Object id) { // helper used to find memeber by id, can be used for deletion and merge functionality seen in sampleservice
    	EntityManager em = emf.createEntityManager();
    	try {
    		 return em.find(entityClass, id);
        } finally {
            em.close();
        }
    	
    }

	public void close() {
		// TODO Auto-generated method stub
		
	}
    	
    }
    