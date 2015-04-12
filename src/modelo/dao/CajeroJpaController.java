/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.entidades.Sucursal;
import modelo.entidades.Transaccion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Cajero;

/**
 *
 * @author EddyA
 */
public class CajeroJpaController implements Serializable {

    public CajeroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cajero cajero) {
        if (cajero.getTransaccionList() == null) {
            cajero.setTransaccionList(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal codsu = cajero.getCodsu();
            if (codsu != null) {
                codsu = em.getReference(codsu.getClass(), codsu.getCodsu());
                cajero.setCodsu(codsu);
            }
            List<Transaccion> attachedTransaccionList = new ArrayList<Transaccion>();
            for (Transaccion transaccionListTransaccionToAttach : cajero.getTransaccionList()) {
                transaccionListTransaccionToAttach = em.getReference(transaccionListTransaccionToAttach.getClass(), transaccionListTransaccionToAttach.getIdtransaccion());
                attachedTransaccionList.add(transaccionListTransaccionToAttach);
            }
            cajero.setTransaccionList(attachedTransaccionList);
            em.persist(cajero);
            if (codsu != null) {
                codsu.getCajeroList().add(cajero);
                codsu = em.merge(codsu);
            }
            for (Transaccion transaccionListTransaccion : cajero.getTransaccionList()) {
                Cajero oldCodcaOfTransaccionListTransaccion = transaccionListTransaccion.getCodca();
                transaccionListTransaccion.setCodca(cajero);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
                if (oldCodcaOfTransaccionListTransaccion != null) {
                    oldCodcaOfTransaccionListTransaccion.getTransaccionList().remove(transaccionListTransaccion);
                    oldCodcaOfTransaccionListTransaccion = em.merge(oldCodcaOfTransaccionListTransaccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cajero cajero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cajero persistentCajero = em.find(Cajero.class, cajero.getCodca());
            Sucursal codsuOld = persistentCajero.getCodsu();
            Sucursal codsuNew = cajero.getCodsu();
            List<Transaccion> transaccionListOld = persistentCajero.getTransaccionList();
            List<Transaccion> transaccionListNew = cajero.getTransaccionList();
            if (codsuNew != null) {
                codsuNew = em.getReference(codsuNew.getClass(), codsuNew.getCodsu());
                cajero.setCodsu(codsuNew);
            }
            List<Transaccion> attachedTransaccionListNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionListNewTransaccionToAttach : transaccionListNew) {
                transaccionListNewTransaccionToAttach = em.getReference(transaccionListNewTransaccionToAttach.getClass(), transaccionListNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionListNew.add(transaccionListNewTransaccionToAttach);
            }
            transaccionListNew = attachedTransaccionListNew;
            cajero.setTransaccionList(transaccionListNew);
            cajero = em.merge(cajero);
            if (codsuOld != null && !codsuOld.equals(codsuNew)) {
                codsuOld.getCajeroList().remove(cajero);
                codsuOld = em.merge(codsuOld);
            }
            if (codsuNew != null && !codsuNew.equals(codsuOld)) {
                codsuNew.getCajeroList().add(cajero);
                codsuNew = em.merge(codsuNew);
            }
            for (Transaccion transaccionListOldTransaccion : transaccionListOld) {
                if (!transaccionListNew.contains(transaccionListOldTransaccion)) {
                    transaccionListOldTransaccion.setCodca(null);
                    transaccionListOldTransaccion = em.merge(transaccionListOldTransaccion);
                }
            }
            for (Transaccion transaccionListNewTransaccion : transaccionListNew) {
                if (!transaccionListOld.contains(transaccionListNewTransaccion)) {
                    Cajero oldCodcaOfTransaccionListNewTransaccion = transaccionListNewTransaccion.getCodca();
                    transaccionListNewTransaccion.setCodca(cajero);
                    transaccionListNewTransaccion = em.merge(transaccionListNewTransaccion);
                    if (oldCodcaOfTransaccionListNewTransaccion != null && !oldCodcaOfTransaccionListNewTransaccion.equals(cajero)) {
                        oldCodcaOfTransaccionListNewTransaccion.getTransaccionList().remove(transaccionListNewTransaccion);
                        oldCodcaOfTransaccionListNewTransaccion = em.merge(oldCodcaOfTransaccionListNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cajero.getCodca();
                if (findCajero(id) == null) {
                    throw new NonexistentEntityException("The cajero with id " + id + " no longer exists.");
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
            Cajero cajero;
            try {
                cajero = em.getReference(Cajero.class, id);
                cajero.getCodca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cajero with id " + id + " no longer exists.", enfe);
            }
            Sucursal codsu = cajero.getCodsu();
            if (codsu != null) {
                codsu.getCajeroList().remove(cajero);
                codsu = em.merge(codsu);
            }
            List<Transaccion> transaccionList = cajero.getTransaccionList();
            for (Transaccion transaccionListTransaccion : transaccionList) {
                transaccionListTransaccion.setCodca(null);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
            }
            em.remove(cajero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cajero> findCajeroEntities() {
        return findCajeroEntities(true, -1, -1);
    }

    public List<Cajero> findCajeroEntities(int maxResults, int firstResult) {
        return findCajeroEntities(false, maxResults, firstResult);
    }

    private List<Cajero> findCajeroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cajero.class));
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

    public Cajero findCajero(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cajero.class, id);
        } finally {
            em.close();
        }
    }

    public int getCajeroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cajero> rt = cq.from(Cajero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
