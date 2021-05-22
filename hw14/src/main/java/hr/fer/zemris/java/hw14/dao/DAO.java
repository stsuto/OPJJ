package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Interface for data persistence.
 * 
 * @author stipe
 *
 */
public interface DAO {

	/**
	 * Gets a list of all polls within database.
	 * 
	 * @return list of polls
	 * @throws DAOException
	 */
	public List<Poll> getPolls() throws DAOException;
	
	/**
	 * Gets a poll with the given id. Returns {@code null}
	 * if no poll has been found.
	 * 
	 * @param id
	 * @return poll with given id
	 * @throws DAOException
	 */
	public Poll getPoll(long id) throws DAOException;

	/**
	 * Gets a list of all poll options for the poll with the given id.
	 * 
	 * @param id id of the poll
	 * @return list of poll options
	 * @throws DAOException
	 */
	public List<PollOption> getPollOptions(long id) throws DAOException;

	/**
	 * Executes the vote for the given poll option within the given poll.
	 * 
	 * @param id poll option id
	 * @param pollId poll id
	 */
	public void vote(long id);
	
}