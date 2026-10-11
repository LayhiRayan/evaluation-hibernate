package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import ma.projet.util.Dates;
@Entity @Table(name="employe_tache")
public class EmployeTache {
 @EmbeddedId private EmployeTacheId id=new EmployeTacheId();
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @MapsId("employeId") @JoinColumn(name="employe_id") private Employe employe;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @MapsId("tacheId") @JoinColumn(name="tache_id") private Tache tache;
 @Column(nullable=false) private LocalDate dateDebutReelle;
 private LocalDate dateFinReelle;
 public EmployeTache(){}
 public EmployeTache(Employe employe,Tache tache,LocalDate debut,LocalDate fin){
  if(employe.getId()==null||tache.getId()==null)throw new IllegalArgumentException("Enregistrer employé et tâche avant l'affectation.");
  this.employe=employe;this.tache=tache;id=new EmployeTacheId(employe.getId(),tache.getId());setPeriode(debut,fin);
 }
 public EmployeTacheId getId(){return id;}
 public Employe getEmploye(){return employe;}
 public Tache getTache(){return tache;}
 public LocalDate getDateDebutReelle(){return dateDebutReelle;}
 public LocalDate getDateFinReelle(){return dateFinReelle;}
 public void setPeriode(LocalDate debut,LocalDate fin){
  if(debut==null)throw new IllegalArgumentException("Début réel obligatoire.");
  if(fin!=null)Dates.intervalle(debut,fin);dateDebutReelle=debut;dateFinReelle=fin;
 }
 public void rattacher(Employe employe,Tache tache){
  if(!id.equals(new EmployeTacheId(employe.getId(),tache.getId())))throw new IllegalArgumentException("Références incompatibles.");
  this.employe=employe;this.tache=tache;
 }
 @PrePersist @PreUpdate private void valider(){setPeriode(dateDebutReelle,dateFinReelle);}
}
