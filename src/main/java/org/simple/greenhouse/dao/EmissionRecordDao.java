package org.simple.greenhouse.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.simple.greenhouse.entities.EmissionRecord;

import java.util.List;

@ApplicationScoped // one instance managed by quarkus for the whole app
public class EmissionRecordDao {

    @Inject
    EntityManager em; // entitymanager injected by quarkus

    @Transactional
    public void save(EmissionRecord record) {
        // persist a new emission record
        em.persist(record);
    }

    public EmissionRecord findById(int id) {
        // find an emission record by primary key
        return em.find(EmissionRecord.class, id);
    }

    public List<EmissionRecord> findAll() {
        // return all emission records
        return em.createQuery("FROM EmissionRecord", EmissionRecord.class)
                 .getResultList();
    }

    @Transactional
    public EmissionRecord update(EmissionRecord record) {
        // merge updated emission record into the persistence context
        return em.merge(record);
    }

    @Transactional
    public void delete(EmissionRecord record) {
        // ensure the entity is managed before removing
        EmissionRecord managed = em.contains(record) ? record : em.merge(record);
        em.remove(managed);
    }

    @Transactional
    public void deleteById(int id) {
        // convenience method to delete by id
        EmissionRecord record = findById(id);
        if (record != null) {
            delete(record);
        }
    }

    public List<EmissionRecord> findByCategory(String categoryCode) {
        // query to find all emissions with a given category code
        TypedQuery<EmissionRecord> query = em.createQuery(
                "SELECT e FROM EmissionRecord e WHERE e.categoryCode = :code",
                EmissionRecord.class
        );
        query.setParameter("code", categoryCode);
        return query.getResultList();
    }

    public List<EmissionRecord> findBySourceType(String sourceType) {
        // e.g. predicted/actual
        TypedQuery<EmissionRecord> query = em.createQuery(
                "SELECT e FROM EmissionRecord e WHERE e.sourceType = :sourceType",
                EmissionRecord.class
        );
        query.setParameter("sourceType", sourceType);
        return query.getResultList();
    }

    public List<EmissionRecord> findApproved() {
        // get all records that are marked as approved
        TypedQuery<EmissionRecord> query = em.createQuery(
                "SELECT e FROM EmissionRecord e WHERE e.approved = true",
                EmissionRecord.class
        );
        return query.getResultList();
    }
}
