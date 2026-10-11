package ma.projet.util;
import java.util.Arrays;
import org.h2.tools.Server;
public final class ConsoleH2 {
 private ConsoleH2(){}
 public static void attendre(String[] args,String base) throws Exception {
  if(!Arrays.asList(args).contains("--console"))return;
  if(!"h2".equalsIgnoreCase(System.getProperty("database","h2")))throw new IllegalArgumentException("La console nécessite H2.");
  Server console=Server.createWebServer("-webPort","8082").start();
  try{
   System.out.println("Console H2 : http://localhost:8082 | JDBC : jdbc:h2:mem:"+base+" | utilisateur : sa | mot de passe vide");
   System.out.println("Appuyez sur Entrée pour arrêter.");System.in.read();
  }finally{console.stop();}
 }
}
