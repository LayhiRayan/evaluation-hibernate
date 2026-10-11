package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="personnes") @Inheritance(strategy=InheritanceType.JOINED)
public abstract class Personne {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) protected Integer id;
 @Column(nullable=false) protected String nom;
 @Column(nullable=false) protected String prenom;
 protected String telephone;
 protected String adresse;
 @Column(nullable=false) protected LocalDate dateNaissance;
 public Personne(){}
 public Personne(String nom,String prenom,String telephone,String adresse,LocalDate dateNaissance){
  this.nom=nom;this.prenom=prenom;this.telephone=telephone;this.adresse=adresse;this.dateNaissance=dateNaissance;
 }
 public Integer getId(){return id;}
 public String getNom(){return nom;}
 public void setNom(String nom){this.nom=nom;}
 public String getPrenom(){return prenom;}
 public void setPrenom(String prenom){this.prenom=prenom;}
 public String getTelephone(){return telephone;}
 public void setTelephone(String telephone){this.telephone=telephone;}
 public String getAdresse(){return adresse;}
 public void setAdresse(String adresse){this.adresse=adresse;}
 public LocalDate getDateNaissance(){return dateNaissance;}
 public void setDateNaissance(LocalDate date){dateNaissance=date;}
 public String toString(){return nom+" "+prenom+" ("+dateNaissance+")";}
}
