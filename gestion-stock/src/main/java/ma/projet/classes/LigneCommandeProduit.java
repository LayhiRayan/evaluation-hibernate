package ma.projet.classes;
import javax.persistence.*;
@Entity @Table(name="lignes_commande")
public class LigneCommandeProduit {
 @EmbeddedId private LigneCommandeId id=new LigneCommandeId();
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @MapsId("commandeId") @JoinColumn(name="commande_id") private Commande commande;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @MapsId("produitId") @JoinColumn(name="produit_id") private Produit produit;
 @Column(nullable=false) private int quantite;
 public LigneCommandeProduit(){}
 public LigneCommandeProduit(Commande commande,Produit produit,int quantite){
  if(commande.getId()==null||produit.getId()==null)throw new IllegalArgumentException("Enregistrer commande et produit avant la ligne.");
  this.commande=commande;this.produit=produit;this.id=new LigneCommandeId(commande.getId(),produit.getId());setQuantite(quantite);
 }
 public LigneCommandeId getId(){return id;}
 public Commande getCommande(){return commande;}
 public Produit getProduit(){return produit;}
 public int getQuantite(){return quantite;}
 public void setQuantite(int quantite){if(quantite<1)throw new IllegalArgumentException("Quantité positive requise.");this.quantite=quantite;}
 public void rattacher(Commande commande,Produit produit){
  if(!id.equals(new LigneCommandeId(commande.getId(),produit.getId())))throw new IllegalArgumentException("Références incompatibles.");
  this.commande=commande;this.produit=produit;
 }
 @PrePersist @PreUpdate private void valider(){setQuantite(quantite);}
}
