package ma.projet.service;
import javax.persistence.EntityManagerFactory;
import ma.projet.classes.Commande;
public class CommandeService extends AbstractService<Commande,Integer> {
 public CommandeService(){super(Commande.class);}
 public CommandeService(EntityManagerFactory factory){super(Commande.class,factory);}
}
