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
import modelo.entidades.Cantones;
import modelo.entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.entidades.Transaccion;
import modelo.entidades.Ctaahorro;
import modelo.entidades.Cajero;
import modelo.entidades.Sucursal;

/**
 *
 * @author EddyA
 */
public class SucursalJpaController implements Serializable {

    public SucursalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sucursal sucursal) {
        if (sucursal.getClienteList() == null) {
            sucursal.setClienteList(new ArrayList<Cliente>());
        }
        if (sucursal.getTransaccionList() == null) {
            sucursal.setTransaccionList(new ArrayList<Transaccion>());
        }
        if (sucursal.getCtaahorroList() == null) {
            sucursal.setCtaahorroList(new ArrayList<Ctaahorro>());
        }
        if (sucursal.getCajeroList() == null) {
            sucursal.setCajeroList(new ArrayList<Cajero>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cantones codcanton = sucursal.getCodcanton();
            if (codcanton != null) {
                codcanton = em.getReference(codcanton.getClass(), codcanton.getCodcanton());
                sucursal.setCodcanton(codcanton);
            }
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : sucursal.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCodcl());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            sucursal.setClienteList(attachedClienteList);
            List<Transaccion> attachedTransaccionList = new ArrayList<Transaccion>();
            for (Transaccion transaccionListTransaccionToAttach : sucursal.getTransaccionList()) {
                transaccionListTransaccionToAttach = em.getReference(transaccionListTransaccionToAttach.getClass(), transaccionListTransaccionToAttach.getIdtransaccion());
                attachedTransaccionList.add(transaccionListTransaccionToAttach);
            }
            sucursal.setTransaccionList(attachedTransaccionList);
            List<Ctaahorro> attachedCtaahorroList = new ArrayList<Ctaahorro>();
            for (Ctaahorro ctaahorroListCtaahorroToAttach : sucursal.getCtaahorroList()) {
                ctaahorroListCtaahorroToAttach = em.getReference(ctaahorroListCtaahorroToAttach.getClass(), ctaahorroListCtaahorroToAttach.getCodcta());
                attachedCtaahorroList.add(ctaahorroListCtaahorroToAttach);
            }
            sucursal.setCtaahorroList(attachedCtaahorroList);
            List<Cajero> attachedCajeroList = new ArrayList<Cajero>();
            for (Cajero cajeroListCajeroToAttach : sucursal.getCajeroList()) {
                cajeroListCajeroToAttach = em.getReference(cajeroListCajeroToAttach.getClass(), cajeroListCajeroToAttach.getCodca());
                attachedCajeroList.add(cajeroListCajeroToAttach);
            }
            sucursal.setCajeroList(attachedCajeroList);
            em.persist(sucursal);
            if (codcanton != null) {
                codcanton.getSucursalList().add(sucursal);
                codcanton = em.merge(codcanton);
            }
            for (Cliente clienteListCliente : sucursal.getClienteList()) {
                Sucursal oldCodsuOfClienteListCliente = clienteListCliente.getCodsu();
                clienteListCliente.setCodsu(sucursal);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldCodsuOfClienteListCliente != null) {
                    oldCodsuOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldCodsuOfClienteListCliente = em.merge(oldCodsuOfClienteListCliente);
                }
            }
            for (Transaccion transaccionListTransaccion : sucursal.getTransaccionList()) {
                Sucursal oldCodsuOfTransaccionListTransaccion = transaccionListTransaccion.getCodsu();
                transaccionListTransaccion.setCodsu(sucursal);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
                if (oldCodsuOfTransaccionListTransaccion != null) {
                    oldCodsuOfTransaccionListTransaccion.getTransaccionList().remove(transaccionListTransaccion);
                    oldCodsuOfTransaccionListTransaccion = em.merge(oldCodsuOfTransaccionListTransaccion);
                }
            }
            for (Ctaahorro ctaahorroListCtaahorro : sucursal.getCtaahorroList()) {
                Sucursal oldCodsuOfCtaahorroListCtaahorro = ctaahorroListCtaahorro.getCodsu();
                ctaahorroListCtaahorro.setCodsu(sucursal);
                ctaahorroListCtaahorro = em.merge(ctaahorroListCtaahorro);
                if (oldCodsuOfCtaahorroListCtaahorro != null) {
                    oldCodsuOfCtaahorroListCtaahorro.getCtaahorroList().remove(ctaahorroListCtaahorro);
                    oldCodsuOfCtaahorroListCtaahorro = em.merge(oldCodsuOfCtaahorroListCtaahorro);
                }
            }
            for (Cajero cajeroListCajero : sucursal.getCajeroList()) {
                Sucursal oldCodsuOfCajeroListCajero = cajeroListCajero.getCodsu();
                cajeroListCajero.setCodsu(sucursal);
                cajeroListCajero = em.merge(cajeroListCajero);
                if (oldCodsuOfCajeroListCajero != null) {
                    oldCodsuOfCajeroListCajero.getCajeroList().remove(cajeroListCajero);
                    oldCodsuOfCajeroListCajero = em.merge(oldCodsuOfCajeroListCajero);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sucursal sucursal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal persistentSucursal = em.find(Sucursal.class, sucursal.getCodsu());
            Cantones codcantonOld = persistentSucursal.getCodcanton();
            Cantones codcantonNew = sucursal.getCodcanton();
            List<Cliente> clienteListOld = persistentSucursal.getClienteList();
            List<Cliente> clienteListNew = sucursal.getClienteList();
            List<Transaccion> transaccionListOld = persistentSucursal.getTransaccionList();
            List<Transaccion> transaccionListNew = sucursal.getTransaccionList();
            List<Ctaahorro> ctaahorroListOld = persistentSucursal.getCtaahorroList();
            List<Ctaahorro> ctaahorroListNew = sucursal.getCtaahorroList();
            List<Cajero> cajeroListOld = persistentSucursal.getCajeroList();
            List<Cajero> cajeroListNew = sucursal.getCajeroList();
            if (codcantonNew != null) {
                codcantonNew = em.getReference(codcantonNew.getClass(), codcantonNew.getCodcanton());
                sucursal.setCodcanton(codcantonNew);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCodcl());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            sucursal.setClienteList(clienteListNew);
            List<Transaccion> attachedTransaccionListNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionListNewTransaccionToAttach : transaccionListNew) {
                transaccionListNewTransaccionToAttach = em.getReference(transaccionListNewTransaccionToAttach.getClass(), transaccionListNewTransaccionToAttach.getIdtransaccion());
                attachedTransaccionListNew.add(transaccionListNewTransaccionToAttach);
            }
            transaccionListNew = attachedTransaccionListNew;
            sucursal.setTransaccionList(transaccionListNew);
            List<Ctaahorro> attachedCtaahorroListNew = new ArrayList<Ctaahorro>();
            for (Ctaahorro ctaahorroListNewCtaahorroToAttach : ctaahorroListNew) {
                ctaahorroListNewCtaahorroToAttach = em.getReference(ctaahorroListNewCtaahorroToAttach.getClass(), ctaahorroListNewCtaahorroToAttach.getCodcta());
                attachedCtaahorroListNew.add(ctaahorroListNewCtaahorroToAttach);
            }
            ctaahorroListNew = attachedCtaahorroListNew;
            sucursal.setCtaahorroList(ctaahorroListNew);
            List<Cajero> attachedCajeroListNew = new ArrayList<Cajero>();
            for (Cajero cajeroListNewCajeroToAttach : cajeroListNew) {
                cajeroListNewCajeroToAttach = em.getReference(cajeroListNewCajeroToAttach.getClass(), cajeroListNewCajeroToAttach.getCodca());
                attachedCajeroListNew.add(cajeroListNewCajeroToAttach);
            }
            cajeroListNew = attachedCajeroListNew;
            sucursal.setCajeroList(cajeroListNew);
            sucursal = em.merge(sucursal);
            if (codcantonOld != null && !codcantonOld.equals(codcantonNew)) {
                codcantonOld.getSucursalList().remove(sucursal);
                codcantonOld = em.merge(codcantonOld);
            }
            if (codcantonNew != null && !codcantonNew.equals(codcantonOld)) {
                codcantonNew.getSucursalList().add(sucursal);
                codcantonNew = em.merge(codcantonNew);
            }
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    clienteListOldCliente.setCodsu(null);
                    clienteListOldCliente = em.merge(clienteListOldCliente);
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Sucursal oldCodsuOfClienteListNewCliente = clienteListNewCliente.getCodsu();
                    clienteListNewCliente.setCodsu(sucursal);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldCodsuOfClienteListNewCliente != null && !oldCodsuOfClienteListNewCliente.equals(sucursal)) {
                        oldCodsuOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldCodsuOfClienteListNewCliente = em.merge(oldCodsuOfClienteListNewCliente);
                    }
                }
            }
            for (Transaccion transaccionListOldTransaccion : transaccionListOld) {
                if (!transaccionListNew.contains(transaccionListOldTransaccion)) {
                    transaccionListOldTransaccion.setCodsu(null);
                    transaccionListOldTransaccion = em.merge(transaccionListOldTransaccion);
                }
            }
            for (Transaccion transaccionListNewTransaccion : transaccionListNew) {
                if (!transaccionListOld.contains(transaccionListNewTransaccion)) {
                    Sucursal oldCodsuOfTransaccionListNewTransaccion = transaccionListNewTransaccion.getCodsu();
                    transaccionListNewTransaccion.setCodsu(sucursal);
                    transaccionListNewTransaccion = em.merge(transaccionListNewTransaccion);
                    if (oldCodsuOfTransaccionListNewTransaccion != null && !oldCodsuOfTransaccionListNewTransaccion.equals(sucursal)) {
                        oldCodsuOfTransaccionListNewTransaccion.getTransaccionList().remove(transaccionListNewTransaccion);
                        oldCodsuOfTransaccionListNewTransaccion = em.merge(oldCodsuOfTransaccionListNewTransaccion);
                    }
                }
            }
            for (Ctaahorro ctaahorroListOldCtaahorro : ctaahorroListOld) {
                if (!ctaahorroListNew.contains(ctaahorroListOldCtaahorro)) {
                    ctaahorroListOldCtaahorro.setCodsu(null);
                    ctaahorroListOldCtaahorro = em.merge(ctaahorroListOldCtaahorro);
                }
            }
            for (Ctaahorro ctaahorroListNewCtaahorro : ctaahorroListNew) {
                if (!ctaahorroListOld.contains(ctaahorroListNewCtaahorro)) {
                    Sucursal oldCodsuOfCtaahorroListNewCtaahorro = ctaahorroListNewCtaahorro.getCodsu();
                    ctaahorroListNewCtaahorro.setCodsu(sucursal);
                    ctaahorroListNewCtaahorro = em.merge(ctaahorroListNewCtaahorro);
                    if (oldCodsuOfCtaahorroListNewCtaahorro != null && !oldCodsuOfCtaahorroListNewCtaahorro.equals(sucursal)) {
                        oldCodsuOfCtaahorroListNewCtaahorro.getCtaahorroList().remove(ctaahorroListNewCtaahorro);
                        oldCodsuOfCtaahorroListNewCtaahorro = em.merge(oldCodsuOfCtaahorroListNewCtaahorro);
                    }
                }
            }
            for (Cajero cajeroListOldCajero : cajeroListOld) {
                if (!cajeroListNew.contains(cajeroListOldCajero)) {
                    cajeroListOldCajero.setCodsu(null);
                    cajeroListOldCajero = em.merge(cajeroListOldCajero);
                }
            }
            for (Cajero cajeroListNewCajero : cajeroListNew) {
                if (!cajeroListOld.contains(cajeroListNewCajero)) {
                    Sucursal oldCodsuOfCajeroListNewCajero = cajeroListNewCajero.getCodsu();
                    cajeroListNewCajero.setCodsu(sucursal);
                    cajeroListNewCajero = em.merge(cajeroListNewCajero);
                    if (oldCodsuOfCajeroListNewCajero != null && !oldCodsuOfCajeroListNewCajero.equals(sucursal)) {
                        oldCodsuOfCajeroListNewCajero.getCajeroList().remove(cajeroListNewCajero);
                        oldCodsuOfCajeroListNewCajero = em.merge(oldCodsuOfCajeroListNewCajero);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sucursal.getCodsu();
                if (findSucursal(id) == null) {
                    throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.");
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
            Sucursal sucursal;
            try {
                sucursal = em.getReference(Sucursal.class, id);
                sucursal.getCodsu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.", enfe);
            }
            Cantones codcanton = sucursal.getCodcanton();
            if (codcanton != null) {
                codcanton.getSucursalList().remove(sucursal);
                codcanton = em.merge(codcanton);
            }
            List<Cliente> clienteList = sucursal.getClienteList();
            for (Cliente clienteListCliente : clienteList) {
                clienteListCliente.setCodsu(null);
                clienteListCliente = em.merge(clienteListCliente);
            }
            List<Transaccion> transaccionList = sucursal.getTransaccionList();
            for (Transaccion transaccionListTransaccion : transaccionList) {
                transaccionListTransaccion.setCodsu(null);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
            }
            List<Ctaahorro> ctaahorroList = sucursal.getCtaahorroList();
            for (Ctaahorro ctaahorroListCtaahorro : ctaahorroList) {
                ctaahorroListCtaahorro.setCodsu(null);
                ctaahorroListCtaahorro = em.merge(ctaahorroListCtaahorro);
            }
            List<Cajero> cajeroList = sucursal.getCajeroList();
            for (Cajero cajeroListCajero : cajeroList) {
                cajeroListCajero.setCodsu(null);
                cajeroListCajero = em.merge(cajeroListCajero);
            }
            em.remove(sucursal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sucursal> findSucursalEntities() {
        return findSucursalEntities(true, -1, -1);
    }

    public List<Sucursal> findSucursalEntities(int maxResults, int firstResult) {
        return findSucursalEntities(false, maxResults, firstResult);
    }

    private List<Sucursal> findSucursalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sucursal.class));
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

    public Sucursal findSucursal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sucursal.class, id);
        } finally {
            em.close();
        }
    }

    public int getSucursalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sucursal> rt = cq.from(Sucursal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
