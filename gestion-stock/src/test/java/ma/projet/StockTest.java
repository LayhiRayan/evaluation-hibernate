package ma.projet;
import org.junit.*;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.EntityManagerFactory;
import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
public class StockTest {
 private EntityManagerFactory factory;
 private CategorieService categories;private ProduitService produits;private CommandeService commandes;private LigneCommandeService lignes;
 private Categorie cat;private Produit p100,p120;private Commande commande;
 @Before public void init(){
  factory=HibernateUtil.creerPourTests("jdbc:h2:mem:stock_"+UUID.randomUUID());
  categories=new CategorieService(factory);produits=new ProduitService(factory);commandes=new CommandeService(factory);lignes=new LigneCommandeService(factory);
  cat=new Categorie("INFO","Informatique");categories.create(cat);
  p100=new Produit("SR18",new BigDecimal("100"),cat);p120=new Produit("CL24",new BigDecimal("120"),cat);produits.create(p100);produits.create(p120);
  commande=new Commande(LocalDate.of(2026,10,5));commandes.create(commande);
  lignes.create(new LigneCommandeProduit(commande,p120,7));
 }
 @After public void close(){if(factory!=null)factory.close();}
 @Test public void categorieEtPrixStrict(){
  assertEquals(2,produits.parCategorie(cat.getId()).size());
  assertEquals(1,produits.prixSuperieur100().size());
  assertEquals(p120.getId(),produits.prixSuperieur100().get(0).getId());
 }
 @Test public void intervalleInclusifEtDistinct(){
  Commande seconde=new Commande(commande.getDate());commandes.create(seconde);lignes.create(new LigneCommandeProduit(seconde,p120,3));
  assertEquals(1,produits.commandesEntre(commande.getDate(),commande.getDate()).size());
  assertTrue(produits.commandesEntre(LocalDate.of(2026,10,6),LocalDate.of(2026,10,7)).isEmpty());
 }
 @Test public void commandeEtAffichage(){
  assertEquals(7,produits.parCommande(commande.getId()).get(0).getQuantite());
  String texte=produits.afficherCommande(commande.getId());
  assertTrue(texte.contains("CL24"));assertTrue(texte.contains("120.00 DH"));assertTrue(texte.contains("octobre"));
  assertThrows(IllegalArgumentException.class,()->produits.afficherCommande(-1));
 }
 @Test public void crudEtCleComposee(){
  LigneCommandeId id=new LigneCommandeId(commande.getId(),p120.getId());
  assertEquals(id,new LigneCommandeId(commande.getId(),p120.getId()));
  assertEquals(id.hashCode(),new LigneCommandeId(commande.getId(),p120.getId()).hashCode());
  LigneCommandeProduit ligne=lignes.findById(id);ligne.setQuantite(9);lignes.update(ligne);assertEquals(9,lignes.findById(id).getQuantite());
  lignes.delete(ligne);assertNull(lignes.findById(id));assertNotNull(produits.findById(p120.getId()));
  commandes.delete(commande);assertNull(commandes.findById(commande.getId()));
 }
 @Test public void erreurUniciteEtReprise(){
  assertThrows(RuntimeException.class,()->lignes.create(new LigneCommandeProduit(commande,p120,1)));
  assertEquals(1,lignes.findAll().size());
  lignes.create(new LigneCommandeProduit(commande,p100,2));assertEquals(2,lignes.findAll().size());
  assertThrows(RuntimeException.class,()->categories.create(new Categorie("INFO","Doublon")));
  cat.setLibelle("Matériel");categories.update(cat);assertEquals("Matériel",categories.findById(cat.getId()).getLibelle());
 }
 @Test public void validationEtContraintes(){
  assertThrows(IllegalArgumentException.class,()->new LigneCommandeProduit(commande,p100,0));
  assertThrows(IllegalArgumentException.class,()->p100.setPrix(new BigDecimal("-1")));
  assertThrows(IllegalArgumentException.class,()->produits.commandesEntre(commande.getDate(),commande.getDate().minusDays(1)));
  assertThrows(RuntimeException.class,()->produits.delete(p120));
  assertNotNull(produits.findById(p120.getId()));
 }
}
