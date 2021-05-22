package hr.fer.zemris.java.hw15.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public List<BlogUser> getAuthors() throws DAOException {
		EntityManager em = JPAEMFProvider.getEmf().createEntityManager();
		em.getTransaction().begin();

		List<BlogUser> authors = 
				em.createQuery("select users from BlogUser as users", BlogUser.class)
					.getResultList();
				
		em.getTransaction().commit();
		em.close();
		
		return authors;
	}

	@Override
	public BlogUser getUser(String nick) {
		EntityManager em = JPAEMFProvider.getEmf().createEntityManager();
		em.getTransaction().begin();

		BlogUser author = null;
		try {
			author = em.createQuery("select b from BlogUser as b where b.nick=:nc", BlogUser.class)
					.setParameter("nc", nick)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
				
		em.getTransaction().commit();
		em.close();
		
		return author;
	}

	@Override
	public void addUser(BlogUser user) {
		EntityManager em = JPAEMFProvider.getEmf().createEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		em.close();
	}
	
}