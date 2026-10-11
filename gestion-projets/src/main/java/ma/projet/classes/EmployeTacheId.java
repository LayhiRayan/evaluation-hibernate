package ma.projet.classes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
@Embeddable
public class EmployeTacheId implements Serializable {
 private static final long serialVersionUID=1L;
 private Integer employeId;
 private Integer tacheId;
 public EmployeTacheId(){}
 public EmployeTacheId(Integer a,Integer b){this.employeId=a;this.tacheId=b;}
 public Integer getEmployeId(){return employeId;}
 public Integer getTacheId(){return tacheId;}
 public boolean equals(Object o){if(this==o)return true;if(!(o instanceof EmployeTacheId))return false;EmployeTacheId autre=(EmployeTacheId)o;return Objects.equals(employeId,autre.employeId)&&Objects.equals(tacheId,autre.tacheId);}
 public int hashCode(){return Objects.hash(employeId,tacheId);}
}
