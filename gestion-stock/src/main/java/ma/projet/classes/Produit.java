package ma.projet.classes;
import javax.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="produits")
@NamedQuery(name="Produit.prixSuperieur100",query="SELECT p FROM Produit p JOIN FETCH p.categorie WHERE p.prix > 100 ORDER BY p.reference")
public class Produit {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false,unique=true) private String reference;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal prix;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="categorie_id",nullable=false) private Categorie categorie;
 public Produit(){}
 public Produit(String reference,BigDecimal prix,Categorie categorie){this.reference=reference;setPrix(prix);this.categorie=categorie;}
 public Integer getId(){return id;}
 public String getReference(){return reference;}
 public void setReference(String reference){this.reference=reference;}
 public BigDecimal getPrix(){return prix;}
 public void setPrix(BigDecimal prix){if(prix==null||prix.signum()<0)throw new IllegalArgumentException("Prix invalide.");this.prix=prix;}
 public Categorie getCategorie(){return categorie;}
 public void setCategorie(Categorie categorie){this.categorie=categorie;}
 @PrePersist @PreUpdate private void valider(){setPrix(prix);}
 public String toString(){return reference+" : "+prix+" DH";}
}
