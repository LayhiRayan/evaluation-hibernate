package ma.projet.service;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import ma.projet.classes.*;
import ma.projet.util.Dates;
public class TacheService extends AbstractService<Tache,Integer> {
 public TacheService(){super(Tache.class);}
 public TacheService(EntityManagerFactory factory){super(Tache.class,factory);}
 public List<Tache> prixSuperieur1000(){return transactions.lire(em->em.createNamedQuery("Tache.prixSuperieur1000",Tache.class).getResultList());}
 // Tâches terminées dont une réalisation complète est comprise dans l'intervalle inclusif.
 public List<Tache> realiseesEntre(LocalDate debut,LocalDate fin){
  Dates.intervalle(debut,fin);
  return transactions.lire(em->em.createQuery("SELECT DISTINCT t FROM EmployeTache a JOIN a.tache t JOIN FETCH t.projet WHERE a.dateDebutReelle>=:debut AND a.dateFinReelle<=:fin ORDER BY t.id",Tache.class).setParameter("debut",debut).setParameter("fin",fin).getResultList());
 }
}
