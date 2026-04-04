package listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dto.MatchScore;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Match;
import model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import service.MatchService;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:tennis_scoreboard;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        HikariDataSource dataSource = new HikariDataSource(config);

        initializeDatabaseTables(dataSource);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .applySetting("hibernate.connection.datasource", dataSource)
                .build();

        SessionFactory sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(Match.class)
                .buildMetadata()
                .buildSessionFactory();
        sce.getServletContext().setAttribute("sessionFactory", sessionFactory);

        MatchService matchService = new MatchService(sessionFactory);
        sce.getServletContext().setAttribute("matchService", matchService);

        Map<UUID, MatchScore> ongoingMatches = new ConcurrentHashMap<>();
        sce.getServletContext().setAttribute("ongoingMatches", ongoingMatches);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactory sessionFactory = (SessionFactory) sce.getServletContext().getAttribute("sessionFactory");
        if (sessionFactory != null) sessionFactory.close();

        HikariDataSource ds = (HikariDataSource) sce.getServletContext().getAttribute("dataSource");
        if (ds != null) ds.close();
    }

    private void initializeDatabaseTables(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = new String(
                    getClass().getClassLoader().getResourceAsStream("schema.sql").readAllBytes()
            );
            conn.createStatement().execute(sql);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
