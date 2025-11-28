package org.simple.greenhouse.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.simple.greenhouse.entities.Users;

import java.util.List;

@ApplicationScoped // one instance managed by quarkus for the whole app
public class UserDao {

    @Inject
    EntityManager em; // entitymanager is injected by quarkus (configured via application.properties)

    @Transactional
    public void save(Users Users) {
        // persist a new Users into the database
        em.persist(Users);
    }

    public Users findById(int id) {
        // find a Users by primary key
        return em.find(Users.class, id);
    }

    public List<Users> findAll() {
        // simple jpql query to return all Userss
        return em.createQuery("FROM Users", Users.class)
                 .getResultList();
    }

    @Transactional
    public Users update(Users Users) {
        // merge returns the managed instance with updated state
        return em.merge(Users);
    }

    @Transactional
    public void delete(Users Users) {
        // ensure the entity is managed before removing
        Users managed = em.contains(Users) ? Users : em.merge(Users);
        em.remove(managed);
    }

    @Transactional
    public void deleteById(int id) {
        // convenience method to delete by id
        Users Users = findById(id);
        if (Users != null) {
            delete(Users);
        }
    }

    public Users findByUsersname(String Usersname) {
        // query to find a single Users by Usersname
        TypedQuery<Users> query = em.createQuery(
                "SELECT u FROM Users u WHERE u.Usersname = :Usersname", Users.class
        );
        query.setParameter("Usersname", Usersname);
        List<Users> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}

