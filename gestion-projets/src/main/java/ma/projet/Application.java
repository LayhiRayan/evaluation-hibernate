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
   EmployeService employes=new EmployeService(factory);
   ProjetService projets=new ProjetService(factory);
   TacheService taches=new TacheService(factory);
   EmployeTacheService affectations=new EmployeTacheService(factory);
   if(employes.findAll().isEmpty()){
    Employe chef=new Employe("Layhi","Rayan","0600000001");Employe dev=new Employe("Amrani","Salma","0600000002");
    employes.create(chef);employes.create(dev);
    Projet projet=new Projet("Gestion du magasin Atlas",LocalDate.of(2026,1,1),LocalDate.of(2026,6,30),chef);projets.create(projet);
    String[] noms={"Analyse","Conception","Développement"};
    String[] prix={"900","1500","4000"};
    for(int i=0;i<3;i++){
     LocalDate debut=LocalDate.of(2026,2+i,1);
     Tache tache=new Tache(noms[i],debut,debut.plusDays(20),new BigDecimal(prix[i]),projet);taches.create(tache);
     affectations.create(new EmployeTache(dev,tache,debut.plusDays(2),debut.plusDays(15)));
    }
   }
   for(Employe employe:employes.findAll()){
    System.out.println("\n=== Employé : "+employe+" ===\nProjets gérés :");
    employes.projetsGeres(employe.getId()).forEach(System.out::println);
    System.out.println("Tâches réalisées :");employes.tachesRealisees(employe.getId()).forEach(System.out::println);
   }
   for(Projet projet:projets.findAll()){
    System.out.println("\n=== Tâches planifiées : "+projet.getNom()+" ===");
    projets.tachesPlanifiees(projet.getId()).forEach(System.out::println);
    System.out.println(projets.afficherTachesRealisees(projet.getId()));
    System.out.println("Coût global (somme des prix des tâches) : "+projets.coutGlobal(projet.getId())+" DH");
   }
   System.out.println("\n=== Tâches de prix supérieur à 1000 DH ===");taches.prixSuperieur1000().forEach(System.out::println);
   System.out.println("\n=== Tâches réalisées entre février et avril 2026 ===");
   taches.realiseesEntre(LocalDate.of(2026,2,1),LocalDate.of(2026,4,30)).forEach(System.out::println);
   ConsoleH2.attendre(args,"gestion_projets");
  }finally{HibernateUtil.shutdown();}
 }
}
