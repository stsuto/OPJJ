package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.dao.sql.SQLDAO;

/**
 * Singleton class capable of returning a DAO implementation.
 * 
 * @author stipe
 *
 */
public class DAOProvider {

	/**
	 * DAO implementation.
	 */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt koji enkapsulira pristup sloju za perzistenciju podataka.
	 */
	/**
	 * Implementation getter.
	 * 
	 * @return object which encapsulates data persistence access.
	 */
	public static DAO getDao() {
		return dao;
	}
	
}