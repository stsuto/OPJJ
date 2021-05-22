package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.form.RegistrationForm;
import hr.fer.zemris.java.hw15.model.BlogUser;

@WebServlet("/servleti/register")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RegistrationForm userForm = new RegistrationForm();
		userForm.loadFromUser(new BlogUser());
	
		req.setAttribute("user", userForm);
		
		req.getRequestDispatcher("/WEB-INF/pages/Registration.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ("Cancel".equals(req.getParameter("method"))) {
			resp.sendRedirect("main");
			return;
		}
		
		RegistrationForm userForm = new RegistrationForm();
		userForm.loadFromHttpRequest(req);
		userForm.validate();
		
		BlogUser user = new BlogUser();
		userForm.saveToUser(user);
		
		if (userForm.hasErrors()) {
			req.setAttribute("user", userForm);
			req.getRequestDispatcher("/WEB-INF/pages/Registration.jsp").forward(req, resp);
		
		} else if (!isNicknameAvailable(userForm)) {
			userForm.setError("nick", "Nickname is taken.");
			req.setAttribute("user", userForm);		
			req.getRequestDispatcher("/WEB-INF/pages/Registration.jsp").forward(req, resp);
		
		} else {			
			DAOProvider.getDAO().addUser(user);
			
			req.getSession().setAttribute("current.user.id", user.getId());
			req.getSession().setAttribute("current.user.fn", user.getFirstName());
			req.getSession().setAttribute("current.user.ln", user.getLastName());
			req.getSession().setAttribute("current.user.nick", user.getNick());
	     
			resp.sendRedirect("main");
		}
	}

	private boolean isNicknameAvailable(RegistrationForm userForm) {
		return DAOProvider.getDAO().getUser(userForm.getNick()) == null;
	}
	
}
