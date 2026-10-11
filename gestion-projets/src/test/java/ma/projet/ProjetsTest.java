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
public class ProjetsTest {
 private EntityManagerFactory factory;private EmployeService employes;private ProjetService projets;private TacheService taches;private EmployeTacheService affectations;
 private Employe chef,dev;private Projet projet;private Tache t1000,t1500;private LocalDate debut=LocalDate.of(2026,2,1),fin=debut.plusDays(10);
 @Before public void init(){
  factory=HibernateUtil.creerPourTests("jdbc:h2:mem:projets_"+UUID.randomUUID());
  employes=new EmployeService(factory);projets=new ProjetService(factory);taches=new TacheService(factory);affectations=new EmployeTacheService(factory);
  chef=new Employe("Layhi","Rayan","0600000001");dev=new Employe("Amrani","Salma","0600000002");employes.create(chef);employes.create(dev);
  projet=new Projet("Atlas",debut,fin.plusMonths(1),chef);projets.create(projet);
  t1000=new Tache("Analyse",debut,fin,new BigDecimal("1000"),projet);t1500=new Tache("Code",debut,fin,new BigDecimal("1500"),projet);taches.create(t1000);taches.create(t1500);
  affectations.create(new EmployeTache(dev,t1000,debut,fin));affectations.create(new EmployeTache(chef,t1000,debut,fin));
  affectations.create(new EmployeTache(dev,t1500,debut,null));
 }
 @After public void close(){if(factory!=null)factory.close();}
 @Test public void employeEtChef(){
  assertEquals(1,employes.projetsGeres(chef.getId()).size());assertTrue(employes.projetsGeres(dev.getId()).isEmpty());
  assertEquals(1,employes.tachesRealisees(dev.getId()).size());assertEquals(t1000.getId(),employes.tachesRealisees(dev.getId()).get(0).getId());
 }
 @Test public void planificationRealisationsEtCoutSansDoubleComptage(){
  assertEquals(2,projets.tachesPlanifiees(projet.getId()).size());
  assertEquals(2,projets.tachesRealisees(projet.getId()).size());
  assertEquals(0,new BigDecimal("2500").compareTo(projets.coutGlobal(projet.getId())));
  assertTrue(projets.afficherTachesRealisees(projet.getId()).contains("01/02/2026"));
 }
 @Test public void requeteNommeeEtDatesReelles(){
  assertEquals(t1500.getId(),taches.prixSuperieur1000().get(0).getId());assertEquals(1,taches.prixSuperieur1000().size());
  assertEquals(1,taches.realiseesEntre(debut,fin).size());
  assertTrue(taches.realiseesEntre(debut.plusDays(1),fin).isEmpty());
  assertTrue(taches.realiseesEntre(debut,fin.minusDays(1)).isEmpty());
 }
 @Test public void crudAffectationEtCle(){
  EmployeTacheId id=new EmployeTacheId(dev.getId(),t1500.getId());EmployeTache a=affectations.findById(id);a.setPeriode(debut,fin);
  affectations.update(a);assertEquals(2,employes.tachesRealisees(dev.getId()).size());
  assertEquals(id,new EmployeTacheId(dev.getId(),t1500.getId()));assertEquals(id.hashCode(),new EmployeTacheId(dev.getId(),t1500.getId()).hashCode());
  affectations.delete(a);assertNull(affectations.findById(id));assertNotNull(taches.findById(t1500.getId()));
 }
 @Test public void validationEtRollback(){
  assertThrows(IllegalArgumentException.class,()->new EmployeTache(dev,t1500,fin,debut));
  assertThrows(IllegalArgumentException.class,()->t1000.setPrix(new BigDecimal("-1")));
  assertThrows(RuntimeException.class,()->affectations.create(new EmployeTache(dev,t1000,debut,fin)));
  assertEquals(3,affectations.findAll().size());
  Projet autre=new Projet("Autre",debut,fin,chef);projets.create(autre);assertNotNull(autre.getId());
 }
 @Test public void crudServicesEtSuppressionProtegee(){
  projet.setNom("Atlas modifié");projets.update(projet);assertEquals("Atlas modifié",projets.findById(projet.getId()).getNom());
  assertThrows(RuntimeException.class,()->projets.delete(projet));assertNotNull(projets.findById(projet.getId()));
  Employe libre=new Employe("Libre","Test","0");employes.create(libre);libre.setPrenom("Modifié");employes.update(libre);
  assertEquals("Modifié",employes.findById(libre.getId()).getPrenom());employes.delete(libre);assertNull(employes.findById(libre.getId()));
 }
}
