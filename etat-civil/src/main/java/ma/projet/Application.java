package ma.projet;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.EntityManagerFactory;
import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.*;
public class Application {
 public static void main(String[] args)throws Exception{
  try{
   EntityManagerFactory factory=HibernateUtil.getSessionFactory();
   HommeService hommes=new HommeService(factory);FemmeService femmes=new FemmeService(factory);MariageService mariages=new MariageService(factory);
   if(hommes.findAll().isEmpty()&&femmes.findAll().isEmpty()){
    String[] prenoms={"Salma","Amal","Wafa","Karima","Nadia","Leila","Hana","Meriem","Imane","Sofia"};
    List<Femme> listeFemmes=new ArrayList<>();List<Homme> listeHommes=new ArrayList<>();
    for(int i=0;i<10;i++){Femme femme=new Femme("Exemple"+(i+1),prenoms[i],"06000000"+String.format("%02d",i),"Adresse fictive",LocalDate.of(1960+i,1,15));femmes.create(femme);listeFemmes.add(femme);}
    String[] noms={"Youssef","Omar","Said","Karim","Adil"};
    for(int i=0;i<5;i++){Homme homme=new Homme("Démonstration"+(i+1),noms[i],"06100000"+String.format("%02d",i),"Adresse fictive",LocalDate.of(1955+i,3,10));hommes.create(homme);listeHommes.add(homme);}
    Homme premier=listeHommes.get(0);
    for(int i=0;i<4;i++)mariages.create(new Mariage(premier,listeFemmes.get(i),LocalDate.of(1990+i*3,1,1),i==3?LocalDate.of(2010,1,1):null,i+1));
    mariages.create(new Mariage(listeHommes.get(1),listeFemmes.get(3),LocalDate.of(2012,1,1),null,2));
    mariages.create(new Mariage(listeHommes.get(2),listeFemmes.get(4),LocalDate.of(2005,6,1),null,1));
   }
   System.out.println("\n=== Liste des 10 femmes de démonstration ===");femmes.findAll().forEach(System.out::println);
   System.out.println("\n=== Femme la plus âgée ===");femmes.plusAgee().ifPresent(System.out::println);
   Homme premier=hommes.findAll().get(0);Femme premiere=femmes.findAll().get(0);
   LocalDate debut=LocalDate.of(2000,1,1),fin=LocalDate.of(2009,12,31);
   System.out.println("\n=== Épouses entre "+debut+" et "+fin+" ===");hommes.epousesEntre(premier.getId(),debut,fin).forEach(System.out::println);
   System.out.println("Enfants enregistrés dans les mariages de "+premiere.getPrenom()+" recoupant la période : "+femmes.nombreEnfants(premiere.getId(),debut,fin));
   System.out.println("\n=== Femmes mariées au moins deux fois ===");femmes.marieesAuMoinsDeuxFois().forEach(System.out::println);
   System.out.println("\n=== Hommes mariés simultanément à quatre femmes dans la période ===");hommes.mariesAQuatreFemmes(debut,fin).forEach(System.out::println);
   System.out.println("Nombre : "+hommes.nombreHommesMariesAQuatreFemmes(debut,fin));
   System.out.println("\n"+hommes.afficherMariages(premier.getId()));
   ConsoleH2.attendre(args,"etat_civil");
  }finally{HibernateUtil.shutdown();}
 }
}
