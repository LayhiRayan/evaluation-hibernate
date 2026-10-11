package ma.projet.classes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
@Embeddable
public class LigneCommandeId implements Serializable {
 private static final long serialVersionUID=1L;
 private Integer commandeId;
 private Integer produitId;
 public LigneCommandeId(){}
 public LigneCommandeId(Integer a,Integer b){this.commandeId=a;this.produitId=b;}
 public Integer getCommandeId(){return commandeId;}
 public Integer getProduitId(){return produitId;}
 public boolean equals(Object o){if(this==o)return true;if(!(o instanceof LigneCommandeId))return false;LigneCommandeId autre=(LigneCommandeId)o;return Objects.equals(commandeId,autre.commandeId)&&Objects.equals(produitId,autre.produitId);}
 public int hashCode(){return Objects.hash(commandeId,produitId);}
}
