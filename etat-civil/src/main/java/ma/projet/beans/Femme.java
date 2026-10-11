package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="femmes")
@NamedQuery(name="Femme.marieesAuMoinsDeuxFois",query="SELECT f FROM Femme f WHERE (SELECT COUNT(m) FROM Mariage m WHERE m.femme=f)>=2 ORDER BY f.id")
@NamedNativeQuery(name="Femme.nombreEnfants",query="SELECT COALESCE(SUM(m.nbr_enfant),0) FROM mariages m WHERE m.femme_id=:id AND m.date_debut<=:fin AND (m.date_fin IS NULL OR m.date_fin>:debut)")
public class Femme extends Personne {
 public Femme(){}
 public Femme(String nom,String prenom,String telephone,String adresse,LocalDate dateNaissance){super(nom,prenom,telephone,adresse,dateNaissance);}
}
