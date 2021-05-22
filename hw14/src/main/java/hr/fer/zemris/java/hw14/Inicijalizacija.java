package hr.fer.zemris.java.hw14;

import static hr.fer.zemris.java.hw14.util.PollUtil.CREATION_PROPERTIES;
import static hr.fer.zemris.java.hw14.util.PollUtil.DB_PROPERTIES;
import static hr.fer.zemris.java.hw14.util.PollUtil.ENTRY_PROPERTIES;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Class {@code Inicijalizacija} represents an implementation of a web listener.
 * 
 * @author stipe
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {
	
	/**
	 * Servlet context event.
	 */
	ServletContextEvent sce;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.sce = sce;
	
		Properties prop = loadProperties(DB_PROPERTIES);
		String connectionURL = getURL(prop);
		ComboPooledDataSource cpds = createCPDS(connectionURL);
		updateTables(cpds);
	
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	/**
	 * Creates and initializes a combo pooled data source.
	 * 
	 * @param connectionURL url of teh connection
	 * @return cdps
	 */
	private ComboPooledDataSource createCPDS(String connectionURL) {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error while initializing pool.", e1);
		}
		cpds.setJdbcUrl(connectionURL);
		return cpds;
	}

	/**
	 * Gets the database url using the properties.
	 * 
	 * @param p property
	 * @return String representing url
	 */
	private String getURL(Properties p) {
		return String.format("jdbc:derby://%s:%s/%s;user=%s;password=%s", 
				getProperty(p, "host"), getProperty(p, "port"), getProperty(p, "name"),
				getProperty(p, "user"), getProperty(p, "password")
			);
	}

	/**
	 * Creates and loads properties.
	 * 
	 * @param propPath location of properties
	 * @return loaded properties
	 */
	private Properties loadProperties(String propPath) {
		Properties prop = new Properties();
		try {
			prop.load(sce.getServletContext().getResourceAsStream(propPath));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load properties!");
		}
		return prop;
	}
	
	/**
	 * Updates table if necessary.
	 * 
	 * @param cpds
	 */
	private void updateTables(ComboPooledDataSource cpds) {
		try (Connection con = cpds.getConnection()){
			
			checkTable(con, TableType.POLLS);
			checkTable(con, TableType.POLLOPTIONS);
			
			if (isTableEmpty(con, TableType.POLLS)) {
				populateTable(con);				
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't create or update tables.");
		}
	}

	/**
	 * Checks if the table exists.
	 * 
	 * @param con connection
	 * @param tableType type of table
	 * @throws SQLException
	 */
	private void checkTable(Connection con, TableType tableType) throws SQLException {
		DatabaseMetaData dbm = con.getMetaData();
		ResultSet tables = dbm.getTables(null, null, tableType.toString(), null);
		if (!tables.next()) {
			createTable(con, tableType);
		}
	}

	/**
	 * Populates the empty table with the necessary data.
	 * 
	 * @param con connection
	 * @throws SQLException
	 */
	private void populateTable(Connection con) throws SQLException {
		addTableEntry(con, PollType.BAND);
		addTableEntry(con, PollType.CHRIS);
	}

	/**
	 * Adds a table entry.
	 * 
	 * @param con connection
	 * @param type type of table
	 * @throws SQLException
	 */
	private void addTableEntry(Connection con, PollType type) throws SQLException {
		Properties p = loadProperties(ENTRY_PROPERTIES);
		
		Poll poll = new Poll(
				0, 
				getProperty(p, type + "title"), 
				getProperty(p, type + "message")
			);
		
		String definitions = getProperty(p, type + "def");
		List<PollOption> pollOptions = getPollOptions(definitions);
		
		insertQuery(con, poll, pollOptions);
	}

	/**
	 * Executes a query which inserts entries into Polls and PollOptions tables.
	 * 
	 * @param con connection
	 * @param poll poll in table
	 * @param pollOptions poll options
	 * @throws SQLException
	 */
	private void insertQuery(Connection con, Poll poll, List<PollOption> pollOptions) throws SQLException {
        PreparedStatement pst = con.prepareStatement(
        	"INSERT INTO POLLS (title, message) values (?,?)",
        	PreparedStatement.RETURN_GENERATED_KEYS
        );

        pst.setString(1, poll.getTitle());
        pst.setString(2, poll.getMessage());
        pst.executeUpdate();
        
        ResultSet rset = pst.getGeneratedKeys();
        rset.next();
        poll.setId(rset.getLong(1));
        
        pst = con.prepareStatement(
        	"INSERT INTO POLLOPTIONS (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)"
        );
        
        for (PollOption option : pollOptions) {
        	option.setPollId(poll.getId());
        	pst.setString(1, option.getOptionTitle());
        	pst.setString(2, option.getOptionLink());
        	pst.setLong(3, option.getPollId());
        	pst.setLong(4, option.getVotesCount());
        	pst.executeUpdate();
        }
	}

	/**
	 * Gets table options from the definitions location.
	 * 
	 * @param definitions
	 * @return list of poll options
	 */
	private List<PollOption> getPollOptions(String definitions) {
		Path defPath = getRealPath(definitions);
		List<PollOption> pollOptions = readDefinitions(defPath);
		Collections.sort(pollOptions, (f,s) -> Long.compare(f.getId(), s.getId()));
		
		return pollOptions;
	}

	/**
	 * Reads the definitions from the file and creates poll options.
	 * 
	 * @param defPath path of options definitions
	 * @return list of poll options
	 */
	private List<PollOption> readDefinitions(Path defPath) {
		List<PollOption> pollOptions = null;
		try (Stream<String> stream = Files.lines(defPath)) {
			pollOptions = stream.map(l -> l.split("\\t"))
				.map(l -> new PollOption(Long.parseLong(l[0]), l[1], l[2], 0, 0))
				.collect(Collectors.toList());	
		
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read poll definitions!");
		} catch (NumberFormatException e) {
			throw new RuntimeException("Illegal ID number format in definitions!");
		}
		
		return pollOptions;
	}

	private Path getRealPath(String fileName) {
		return Paths.get(sce.getServletContext().getRealPath(fileName));
	}

	/**
	 * Checks if the table is empty.
	 * 
	 * @param con connection
	 * @param tableType type of table
	 * @return {@code true} if empty, {@code false} otherwise
	 * @throws SQLException
	 */
	private boolean isTableEmpty(Connection con, TableType tableType) throws SQLException {
		String query = "SELECT * FROM " + tableType;
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rset = pst.executeQuery();
        
        return !rset.next();
	}

	/**
	 * Creates a table of the given type.
	 * 
	 * @param con connection
	 * @param tableType type of table
	 * @throws SQLException
	 */
	private void createTable(Connection con, TableType tableType) throws SQLException {
		Properties prop = loadProperties(CREATION_PROPERTIES);
		String queryText = getProperty(prop, tableType.toString());
		PreparedStatement pst = con.prepareStatement(queryText);
        pst.executeUpdate();
	}

	/**
	 * Gets property with the given name.
	 * 
	 * @param prop properties
	 * @param propertyName property name
	 * @return property value
	 */
	private String getProperty(Properties prop, String propertyName) {
		String property = prop.getProperty(propertyName);
		if (property == null) {
			throw new RuntimeException("Properties don't contain " + propertyName + "!");
		}
		return property;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce
				.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}