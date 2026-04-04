package controller;

import dto.MatchDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;
import service.MatchService;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        this.matchService = new MatchService(
                (SessionFactory) getServletContext().getAttribute("sessionFactory")
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam)); }
            catch (NumberFormatException ignored) {}
        }

        String filterName = req.getParameter("filter_by_player_name");

        List<MatchDto> all = matchService.getFinishedMatches(filterName);

        final int totalPages = (int) Math.ceil((double)  all.size() / PAGE_SIZE);
        final int from = Math.min(PAGE_SIZE * (page - 1), all.size());
        final int to = Math.min(from  + PAGE_SIZE, all.size());
        List<MatchDto> pageMatches = all.subList(from, to);

        req.setAttribute("finishedMatches", pageMatches);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/matches.jsp").forward(req,resp);
    }
}
