package ma.projet.service;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import ma.projet.classes.*;
public class ProjetService extends AbstractService<Projet,Integer> {
 public ProjetService(){super(Projet.class);}
 public ProjetService(EntityManagerFactory factory){super(Projet.class,factory);}
 public List<Tache> tachesPlanifiees(Integer id){
  return transactions.lire(em->em.createQuery("SELECT t FROM Tache t JOIN FETCH t.projet WHERE t.projet.id=:id ORDER BY t.id",Tache.class).setParameter("id",id).getResultList());
 }
 public List<EmployeTache> tachesRealisees(Integer id){
  return transactions.lire(em->em.createQuery("SELECT a FROM EmployeTache a JOIN FETCH a.tache t JOIN FETCH a.employe WHERE t.projet.id=:id AND a.dateFinReelle IS NOT NULL ORDER BY t.id,a.employe.id",EmployeTache.class).setParameter("id",id).getResultList());
 }
 public BigDecimal coutGlobal(Integer id){
  BigDecimal total=transactions.lire(em->em.createQuery("SELECT SUM(t.prix) FROM Tache t WHERE t.projet.id=:id",BigDecimal.class).setParameter("id",id).getSingleResult());
  return total==null?BigDecimal.ZERO:total;
 }
 public String afficherTachesRealisees(Integer id){
  Projet projet=findById(id);if(projet==null)throw new IllegalArgumentException("Projet inconnu.");
  DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MM/uuuu");
  StringBuilder texte=new StringBuilder("Projet : ").append(id).append("   Nom : ").append(projet.getNom())
    .append("   Date début : ").append(projet.getDateDebut().format(format))
    .append("\nListe des tâches réalisées (une ligne par employé) :\nNum Nom                  Début réel   Fin réelle   Employé\n");
  for(EmployeTache a:tachesRealisees(id))texte.append(String.format("%-3d %-20s %s   %s   %s%n",a.getTache().getId(),a.getTache().getNom(),a.getDateDebutReelle().format(format),a.getDateFinReelle().format(format),a.getEmploye()));
  return texte.toString();
 }
}
