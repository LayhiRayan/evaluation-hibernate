package ma.projet.service;
import javax.persistence.*;
import ma.projet.classes.*;
public class EmployeTacheService extends AbstractService<EmployeTache,EmployeTacheId> {
 public EmployeTacheService(){super(EmployeTache.class);}
 public EmployeTacheService(EntityManagerFactory factory){super(EmployeTache.class,factory);}
 protected void preparerCreation(EntityManager em,EmployeTache affectation){
  Employe employe=em.find(Employe.class,affectation.getId().getEmployeId());
  Tache tache=em.find(Tache.class,affectation.getId().getTacheId());
  if(employe==null||tache==null)throw new IllegalArgumentException("Employé ou tâche introuvable.");
  affectation.rattacher(employe,tache);
 }
}
