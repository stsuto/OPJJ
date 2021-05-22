package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.form.LoginForm;
import hr.fer.zemris.java.hw15.model.BlogUser;

@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginForm userForm = new LoginForm();
		userForm.loadFromUser(new BlogUser());
		
		List<BlogUser> authors = DAOProvider.getDAO().getAuthors();
		
		req.setAttribute("authors", authors);
		req.setAttribute("user", userForm);
		req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> authors = DAOProvider.getDAO().getAuthors();
		req.setAttribute("authors", authors);

		LoginForm userForm = new LoginForm();
		userForm.loadFromHttpRequest(req);
		userForm.validate();
		
		BlogUser savedUser = DAOProvider.getDAO().getUser(userForm.getNick());
		
		if (userForm.hasErrors()) {
			req.setAttribute("user", userForm);
			req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
			
		} else if (savedUser == null || !checkPassword(userForm, savedUser)) {
			userForm.setError("password", "Incorrect username and password combination.");
			req.setAttribute("user", userForm);
			req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);

		} else {
			req.getSession().setAttribute("current.user.id", savedUser.getId());
	        req.getSession().setAttribute("current.user.fn", savedUser.getFirstName());
	        req.getSession().setAttribute("current.user.ln", savedUser.getLastName());
	        req.getSession().setAttribute("current.user.nick", savedUser.getNick());
	       
	        resp.sendRedirect("main");
		}
	}

	private boolean checkPassword(LoginForm userForm, BlogUser savedUser) {
		return userForm.getPasswordHash().equals(savedUser.getPasswordHash());
	}
	
}
