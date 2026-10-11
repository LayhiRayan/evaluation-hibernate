package ma.projet.service;
import javax.persistence.*;
import ma.projet.beans.*;
public class MariageService extends AbstractService<Mariage,Integer> {
 public MariageService(){super(Mariage.class);}
 public MariageService(EntityManagerFactory factory){super(Mariage.class,factory);}
}
