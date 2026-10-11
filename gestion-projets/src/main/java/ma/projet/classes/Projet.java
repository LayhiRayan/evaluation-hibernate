package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import ma.projet.util.Dates;
@Entity @Table(name="projets")
public class Projet {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false) private String nom;
 @Column(nullable=false) private LocalDate dateDebut;
 @Column(nullable=false) private LocalDate dateFin;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="chef_id",nullable=false) private Employe chef;
 public Projet(){}
 public Projet(String nom,LocalDate debut,LocalDate fin,Employe chef){this.nom=nom;setPeriode(debut,fin);this.chef=chef;}
 public Integer getId(){return id;}
 public String getNom(){return nom;}
 public void setNom(String nom){this.nom=nom;}
 public LocalDate getDateDebut(){return dateDebut;}
 public LocalDate getDateFin(){return dateFin;}
 public Employe getChef(){return chef;}
 public void setChef(Employe chef){this.chef=chef;}
 public void setPeriode(LocalDate debut,LocalDate fin){Dates.intervalle(debut,fin);dateDebut=debut;dateFin=fin;}
 @PrePersist @PreUpdate private void valider(){Dates.intervalle(dateDebut,dateFin);}
 public String toString(){return nom+" ("+dateDebut+" - "+dateFin+")";}
}
