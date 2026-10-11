package ma.projet.service;
import javax.persistence.*;
import java.util.List;
import ma.projet.classes.*;
public class EmployeService extends AbstractService<Employe,Integer> {
 public EmployeService(){super(Employe.class);}
 public EmployeService(EntityManagerFactory factory){super(Employe.class,factory);}
 public List<Tache> tachesRealisees(Integer id){
  return transactions.lire(em->em.createQuery("SELECT DISTINCT t FROM EmployeTache a JOIN a.tache t JOIN FETCH t.projet WHERE a.employe.id=:id AND a.dateFinReelle IS NOT NULL ORDER BY t.id",Tache.class).setParameter("id",id).getResultList());
 }
 public List<Projet> projetsGeres(Integer id){
  return transactions.lire(em->em.createQuery("SELECT p FROM Projet p JOIN FETCH p.chef WHERE p.chef.id=:id ORDER BY p.id",Projet.class).setParameter("id",id).getResultList());
 }
}
