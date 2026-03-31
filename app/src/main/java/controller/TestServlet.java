package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    private DataSource dataSource;
    @Override
    public void init() throws ServletException {
        this.dataSource = (DataSource) getServletContext().getAttribute("dataSource");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (dataSource == null) {
            resp.getWriter().println("dataSource is NULL");
            return;
        }

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Players")) {
            while (rs.next()) {
                resp.getWriter().println(String.valueOf(rs.getInt("id")) + " " + rs.getString("Name"));
            }
        } catch (SQLException e) {
            resp.getWriter().println("database error");
        }
    }
}
