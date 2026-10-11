package ma.projet.service;
import javax.persistence.*;
import ma.projet.classes.*;
public class LigneCommandeService extends AbstractService<LigneCommandeProduit,LigneCommandeId> {
 public LigneCommandeService(){super(LigneCommandeProduit.class);}
 public LigneCommandeService(EntityManagerFactory factory){super(LigneCommandeProduit.class,factory);}
 protected void preparerCreation(EntityManager em,LigneCommandeProduit ligne){
  Commande commande=em.find(Commande.class,ligne.getId().getCommandeId());
  Produit produit=em.find(Produit.class,ligne.getId().getProduitId());
  if(commande==null||produit==null)throw new IllegalArgumentException("Commande ou produit introuvable.");
  ligne.rattacher(commande,produit);
 }
}
