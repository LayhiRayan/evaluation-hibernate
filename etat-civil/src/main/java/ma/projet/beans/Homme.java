package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="hommes")
public class Homme extends Personne {
 public Homme(){}
 public Homme(String nom,String prenom,String telephone,String adresse,LocalDate dateNaissance){super(nom,prenom,telephone,adresse,dateNaissance);}
}
