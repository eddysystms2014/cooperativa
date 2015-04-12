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
import modelo.entidades.Ctaahorro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Cliente;

/**
 *
 * @author EddyA
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getCtaahorroList() == null) {
            cliente.setCtaahorroList(new ArrayList<Ctaahorro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal codsu = cliente.getCodsu();
            if (codsu != null) {
                codsu = em.getReference(codsu.getClass(), codsu.getCodsu());
                cliente.setCodsu(codsu);
            }
            List<Ctaahorro> attachedCtaahorroList = new ArrayList<Ctaahorro>();
            for (Ctaahorro ctaahorroListCtaahorroToAttach : cliente.getCtaahorroList()) {
                ctaahorroListCtaahorroToAttach = em.getReference(ctaahorroListCtaahorroToAttach.getClass(), ctaahorroListCtaahorroToAttach.getCodcta());
                attachedCtaahorroList.add(ctaahorroListCtaahorroToAttach);
            }
            cliente.setCtaahorroList(attachedCtaahorroList);
            em.persist(cliente);
            if (codsu != null) {
                codsu.getClienteList().add(cliente);
                codsu = em.merge(codsu);
            }
            for (Ctaahorro ctaahorroListCtaahorro : cliente.getCtaahorroList()) {
                ctaahorroListCtaahorro.getClienteList().add(cliente);
                ctaahorroListCtaahorro = em.merge(ctaahorroListCtaahorro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getCodcl());
            Sucursal codsuOld = persistentCliente.getCodsu();
            Sucursal codsuNew = cliente.getCodsu();
            List<Ctaahorro> ctaahorroListOld = persistentCliente.getCtaahorroList();
            List<Ctaahorro> ctaahorroListNew = cliente.getCtaahorroList();
            if (codsuNew != null) {
                codsuNew = em.getReference(codsuNew.getClass(), codsuNew.getCodsu());
                cliente.setCodsu(codsuNew);
            }
            List<Ctaahorro> attachedCtaahorroListNew = new ArrayList<Ctaahorro>();
            for (Ctaahorro ctaahorroListNewCtaahorroToAttach : ctaahorroListNew) {
                ctaahorroListNewCtaahorroToAttach = em.getReference(ctaahorroListNewCtaahorroToAttach.getClass(), ctaahorroListNewCtaahorroToAttach.getCodcta());
                attachedCtaahorroListNew.add(ctaahorroListNewCtaahorroToAttach);
            }
            ctaahorroListNew = attachedCtaahorroListNew;
            cliente.setCtaahorroList(ctaahorroListNew);
            cliente = em.merge(cliente);
            if (codsuOld != null && !codsuOld.equals(codsuNew)) {
                codsuOld.getClienteList().remove(cliente);
                codsuOld = em.merge(codsuOld);
            }
            if (codsuNew != null && !codsuNew.equals(codsuOld)) {
                codsuNew.getClienteList().add(cliente);
                codsuNew = em.merge(codsuNew);
            }
            for (Ctaahorro ctaahorroListOldCtaahorro : ctaahorroListOld) {
                if (!ctaahorroListNew.contains(ctaahorroListOldCtaahorro)) {
                    ctaahorroListOldCtaahorro.getClienteList().remove(cliente);
                    ctaahorroListOldCtaahorro = em.merge(ctaahorroListOldCtaahorro);
                }
            }
            for (Ctaahorro ctaahorroListNewCtaahorro : ctaahorroListNew) {
                if (!ctaahorroListOld.contains(ctaahorroListNewCtaahorro)) {
                    ctaahorroListNewCtaahorro.getClienteList().add(cliente);
                    ctaahorroListNewCtaahorro = em.merge(ctaahorroListNewCtaahorro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getCodcl();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCodcl();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Sucursal codsu = cliente.getCodsu();
            if (codsu != null) {
                codsu.getClienteList().remove(cliente);
                codsu = em.merge(codsu);
            }
            List<Ctaahorro> ctaahorroList = cliente.getCtaahorroList();
            for (Ctaahorro ctaahorroListCtaahorro : ctaahorroList) {
                ctaahorroListCtaahorro.getClienteList().remove(cliente);
                ctaahorroListCtaahorro = em.merge(ctaahorroListCtaahorro);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
