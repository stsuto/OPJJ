package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class {@code GlasanjeRezultatiServlet} is a servlet which shows 
 * the voting results in a html table.
 * 
 * @author stipe
 *
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static List<String> bandsInfo;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		bandsInfo = getBandsInfo(req);
		List<Vote> voteValues = getVoteValues(req);
		List<Vote> best = getBest(voteValues);
		
		req.setAttribute("voteValues", voteValues);
		req.setAttribute("best", best);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	/**
	 * Gets the information about the bands in predefined file.
	 * 
	 * @param req request
	 * @return list of band informations
	 * @throws IOException in case of an I/O error
	 */
	private List<String> getBandsInfo(HttpServletRequest req) throws IOException {
		String definitionFile = req.getServletContext()
				.getRealPath("/WEB-INF/glasanje-definicija.txt");
		return Files.readAllLines(Paths.get(definitionFile));
		
	}

	/**
	 * Gets the information about voting status.
	 * 
	 * @param req request
	 * @return list of voting informations
	 * @throws IOException in case of an I/O error
	 */
	public static List<Vote> getVoteValues(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext()
				.getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path path = Paths.get(fileName);
		
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		
		List<Vote> voteValues = null;
		try (Stream<String> stream = Files.lines(path)){
			voteValues = stream.map(l -> getAllInfo(l))
				.sorted((f, s) -> Integer.compare(s.votes, f.votes))
				.collect(Collectors.toList());
		}
		return voteValues;
	}

	/**
	 * Gets the most voted for bands.
	 * 
	 * @param voteValues list of vote info
	 * @return teh best bands
	 */
	private List<Vote> getBest(List<Vote> voteValues) {
		List<Vote> best = new ArrayList<>();
		int maxVotes = voteValues.get(0).votes;
		
		for (Vote value : voteValues) {
			if (value.votes != maxVotes) {
				break;
			}
			best.add(value);
		}
		
		return best;
	}

	/**
	 * Gets all info about the band with the id 
	 * contained in given string.
	 * 
	 * @param line string of information
	 * @return Vote with voting info
	 */
	private static Vote getAllInfo(String line) {
		String[] voteParts = line.split("\t");
		
		for (String band : bandsInfo) {
			String[] bandParts = band.split("\t");
			if (bandParts[0].equals(voteParts[0])) {
				return new Vote(
					bandParts[1], 
					bandParts[2], 
					Integer.parseInt(voteParts[1])
				);
			}
		}
		
		return null;
	}
	
	/**
	 * Structure representing voting informations.
	 * 
	 * @author stipe
	 *
	 */
	public static class Vote {
		/**
		 * Name of band.
		 */
		String name;
		/**
		 * Band's song name.
		 */
		String song;
		/**
		 * Number of votes.
		 */
		int votes;
		
		/**
		 * Constructor
		 * 
		 * @param name {@link #name}
		 * @param song {@link #song}
		 * @param votes {@link #votes}
		 */
		public Vote(String name, String song, int votes) {
			this.name = name;
			this.song = song;
			this.votes = votes;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the song
		 */
		public String getSong() {
			return song;
		}

		/**
		 * @return the votes
		 */
		public int getVotes() {
			return votes;
		}
	}
	
}
