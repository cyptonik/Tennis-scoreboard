package listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Match;
import model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.h2.Driver"); // явная регистрация
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:tennis_scoreboard;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        HikariDataSource dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection()) {
            String sql = new String(
                        getClass().getClassLoader().getResourceAsStream("schema.sql").readAllBytes()
                );
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Players (name) VALUES ('Alice')");
            stmt.execute("INSERT INTO Players (name) VALUES ('Bob')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        sce.getServletContext().setAttribute("dataSource", dataSource);
        sce.getServletContext().setAttribute("sessionFactory", sessionFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactory sessionFactory = (SessionFactory) sce.getServletContext().getAttribute("sessionFactory");
        if (sessionFactory != null) sessionFactory.close();

        HikariDataSource ds = (HikariDataSource) sce.getServletContext().getAttribute("dataSource");
        if (ds != null) ds.close();
    }
}
