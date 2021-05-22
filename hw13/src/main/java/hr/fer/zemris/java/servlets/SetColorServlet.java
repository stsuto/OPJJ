package hr.fer.zemris.java.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class {@code SetColorServlet} represents a servlet which
 * changes the current color of the background. More precisely,
 * it sets parameters which are to be set as background colors.
 * 
 * @author stipe
 *
 */
@WebServlet("/setcolor")
public class SetColorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("color") != null) {
            req.getSession().setAttribute("pickedBgCol", req.getParameter("color"));
        }
        req.getRequestDispatcher("colors.jsp").forward(req, resp);
    }
}