package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="commandes")
public class Commande {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(name="date_commande",nullable=false) private LocalDate date;
 public Commande(){}
 public Commande(LocalDate date){this.date=date;}
 public Integer getId(){return id;}
 public LocalDate getDate(){return date;}
 public void setDate(LocalDate date){this.date=date;}
}
