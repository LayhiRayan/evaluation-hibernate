package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import ma.projet.util.Dates;
@Entity @Table(name="taches")
@NamedQuery(name="Tache.prixSuperieur1000",query="SELECT t FROM Tache t JOIN FETCH t.projet WHERE t.prix>1000 ORDER BY t.id")
public class Tache {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false) private String nom;
 @Column(nullable=false) private LocalDate dateDebut;
 @Column(nullable=false) private LocalDate dateFin;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal prix;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="projet_id",nullable=false) private Projet projet;
 public Tache(){}
 public Tache(String nom,LocalDate debut,LocalDate fin,BigDecimal prix,Projet projet){this.nom=nom;setPeriode(debut,fin);setPrix(prix);this.projet=projet;}
 public Integer getId(){return id;}
 public String getNom(){return nom;}
 public void setNom(String nom){this.nom=nom;}
 public LocalDate getDateDebut(){return dateDebut;}
 public LocalDate getDateFin(){return dateFin;}
 public BigDecimal getPrix(){return prix;}
 public Projet getProjet(){return projet;}
 public void setProjet(Projet projet){this.projet=projet;}
 public void setPrix(BigDecimal prix){if(prix==null||prix.signum()<0)throw new IllegalArgumentException("Prix invalide.");this.prix=prix;}
 public void setPeriode(LocalDate debut,LocalDate fin){Dates.intervalle(debut,fin);dateDebut=debut;dateFin=fin;}
 @PrePersist @PreUpdate private void valider(){Dates.intervalle(dateDebut,dateFin);setPrix(prix);}
 public String toString(){return id+" "+nom+" : "+prix+" DH";}
}
