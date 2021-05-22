package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * Class {@code HomeServlet} represents the home page of the application.
 * 
 * @author stipe
 *
 */
@WebServlet("/servleti/index.html")
public class HomeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("polls", DAOProvider.getDao().getPolls());
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
	}
	
}
