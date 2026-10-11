package ma.projet.service;
import javax.persistence.EntityManagerFactory;
import ma.projet.classes.Categorie;
public class CategorieService extends AbstractService<Categorie,Integer> {
 public CategorieService(){super(Categorie.class);}
 public CategorieService(EntityManagerFactory factory){super(Categorie.class,factory);}
}
