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
import modelo.entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Ctaahorro;
import modelo.entidades.Transaccion;

/**
 *
 * @author EddyA
 */
public class CtaahorroJpaController implements Serializable {

    public CtaahorroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ctaahorro ctaahorro) {
        if (ctaahorro.getClienteList() == null) {
            ctaahorro.setClienteList(new ArrayList<Cliente>());
        }
        if (ctaahorro.getTransaccionList() == null) {
            ctaahorro.setTransaccionList(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal codsu = ctaahorro.getCodsu();
            if (codsu != null) {
                codsu = em.getReference(codsu.getClass(), codsu.getCodsu());
                ctaahorro.setCodsu(codsu);
            }
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : ctaahorro.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCodcl());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            ctaahorro.setClienteList(attachedClienteList);
            List<Transaccion> attachedTransaccionList = new ArrayList<Transaccion>();
            for (Transaccion transaccionListTransaccionToAttach : ctaahorro.getTransaccionList()) {
                transaccionListTransaccionToAttach = em.getReference(transaccionListTransaccionToAttach.getClass(), transaccionListTransaccionToAttach.getIdtransaccion());
                attachedTransaccionList.add(transaccionListTransaccionToAttach);
            }
            ctaahorro.setTransaccionList(attachedTransaccionList);
            em.persist(ctaahorro);
            if (codsu != null) {
                codsu.getCtaahorroList().add(ctaahorro);
                codsu = em.merge(codsu);
            }
            for (Cliente clienteListCliente : ctaahorro.getClienteList()) {
                clienteListCliente.getCtaahorroList().add(ctaahorro);
                clienteListCliente = em.merge(clienteListCliente);
            }
            for (Transaccion transaccionListTransaccion : ctaahorro.getTransaccionList()) {
                Ctaahorro oldCodctaOfTransaccionListTransaccion = transaccionListTransaccion.getCodcta();
                transaccionListTransaccion.setCodcta(ctaahorro);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
                if (oldCodctaOfTransaccionListTransaccion != null) {
                    oldCodctaOfTransaccionListTransaccion.getTransaccionList().remove(transaccionListTransaccion);
                    oldCodctaOfTransaccionListTransaccion = em.merge(oldCodctaOfTransaccionListTransaccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ctaahorro ctaahorro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ctaahorro persistentCtaahorro = em.find(Ctaahorro.class, ctaahorro.getCodcta());
            Sucursal codsuOld = persistentCtaahorro.getCodsu();
            Sucursal codsuNew = ctaahorro.getCodsu();
            List<Cliente> clienteListOld = persistentCtaahorro.getClienteList();
            List<Cliente> clienteListNew = ctaahorro.getClienteList();
            List<Transaccion> transaccionListOld = persistentCtaahorro.getTransaccionList();
            List<Transaccion> transaccionListNew = ctaahorro.getTransaccionList();
            if (codsuNew != null) {
                codsuNew = em.getReference(codsuNew.getClass(), codsuNew.getCodsu());
                ctaahorro.setCodsu(codsuNew);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCodcl());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            ctaahorro.setClienteList(clienteListNew);
            List<Transaccion> attachedTransaccionListNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionListNewTransaccionToAttach : transaccionListNew) {
                transaccionListNewTransaccionToAttach = em.getReference(transaccionListNewTransaccionToAttach.getClass(), transaccionListNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionListNew.add(transaccionListNewTransaccionToAttach);
            }
            transaccionListNew = attachedTransaccionListNew;
            ctaahorro.setTransaccionList(transaccionListNew);
            ctaahorro = em.merge(ctaahorro);
            if (codsuOld != null && !codsuOld.equals(codsuNew)) {
                codsuOld.getCtaahorroList().remove(ctaahorro);
                codsuOld = em.merge(codsuOld);
            }
            if (codsuNew != null && !codsuNew.equals(codsuOld)) {
                codsuNew.getCtaahorroList().add(ctaahorro);
                codsuNew = em.merge(codsuNew);
            }
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    clienteListOldCliente.getCtaahorroList().remove(ctaahorro);
                    clienteListOldCliente = em.merge(clienteListOldCliente);
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    clienteListNewCliente.getCtaahorroList().add(ctaahorro);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                }
            }
            for (Transaccion transaccionListOldTransaccion : transaccionListOld) {
                if (!transaccionListNew.contains(transaccionListOldTransaccion)) {
                    transaccionListOldTransaccion.setCodcta(null);
                    transaccionListOldTransaccion = em.merge(transaccionListOldTransaccion);
                }
            }
            for (Transaccion transaccionListNewTransaccion : transaccionListNew) {
                if (!transaccionListOld.contains(transaccionListNewTransaccion)) {
                    Ctaahorro oldCodctaOfTransaccionListNewTransaccion = transaccionListNewTransaccion.getCodcta();
                    transaccionListNewTransaccion.setCodcta(ctaahorro);
                    transaccionListNewTransaccion = em.merge(transaccionListNewTransaccion);
                    if (oldCodctaOfTransaccionListNewTransaccion != null && !oldCodctaOfTransaccionListNewTransaccion.equals(ctaahorro)) {
                        oldCodctaOfTransaccionListNewTransaccion.getTransaccionList().remove(transaccionListNewTransaccion);
                        oldCodctaOfTransaccionListNewTransaccion = em.merge(oldCodctaOfTransaccionListNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ctaahorro.getCodcta();
                if (findCtaahorro(id) == null) {
                    throw new NonexistentEntityException("The ctaahorro with id " + id + " no longer exists.");
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
            Ctaahorro ctaahorro;
            try {
                ctaahorro = em.getReference(Ctaahorro.class, id);
                ctaahorro.getCodcta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ctaahorro with id " + id + " no longer exists.", enfe);
            }
            Sucursal codsu = ctaahorro.getCodsu();
            if (codsu != null) {
                codsu.getCtaahorroList().remove(ctaahorro);
                codsu = em.merge(codsu);
            }
            List<Cliente> clienteList = ctaahorro.getClienteList();
            for (Cliente clienteListCliente : clienteList) {
                clienteListCliente.getCtaahorroList().remove(ctaahorro);
                clienteListCliente = em.merge(clienteListCliente);
            }
            List<Transaccion> transaccionList = ctaahorro.getTransaccionList();
            for (Transaccion transaccionListTransaccion : transaccionList) {
                transaccionListTransaccion.setCodcta(null);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
            }
            em.remove(ctaahorro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ctaahorro> findCtaahorroEntities() {
        return findCtaahorroEntities(true, -1, -1);
    }

    public List<Ctaahorro> findCtaahorroEntities(int maxResults, int firstResult) {
        return findCtaahorroEntities(false, maxResults, firstResult);
    }

    private List<Ctaahorro> findCtaahorroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ctaahorro.class));
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

    public Ctaahorro findCtaahorro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ctaahorro.class, id);
        } finally {
            em.close();
        }
    }

    public int getCtaahorroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ctaahorro> rt = cq.from(Ctaahorro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
