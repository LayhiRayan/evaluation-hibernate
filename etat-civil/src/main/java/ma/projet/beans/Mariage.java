package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="mariages",uniqueConstraints=@UniqueConstraint(columnNames={"homme_id","femme_id","date_debut"}))
public class Mariage {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="homme_id",nullable=false) private Homme homme;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="femme_id",nullable=false) private Femme femme;
 @Column(name="date_debut",nullable=false) private LocalDate dateDebut;
 @Column(name="date_fin") private LocalDate dateFin;
 @Column(name="nbr_enfant",nullable=false) private int nbrEnfant;
 public Mariage(){}
 public Mariage(Homme homme,Femme femme,LocalDate debut,LocalDate fin,int enfants){this.homme=homme;this.femme=femme;setPeriode(debut,fin);setNbrEnfant(enfants);}
 public Integer getId(){return id;}
 public Homme getHomme(){return homme;}
 public Femme getFemme(){return femme;}
 public LocalDate getDateDebut(){return dateDebut;}
 public LocalDate getDateFin(){return dateFin;}
 public int getNbrEnfant(){return nbrEnfant;}
 public void setNbrEnfant(int enfants){if(enfants<0)throw new IllegalArgumentException("Nombre d'enfants négatif.");nbrEnfant=enfants;}
 public void setPeriode(LocalDate debut,LocalDate fin){if(debut==null||(fin!=null&&!fin.isAfter(debut)))throw new IllegalArgumentException("Période de mariage invalide.");dateDebut=debut;dateFin=fin;}
 @PrePersist @PreUpdate private void valider(){setPeriode(dateDebut,dateFin);setNbrEnfant(nbrEnfant);}
}
