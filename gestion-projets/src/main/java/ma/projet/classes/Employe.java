package ma.projet.classes;
import javax.persistence.*;
@Entity @Table(name="employes")
public class Employe {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false) private String nom;
 @Column(nullable=false) private String prenom;
 private String telephone;
 public Employe(){}
 public Employe(String nom,String prenom,String telephone){this.nom=nom;this.prenom=prenom;this.telephone=telephone;}
 public Integer getId(){return id;}
 public String getNom(){return nom;}
 public void setNom(String nom){this.nom=nom;}
 public String getPrenom(){return prenom;}
 public void setPrenom(String prenom){this.prenom=prenom;}
 public String getTelephone(){return telephone;}
 public void setTelephone(String telephone){this.telephone=telephone;}
 public String toString(){return nom+" "+prenom;}
}
