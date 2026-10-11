package ma.projet.service;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import ma.projet.beans.*;
import ma.projet.util.Dates;
public class FemmeService extends AbstractService<Femme,Integer> {
 public FemmeService(){super(Femme.class);}
 public FemmeService(EntityManagerFactory factory){super(Femme.class,factory);}
 public Optional<Femme> plusAgee(){
  return transactions.lire(em->em.createQuery("SELECT f FROM Femme f ORDER BY f.dateNaissance,f.id",Femme.class).setMaxResults(1).getResultStream().findFirst());
 }
 // Le modèle stocke un total par mariage, pas les dates de naissance des enfants.
 // La somme porte sur les mariages qui recoupent la période demandée.
 public long nombreEnfants(Integer id,LocalDate debut,LocalDate fin){
  Dates.intervalle(debut,fin);
  return transactions.lire(em->((Number)em.createNamedQuery("Femme.nombreEnfants").setParameter("id",id)
    .setParameter("debut",java.sql.Date.valueOf(debut)).setParameter("fin",java.sql.Date.valueOf(fin)).getSingleResult()).longValue());
 }
 public List<Femme> marieesAuMoinsDeuxFois(){
  return transactions.lire(em->em.createNamedQuery("Femme.marieesAuMoinsDeuxFois",Femme.class).getResultList());
 }
}
