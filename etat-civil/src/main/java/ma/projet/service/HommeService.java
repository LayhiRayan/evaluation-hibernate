package ma.projet.service;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import ma.projet.beans.*;
import ma.projet.util.Dates;
public class HommeService extends AbstractService<Homme,Integer> {
 public HommeService(){super(Homme.class);}
 public HommeService(EntityManagerFactory factory){super(Homme.class,factory);}
 public List<Femme> epousesEntre(Integer id,LocalDate debut,LocalDate fin){
  Dates.intervalle(debut,fin);
  return transactions.lire(em->em.createQuery("SELECT DISTINCT f FROM Mariage m JOIN m.femme f WHERE m.homme.id=:id AND m.dateDebut<=:fin AND (m.dateFin IS NULL OR m.dateFin>:debut) ORDER BY f.id",Femme.class)
    .setParameter("id",id).setParameter("debut",debut).setParameter("fin",fin).getResultList());
 }
 public List<Mariage> mariages(Integer id){
  return transactions.lire(em->em.createQuery("SELECT m FROM Mariage m JOIN FETCH m.femme JOIN FETCH m.homme WHERE m.homme.id=:id ORDER BY m.dateDebut,m.id",Mariage.class).setParameter("id",id).getResultList());
 }
 // Compter quatre épouses simultanées à au moins une date, pas quatre unions successives.
 // Les intervalles sont [dateDebut,dateFin[, et la période de recherche inclut ses deux bornes.
 public List<Homme> mariesAQuatreFemmes(LocalDate debut,LocalDate fin){
  Dates.intervalle(debut,fin);
  return transactions.lire(em->{
   CriteriaBuilder cb=em.getCriteriaBuilder();
   CriteriaQuery<Mariage> evenements=cb.createQuery(Mariage.class);
   Root<Mariage> m=evenements.from(Mariage.class);
   evenements.select(m).where(cb.lessThanOrEqualTo(m.get("dateDebut"),fin),
     cb.or(cb.isNull(m.get("dateFin")),cb.greaterThan(m.get("dateFin"),debut)));
   Set<LocalDate> dates=new TreeSet<>();dates.add(debut);
   for(Mariage mariage:em.createQuery(evenements).getResultList()){
    if(!mariage.getDateDebut().isBefore(debut))dates.add(mariage.getDateDebut());
    if(mariage.getDateFin()!=null&&!mariage.getDateFin().isAfter(fin))dates.add(mariage.getDateFin());
   }
   Set<Integer> ids=new TreeSet<>();
   for(LocalDate date:dates){
    CriteriaQuery<Integer> query=cb.createQuery(Integer.class);
    Root<Mariage> racine=query.from(Mariage.class);
    Path<Integer> hommeId=racine.get("homme").get("id");
    query.select(hommeId).where(cb.lessThanOrEqualTo(racine.get("dateDebut"),date),
      cb.or(cb.isNull(racine.get("dateFin")),cb.greaterThan(racine.get("dateFin"),date)))
      .groupBy(hommeId).having(cb.equal(cb.countDistinct(racine.get("femme").get("id")),4L));
    ids.addAll(em.createQuery(query).getResultList());
   }
   List<Homme> resultat=new ArrayList<>();
   for(Integer id:ids)resultat.add(em.find(Homme.class,id));
   return resultat;
  });
 }
 public long nombreHommesMariesAQuatreFemmes(LocalDate debut,LocalDate fin){return mariesAQuatreFemmes(debut,fin).size();}
 public String afficherMariages(Integer id){
  Homme homme=findById(id);if(homme==null)throw new IllegalArgumentException("Homme inconnu.");
  DateTimeFormatter format=DateTimeFormatter.ofPattern("dd/MM/uuuu");
  StringBuilder texte=new StringBuilder("Nom : ").append(homme.getNom()).append(' ').append(homme.getPrenom()).append('\n');
  List<Mariage> unions=mariages(id);
  for(boolean enCours:new boolean[]{true,false}){
   texte.append(enCours?"Mariages En Cours :\n":"Mariages échoués :\n");
   int numero=1;
   for(Mariage union:unions){
    if((union.getDateFin()==null)!=enCours)continue;
    texte.append(numero++).append(". Femme : ").append(union.getFemme().getNom()).append(' ').append(union.getFemme().getPrenom())
      .append("   Date Début : ").append(union.getDateDebut().format(format));
    if(!enCours)texte.append("   Date Fin : ").append(union.getDateFin().format(format));
    texte.append("   Nbr Enfants : ").append(union.getNbrEnfant()).append('\n');
   }
  }
  return texte.toString();
 }
}
