package hr.fer.zemris.java.hw14.model;

/**
 * Class representing a poll option table entry.
 * 
 * @author stipe
 *
 */
public class PollOption {
	
	/**
	 * Poll option id.
	 */
	private long id;
	/**
	 * Poll option title.
	 */
	private String optionTitle;
	/**
	 * Poll option link.
	 */
	private String optionLink;
	/**
	 * Poll id.
	 */
	private long pollId;
	/**
	 * Poll option vote count.
	 */
	private long votesCount;
	
	/**
	 * @param id
	 * @param optionTitle
	 * @param optionLink
	 * @param pollId
	 * @param votesCount
	 */
	public PollOption(long id, String optionTitle, String optionLink, long pollId, long votesCount) {
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollId = pollId;
		this.votesCount = votesCount;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the optionTitle
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * @param optionTitle the optionTitle to set
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * @return the optionLink
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * @param optionLink the optionLink to set
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * @return the pollId
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * @param pollId the pollId to set
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	/**
	 * @return the votesCount
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * @param votesCount the votesCount to set
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}
	
	@Override
	public String toString() {
		return "ID=" + id + ", Option title=" + optionTitle + ", Option link=" + optionLink
				+ ", Poll ID=" + pollId + ", Votes count=" + votesCount;
	}
	
}

