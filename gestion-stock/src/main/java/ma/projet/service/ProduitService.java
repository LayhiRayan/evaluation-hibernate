package ma.projet.service;
import javax.persistence.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import ma.projet.classes.*;
import ma.projet.util.Dates;
public class ProduitService extends AbstractService<Produit,Integer> {
 public ProduitService(){super(Produit.class);}
 public ProduitService(EntityManagerFactory factory){super(Produit.class,factory);}
 public List<Produit> parCategorie(Integer id){
  verifier(id);
  return transactions.lire(em->em.createQuery("SELECT p FROM Produit p JOIN FETCH p.categorie WHERE p.categorie.id=:id ORDER BY p.reference",Produit.class).setParameter("id",id).getResultList());
 }
 public List<Produit> commandesEntre(LocalDate debut,LocalDate fin){
  Dates.intervalle(debut,fin);
  return transactions.lire(em->em.createQuery("SELECT DISTINCT p FROM LigneCommandeProduit l JOIN l.produit p JOIN FETCH p.categorie WHERE l.commande.date BETWEEN :debut AND :fin ORDER BY p.reference",Produit.class).setParameter("debut",debut).setParameter("fin",fin).getResultList());
 }
 public List<LigneCommandeProduit> parCommande(Integer id){
  verifier(id);
  return transactions.lire(em->em.createQuery("SELECT l FROM LigneCommandeProduit l JOIN FETCH l.produit p JOIN FETCH l.commande WHERE l.commande.id=:id ORDER BY p.reference",LigneCommandeProduit.class).setParameter("id",id).getResultList());
 }
 public List<Produit> prixSuperieur100(){
  return transactions.lire(em->em.createNamedQuery("Produit.prixSuperieur100",Produit.class).getResultList());
 }
 public String afficherCommande(Integer id){
  Commande commande=transactions.lire(em->em.find(Commande.class,id));
  if(commande==null)throw new IllegalArgumentException("Commande inconnue.");
  StringBuilder texte=new StringBuilder("Commande : ").append(id).append("    Date : ")
    .append(commande.getDate().format(DateTimeFormatter.ofPattern("dd MMMM uuuu",Locale.FRENCH)))
    .append("\nListe des produits :\nRéférence   Prix       Quantité\n");
  for(LigneCommandeProduit ligne:parCommande(id))texte.append(String.format(Locale.ROOT,"%-11s %-10s %d%n",ligne.getProduit().getReference(),ligne.getProduit().getPrix()+" DH",ligne.getQuantite()));
  return texte.toString();
 }
}
