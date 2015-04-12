/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Cajero;
import modelo.entidades.Ctaahorro;
import modelo.entidades.Sucursal;
import modelo.entidades.Transaccion;

/**
 *
 * @author EddyA
 */
public class TransaccionJpaController implements Serializable {

    public TransaccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaccion transaccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cajero codca = transaccion.getCodca();
            if (codca != null) {
                codca = em.getReference(codca.getClass(), codca.getCodca());
                transaccion.setCodca(codca);
            }
            Ctaahorro codcta = transaccion.getCodcta();
            if (codcta != null) {
                codcta = em.getReference(codcta.getClass(), codcta.getCodcta());
                transaccion.setCodcta(codcta);
            }
            Sucursal codsu = transaccion.getCodsu();
            if (codsu != null) {
                codsu = em.getReference(codsu.getClass(), codsu.getCodsu());
                transaccion.setCodsu(codsu);
            }
            em.persist(transaccion);
            if (codca != null) {
                codca.getTransaccionList().add(transaccion);
                codca = em.merge(codca);
            }
            if (codcta != null) {
                codcta.getTransaccionList().add(transaccion);
                codcta = em.merge(codcta);
            }
            if (codsu != null) {
                codsu.getTransaccionList().add(transaccion);
                codsu = em.merge(codsu);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaccion transaccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion persistentTransaccion = em.find(Transaccion.class, transaccion.getIdtransaccion());
            Cajero codcaOld = persistentTransaccion.getCodca();
            Cajero codcaNew = transaccion.getCodca();
            Ctaahorro codctaOld = persistentTransaccion.getCodcta();
            Ctaahorro codctaNew = transaccion.getCodcta();
            Sucursal codsuOld = persistentTransaccion.getCodsu();
            Sucursal codsuNew = transaccion.getCodsu();
            if (codcaNew != null) {
                codcaNew = em.getReference(codcaNew.getClass(), codcaNew.getCodca());
                transaccion.setCodca(codcaNew);
            }
            if (codctaNew != null) {
                codctaNew = em.getReference(codctaNew.getClass(), codctaNew.getCodcta());
                transaccion.setCodcta(codctaNew);
            }
            if (codsuNew != null) {
                codsuNew = em.getReference(codsuNew.getClass(), codsuNew.getCodsu());
                transaccion.setCodsu(codsuNew);
            }
            transaccion = em.merge(transaccion);
            if (codcaOld != null && !codcaOld.equals(codcaNew)) {
                codcaOld.getTransaccionList().remove(transaccion);
                codcaOld = em.merge(codcaOld);
            }
            if (codcaNew != null && !codcaNew.equals(codcaOld)) {
                codcaNew.getTransaccionList().add(transaccion);
                codcaNew = em.merge(codcaNew);
            }
            if (codctaOld != null && !codctaOld.equals(codctaNew)) {
                codctaOld.getTransaccionList().remove(transaccion);
                codctaOld = em.merge(codctaOld);
            }
            if (codctaNew != null && !codctaNew.equals(codctaOld)) {
                codctaNew.getTransaccionList().add(transaccion);
                codctaNew = em.merge(codctaNew);
            }
            if (codsuOld != null && !codsuOld.equals(codsuNew)) {
                codsuOld.getTransaccionList().remove(transaccion);
                codsuOld = em.merge(codsuOld);
            }
            if (codsuNew != null && !codsuNew.equals(codsuOld)) {
                codsuNew.getTransaccionList().add(transaccion);
                codsuNew = em.merge(codsuNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaccion.getIdtransaccion();
                if (findTransaccion(id) == null) {
                    throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion transaccion;
            try {
                transaccion = em.getReference(Transaccion.class, id);
                transaccion.getIdtransaccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.", enfe);
            }
            Cajero codca = transaccion.getCodca();
            if (codca != null) {
                codca.getTransaccionList().remove(transaccion);
                codca = em.merge(codca);
            }
            Ctaahorro codcta = transaccion.getCodcta();
            if (codcta != null) {
                codcta.getTransaccionList().remove(transaccion);
                codcta = em.merge(codcta);
            }
            Sucursal codsu = transaccion.getCodsu();
            if (codsu != null) {
                codsu.getTransaccionList().remove(transaccion);
                codsu = em.merge(codsu);
            }
            em.remove(transaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaccion> findTransaccionEntities() {
        return findTransaccionEntities(true, -1, -1);
    }

    public List<Transaccion> findTransaccionEntities(int maxResults, int firstResult) {
        return findTransaccionEntities(false, maxResults, firstResult);
    }

    private List<Transaccion> findTransaccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaccion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Transaccion findTransaccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaccion> rt = cq.from(Transaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
