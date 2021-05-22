package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Class {@code GlasanjeServlet} represents the main voting page.
 * 
 * @author stipe
 *
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollID = req.getParameter("pollID");
		
		if (pollID != null) {
			try {
				long pollId = Long.parseLong(pollID);				
				Poll poll = DAOProvider.getDao().getPoll(pollId);
				List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollId);
				req.setAttribute("poll", poll);
				req.setAttribute("pollOptions", pollOptions);
				
			} catch (NumberFormatException e) {}	
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
