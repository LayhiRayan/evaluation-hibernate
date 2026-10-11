package ma.projet.util;

import java.io.*;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ma.projet.classes.*;

public final class HibernateUtil {
    private static SessionFactory factory;
    private HibernateUtil() {}
    private static Configuration configuration() {
        Properties properties = new Properties();
        try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) { throw new IllegalStateException("application.properties introuvable."); }
            properties.load(input);
        } catch (IOException erreur) { throw new IllegalStateException(erreur); }
        Configuration config = new Configuration().addProperties(properties);
        config.addAnnotatedClass(Categorie.class);
        config.addAnnotatedClass(Produit.class);
        config.addAnnotatedClass(Commande.class);
        config.addAnnotatedClass(LigneCommandeProduit.class);
        return config;
    }
    public static synchronized SessionFactory getSessionFactory() {
        if (factory == null || factory.isClosed()) {
            Configuration config = configuration();
            String mode = System.getProperty("database", "h2");
            if ("mysql".equalsIgnoreCase(mode)) {
                config.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                config.setProperty("hibernate.connection.url", System.getenv().getOrDefault("DB_URL", config.getProperty("mysql.url")));
                String user = System.getenv("DB_USER");
                if (user == null || user.trim().isEmpty()) { throw new IllegalStateException("DB_USER est requis pour MySQL."); }
                config.setProperty("hibernate.connection.username", user);
                config.setProperty("hibernate.connection.password", System.getenv().getOrDefault("DB_PASSWORD", ""));
            } else if (!"h2".equalsIgnoreCase(mode)) { throw new IllegalArgumentException("database doit être h2 ou mysql."); }
            factory = config.buildSessionFactory();
        }
        return factory;
    }
    public static SessionFactory creerPourTests(String url) {
        return configuration().setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.hbm2ddl.auto", "create-drop").setProperty("hibernate.show_sql", "false").buildSessionFactory();
    }
    public static synchronized void shutdown() {
        if (factory != null && !factory.isClosed()) { factory.close(); }
        factory = null;
    }
}
