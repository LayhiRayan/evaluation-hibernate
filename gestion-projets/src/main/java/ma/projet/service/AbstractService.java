package ma.projet.service;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import ma.projet.dao.IDao;
import ma.projet.util.*;

public abstract class AbstractService<T, ID extends Serializable> implements IDao<T, ID> {
    private final Class<T> type;
    protected final Transactions transactions;
    protected AbstractService(Class<T> type) { this(type, HibernateUtil.getSessionFactory()); }
    protected AbstractService(Class<T> type, EntityManagerFactory factory) {
        this.type = type;
        transactions = new Transactions(factory);
    }
    protected void preparerCreation(EntityManager em, T entity) {}
    public void create(T entity) {
        verifier(entity);
        transactions.ecrire(em -> { preparerCreation(em, entity); em.persist(entity); return null; });
    }
    public T update(T entity) { verifier(entity); return transactions.ecrire(em -> em.merge(entity)); }
    public void delete(T entity) {
        verifier(entity);
        transactions.ecrire(em -> {
            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            if (id == null) { throw new IllegalArgumentException("Entité non enregistrée."); }
            T managed = em.find(type, id);
            if (managed == null) { throw new IllegalArgumentException("Entité introuvable."); }
            em.remove(managed);
            return null;
        });
    }
    public T findById(ID id) { verifier(id); return transactions.lire(em -> em.find(type, id)); }
    public List<T> findAll() {
        return transactions.lire(em -> em.createQuery("SELECT e FROM " + em.getMetamodel().entity(type).getName()
                + " e ORDER BY e.id", type).getResultList());
    }
    protected static void verifier(Object valeur) {
        if (valeur == null) { throw new IllegalArgumentException("Le paramètre est obligatoire."); }
    }
}
