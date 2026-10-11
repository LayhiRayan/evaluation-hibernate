package ma.projet;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.EntityManagerFactory;
import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.*;
public class Application {
 public static void main(String[] args)throws Exception{
  try{
   EntityManagerFactory factory=HibernateUtil.getSessionFactory();
   CategorieService categories=new CategorieService(factory);
   ProduitService produits=new ProduitService(factory);
   CommandeService commandes=new CommandeService(factory);
   LigneCommandeService lignes=new LigneCommandeService(factory);
   if(categories.findAll().isEmpty()){
    Categorie categorie=new Categorie("INFO","Matériel informatique");categories.create(categorie);
    Produit clavier=new Produit("CL24",new BigDecimal("120"),categorie);
    Produit souris=new Produit("SR18",new BigDecimal("100"),categorie);
    Produit ecran=new Produit("EC27",new BigDecimal("200"),categorie);
    produits.create(clavier);produits.create(souris);produits.create(ecran);
    Commande commande=new Commande(LocalDate.of(2026,10,5));commandes.create(commande);
    lignes.create(new LigneCommandeProduit(commande,clavier,7));
    lignes.create(new LigneCommandeProduit(commande,souris,14));
    lignes.create(new LigneCommandeProduit(commande,ecran,5));
   }
   System.out.println("\n=== Produits par catégorie ===");
   for(Categorie categorie:categories.findAll()){System.out.println(categorie);produits.parCategorie(categorie.getId()).forEach(System.out::println);}
   System.out.println("\n=== Produits commandés du 1 au 31 octobre 2026 ===");
   produits.commandesEntre(LocalDate.of(2026,10,1),LocalDate.of(2026,10,31)).forEach(System.out::println);
   for(Commande commande:commandes.findAll())System.out.println(produits.afficherCommande(commande.getId()));
   System.out.println("\n=== Prix strictement supérieur à 100 DH (requête nommée) ===");
   produits.prixSuperieur100().forEach(System.out::println);
   ConsoleH2.attendre(args,"gestion_stock");
  }finally{HibernateUtil.shutdown();}
 }
}
