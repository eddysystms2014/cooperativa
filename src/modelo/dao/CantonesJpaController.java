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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Cantones;

/**
 *
 * @author EddyA
 */
public class CantonesJpaController implements Serializable {

    public CantonesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cantones cantones) {
        if (cantones.getSucursalList() == null) {
            cantones.setSucursalList(new ArrayList<Sucursal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Sucursal> attachedSucursalList = new ArrayList<Sucursal>();
            for (Sucursal sucursalListSucursalToAttach : cantones.getSucursalList()) {
                sucursalListSucursalToAttach = em.getReference(sucursalListSucursalToAttach.getClass(), sucursalListSucursalToAttach.getCodsu());
                attachedSucursalList.add(sucursalListSucursalToAttach);
            }
            cantones.setSucursalList(attachedSucursalList);
            em.persist(cantones);
            for (Sucursal sucursalListSucursal : cantones.getSucursalList()) {
                Cantones oldCodcantonOfSucursalListSucursal = sucursalListSucursal.getCodcanton();
                sucursalListSucursal.setCodcanton(cantones);
                sucursalListSucursal = em.merge(sucursalListSucursal);
                if (oldCodcantonOfSucursalListSucursal != null) {
                    oldCodcantonOfSucursalListSucursal.getSucursalList().remove(sucursalListSucursal);
                    oldCodcantonOfSucursalListSucursal = em.merge(oldCodcantonOfSucursalListSucursal);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cantones cantones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cantones persistentCantones = em.find(Cantones.class, cantones.getCodcanton());
            List<Sucursal> sucursalListOld = persistentCantones.getSucursalList();
            List<Sucursal> sucursalListNew = cantones.getSucursalList();
            List<Sucursal> attachedSucursalListNew = new ArrayList<Sucursal>();
            for (Sucursal sucursalListNewSucursalToAttach : sucursalListNew) {
                sucursalListNewSucursalToAttach = em.getReference(sucursalListNewSucursalToAttach.getClass(), sucursalListNewSucursalToAttach.getCodsu());
                attachedSucursalListNew.add(sucursalListNewSucursalToAttach);
            }
            sucursalListNew = attachedSucursalListNew;
            cantones.setSucursalList(sucursalListNew);
            cantones = em.merge(cantones);
            for (Sucursal sucursalListOldSucursal : sucursalListOld) {
                if (!sucursalListNew.contains(sucursalListOldSucursal)) {
                    sucursalListOldSucursal.setCodcanton(null);
                    sucursalListOldSucursal = em.merge(sucursalListOldSucursal);
                }
            }
            for (Sucursal sucursalListNewSucursal : sucursalListNew) {
                if (!sucursalListOld.contains(sucursalListNewSucursal)) {
                    Cantones oldCodcantonOfSucursalListNewSucursal = sucursalListNewSucursal.getCodcanton();
                    sucursalListNewSucursal.setCodcanton(cantones);
                    sucursalListNewSucursal = em.merge(sucursalListNewSucursal);
                    if (oldCodcantonOfSucursalListNewSucursal != null && !oldCodcantonOfSucursalListNewSucursal.equals(cantones)) {
                        oldCodcantonOfSucursalListNewSucursal.getSucursalList().remove(sucursalListNewSucursal);
                        oldCodcantonOfSucursalListNewSucursal = em.merge(oldCodcantonOfSucursalListNewSucursal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cantones.getCodcanton();
                if (findCantones(id) == null) {
                    throw new NonexistentEntityException("The cantones with id " + id + " no longer exists.");
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
            Cantones cantones;
            try {
                cantones = em.getReference(Cantones.class, id);
                cantones.getCodcanton();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cantones with id " + id + " no longer exists.", enfe);
            }
            List<Sucursal> sucursalList = cantones.getSucursalList();
            for (Sucursal sucursalListSucursal : sucursalList) {
                sucursalListSucursal.setCodcanton(null);
                sucursalListSucursal = em.merge(sucursalListSucursal);
            }
            em.remove(cantones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cantones> findCantonesEntities() {
        return findCantonesEntities(true, -1, -1);
    }

    public List<Cantones> findCantonesEntities(int maxResults, int firstResult) {
        return findCantonesEntities(false, maxResults, firstResult);
    }

    private List<Cantones> findCantonesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cantones.class));
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

    public Cantones findCantones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cantones.class, id);
        } finally {
            em.close();
        }
    }

    public int getCantonesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cantones> rt = cq.from(Cantones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
