package murach.data;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        Map<String, String> overrides = new HashMap<>();

        String dbUrl  = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASS");

        if (dbUrl  != null) overrides.put("javax.persistence.jdbc.url", dbUrl);
        if (dbUser != null) overrides.put("javax.persistence.jdbc.user", dbUser);
        if (dbPass != null) overrides.put("javax.persistence.jdbc.password", dbPass);

        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("emailListPU", overrides);
    }

    private JPAUtil() {}

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void close() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
