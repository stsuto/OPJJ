package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Class {@code GlasanjeRezultatiServlet} is a servlet which shows 
 * the voting results in a html table.
 * 
 * @author stipe
 *
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID;
		try {
			pollID = Long.parseLong(req.getParameter("pollID"));
		} catch (RuntimeException e) {
			return;
		}
		
		List<PollOption> voteValues = DAOProvider.getDao().getPollOptions(pollID);
		voteValues.sort((f,s) -> Long.compare(s.getVotesCount(), f.getVotesCount()));
		List<PollOption> best = getBest(voteValues);
		
		req.setAttribute("voteValues", voteValues);
		req.setAttribute("best", best);
		req.setAttribute("pollID", pollID);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Gets the most voted poll option.
	 * 
	 * @param voteValues list of vote info
	 * @return the most voted option
	 */
	private List<PollOption> getBest(List<PollOption> voteValues) {
		List<PollOption> best = new ArrayList<>();
		long maxVotes = voteValues.get(0).getVotesCount();
		
		for (PollOption value : voteValues) {
			if (value.getVotesCount() != maxVotes) {
				break;
			}
			best.add(value);
		}
		
		return best;
	}
	
}
