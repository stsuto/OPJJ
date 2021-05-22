package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * Class {@code GlasanjeGlasajServlet} is a servlet which 
 * defines the act of voting.
 * 
 * @author stipe
 *
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id;
		long pollID;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
			id = Long.parseLong(req.getParameter("id"));
		} catch (RuntimeException e) {			
			return;
		}
		
		DAOProvider.getDao().vote(id);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollID);
	}
	
}
