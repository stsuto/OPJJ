package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.form.CommentForm;
import hr.fer.zemris.java.hw15.form.EntryForm;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

@WebServlet("/servleti/author/*")
public class EntriesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}	
	
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo().substring(1);
		String[] url = pathInfo.split("/");
		
		String nick = url[0];
		BlogUser author = DAOProvider.getDAO().getUser(nick);
		
		if (author == null) {
			sendError(req, resp);
			
		} else if (url.length == 1) {
			showEntries(author, req, resp);
		
		} else if (url.length == 2) {
			processTwoArguments(url, author, req, resp);
		
		} else {
			sendError(req, resp);
		}	
	}

	private void sendError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
	}

	private void showEntries(BlogUser author, HttpServletRequest req, 
				HttpServletResponse resp) throws ServletException, IOException {
		
		List<BlogEntry> entries = author.getEntries();
		req.setAttribute("entries", entries);
		req.setAttribute("author", author);
		req.getRequestDispatcher("/WEB-INF/pages/Entries.jsp").forward(req, resp);
	}

	private void processTwoArguments(String[] url, BlogUser author, HttpServletRequest req, 
				HttpServletResponse resp) throws ServletException, IOException {
		
		if (url[1].equals("new")) {
			createEntry(author, req, resp);
			
		} else if (url[1].equals("edit")) {
			long entryId = parseId(req.getParameter("id"));
			if (entryId < 0) {
				sendError(req, resp);
			} else {
				editEntry(author, entryId, req, resp);				
			}
			
		} else {
			long entryId = parseId(url[1]);
			if (entryId < 0) {
				sendError(req, resp);
			} else {
				showEntry(author, entryId, req, resp);					
			}
		}
	}

	private void createEntry(BlogUser author, HttpServletRequest req, HttpServletResponse resp) 
				throws ServletException, IOException {
		if (!isUserValid(author, req)) {
			sendError(req, resp);
		}
		
		BlogEntry entry = new BlogEntry();
		entry.setCreatedAt(new Date());
		entry.setLastModifiedAt(new Date());
		
		EntryForm form = new EntryForm();
		form.loadFromEntry(entry);
		
		req.setAttribute("form", form);
		req.setAttribute("author", author);
		req.setAttribute("action", "create");
		req.getRequestDispatcher("WEB-INF/pages/ChangeEntry.jsp").forward(req, resp);
	}

	private boolean isUserValid(BlogUser author, HttpServletRequest req) {
		Long obj = (Long) req.getSession().getAttribute("current.user.id");
		return obj != null && obj.equals(author.getId());
	}

	private void showEntry(BlogUser author, long entryId, HttpServletRequest req, 
				HttpServletResponse resp) throws ServletException, IOException {
		
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryId);
		if (!isEntryValid(entry, author)) {
			sendError(req, resp);
		}

		CommentForm form = new CommentForm();
		form.loadFromComment(new BlogComment());
		
		req.setAttribute("form", form);
		req.setAttribute("entry", entry);
		req.setAttribute("author", author);

		req.getRequestDispatcher("/WEB-INF/pages/ShowEntry.jsp").forward(req, resp);
	}

	private void editEntry(BlogUser author, long entryId, HttpServletRequest req, 
				HttpServletResponse resp) throws ServletException, IOException {
		
		String currentNick = (String) req.getSession().getAttribute("current.user.nick");
		if (!author.getNick().equals(currentNick)) {
			sendError(req, resp);
			return;
		}
		
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryId);		
		if (entry == null) {
			sendError(req, resp);
			return;
		}
		if (!entry.getCreator().equals(author)) {
			sendError(req, resp);
			return;	
		}
		
		EntryForm form = new EntryForm();
		form.loadFromEntry(entry);
		entry.setLastModifiedAt(new Date());
		
		req.setAttribute("form", form);
		req.setAttribute("author", author);
		req.setAttribute("action", "create");
		req.getRequestDispatcher("WEB-INF/pages/ChangeEntry.jsp").forward(req, resp);
	}

	private long parseId(String id) {
		try {
			return Long.parseLong(id);				
		} catch (Exception e) {
			return -1;
		}
	}

	private boolean isEntryValid(BlogEntry entry, BlogUser author) {
		return entry != null && entry.getCreator().equals(author);
	}
	
}