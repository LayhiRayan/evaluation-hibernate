package ma.projet;
import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.EntityManagerFactory;
import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
public class EtatCivilTest {
 private EntityManagerFactory factory;private HommeService hommes;private FemmeService femmes;private MariageService mariages;
 private Homme homme;private List<Femme> liste;
 private LocalDate debut=LocalDate.of(2000,1,1),fin=LocalDate.of(2009,12,31);
 @Before public void init(){
  factory=HibernateUtil.creerPourTests("jdbc:h2:mem:civil_"+UUID.randomUUID());hommes=new HommeService(factory);femmes=new FemmeService(factory);mariages=new MariageService(factory);
  homme=new Homme("Atlas","Omar","0","Fictive",LocalDate.of(1960,1,1));hommes.create(homme);
  liste=new ArrayList<>();
  for(int i=0;i<5;i++){Femme femme=new Femme("Test"+i,"Femme"+i,"0","Fictive",LocalDate.of(1965+i,1,1));femmes.create(femme);liste.add(femme);}
 }
 @After public void close(){if(factory!=null)factory.close();}
 private Mariage mariage(Homme h,Femme f,LocalDate d,LocalDate fin,int enfants){Mariage m=new Mariage(h,f,d,fin,enfants);mariages.create(m);return m;}
 @Test public void plusAgeeEtEpousesBornes(){
  assertEquals(liste.get(0).getId(),femmes.plusAgee().get().getId());
  mariage(homme,liste.get(0),debut.minusYears(2),debut,2);
  mariage(homme,liste.get(1),debut,null,3);
  assertEquals(1,hommes.epousesEntre(homme.getId(),debut,fin).size());assertEquals(liste.get(1).getId(),hommes.epousesEntre(homme.getId(),debut,fin).get(0).getId());
 }
 @Test public void nativeEnfantsEtRemariageMemeCouple(){
  Femme f=liste.get(0);mariage(homme,f,debut.minusYears(3),debut,2);mariage(homme,f,debut.plusYears(1),null,3);
  assertEquals(3,femmes.nombreEnfants(f.getId(),debut,fin));
  assertEquals(5,femmes.nombreEnfants(f.getId(),debut.minusYears(5),fin));
  assertEquals(1,femmes.marieesAuMoinsDeuxFois().size());assertEquals(0,femmes.nombreEnfants(liste.get(1).getId(),debut,fin));
 }
 @Test public void quatreSimultaneesEtFinDeCinquieme(){
  for(int i=0;i<4;i++)mariage(homme,liste.get(i),debut.minusYears(1),null,0);
  mariage(homme,liste.get(4),debut.minusYears(1),debut.plusYears(1),0);
  assertEquals(0,hommes.nombreHommesMariesAQuatreFemmes(debut,debut));
  assertEquals(1,hommes.nombreHommesMariesAQuatreFemmes(debut,fin));
  assertEquals(1,hommes.nombreHommesMariesAQuatreFemmes(debut.plusYears(1),debut.plusYears(1)));
 }
 @Test public void quatreSuccessivesNeSontPasSimultanees(){
  for(int i=0;i<4;i++)mariage(homme,liste.get(i),debut.plusYears(i),debut.plusYears(i+1),0);
  assertEquals(0,hommes.nombreHommesMariesAQuatreFemmes(debut,fin));
 }
 @Test public void compterChaqueHommeUneSeuleFois(){
  for(int i=0;i<4;i++)mariage(homme,liste.get(i),debut.plusYears(i),null,0);
  assertEquals(1,hommes.mariesAQuatreFemmes(debut,fin).size());
  assertEquals(homme.getId(),hommes.mariesAQuatreFemmes(debut,fin).get(0).getId());
 }
 @Test public void detailsDatesEtEnfantsApresFermeture(){
  mariage(homme,liste.get(0),debut.minusYears(1),debut,2);mariage(homme,liste.get(1),debut,null,3);
  String texte=hommes.afficherMariages(homme.getId());assertTrue(texte.contains("Mariages En Cours"));assertTrue(texte.contains("Mariages échoués"));
  assertTrue(texte.contains("Nbr Enfants : 3"));assertTrue(texte.contains("01/01/2000"));
  assertEquals(2,hommes.mariages(homme.getId()).size());
 }
 @Test public void crudEtUnicite(){
  Mariage m=mariage(homme,liste.get(0),debut,null,1);m.setNbrEnfant(2);mariages.update(m);assertEquals(2,mariages.findById(m.getId()).getNbrEnfant());
  assertThrows(RuntimeException.class,()->mariage(homme,liste.get(0),debut,null,1));assertEquals(1,mariages.findAll().size());
  mariages.delete(m);assertNull(mariages.findById(m.getId()));assertNotNull(femmes.findById(liste.get(0).getId()));
  homme.setNom("Modifié");hommes.update(homme);assertEquals("Modifié",hommes.findById(homme.getId()).getNom());
 }
 @Test public void validationEtListeVide(){
  assertThrows(IllegalArgumentException.class,()->new Mariage(homme,liste.get(0),debut,debut,0));
  assertThrows(IllegalArgumentException.class,()->new Mariage(homme,liste.get(0),debut,null,-1));
  assertThrows(IllegalArgumentException.class,()->hommes.epousesEntre(homme.getId(),fin,debut));
  assertTrue(femmes.marieesAuMoinsDeuxFois().isEmpty());assertTrue(hommes.mariesAQuatreFemmes(debut,fin).isEmpty());
 }
}
