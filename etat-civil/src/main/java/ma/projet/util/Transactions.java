package ma.projet.util;

import java.util.function.Function;
import javax.persistence.*;

public final class Transactions {
    private final EntityManagerFactory factory;
    public Transactions(EntityManagerFactory factory) { this.factory = factory; }
    public <R> R lire(Function<EntityManager, R> action) {
        EntityManager em = factory.createEntityManager();
        try { return action.apply(em); } finally { em.close(); }
    }
    public <R> R ecrire(Function<EntityManager, R> action) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R resultat = action.apply(em);
            tx.commit();
            return resultat;
        } catch (RuntimeException erreur) {
            try { if (tx.isActive()) { tx.rollback(); } }
            catch (RuntimeException annulation) { erreur.addSuppressed(annulation); }
            throw erreur;
        } finally { em.close(); }
    }
}
