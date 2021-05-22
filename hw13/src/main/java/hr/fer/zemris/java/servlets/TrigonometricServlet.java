package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class {@code TrigonometricServlet} represents a servlet which
 * calculates sine and cosine values and uses them to create an
 * html table.
 * 
 * @author stipe
 *
 */
@WebServlet("/trigonometric")
public class TrigonometricServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static int DEFAULT_A = 0;
	private static int DEFAULT_B = 360;
	private static int MAX_DIFF = 720;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String a = req.getParameter("a");
		String b = req.getParameter("b");
		
		int varA = DEFAULT_A;
		int varB = DEFAULT_B;

		try {
			if (a != null) {
				varA = Integer.valueOf(a);
			}
			if (b != null) {
				varB = Integer.valueOf(b);
			}
		} catch (Exception ignorable) {
		}
		
		if (varA > varB) {
			int tmp = varA;
			varA = varB;
			varB = tmp;
		}
		
		if (varB > varA + MAX_DIFF) {
			varB = varA + MAX_DIFF;
		}

		List<AngleValue> values = new ArrayList<>(); 

		for (int i = varA; i <= varB; i++) {
			values.add(new AngleValue(i));
		}

		req.setAttribute("angleValues", values);
		
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Structure containing information about the angle and the
	 * sine and cosine values for that angle.
	 * 
	 * @author stipe
	 *
	 */
	public static class AngleValue {
		/**
		 * Angle in degrees.
		 */
		private int angle;
		/**
		 * Sine value for the angle.
		 */
		private double sin;
		/**
		 * Cosine value for the angle.
		 */
		private double cos;
		
		/**
		 * Constructor
		 * 
		 * @param angle
		 */
		AngleValue(int angle){
			this.angle = angle;
			this.sin = Math.sin(Math.toRadians(angle));
			this.cos = Math.cos(Math.toRadians(angle));
		}
		
		/**
		 * @return the angle
		 */
		public int getAngle() {
			return angle;
		}
		
		/**
		 * @return the sin
		 */
		public double getSin() {
			return sin;
		}
		
		/**
		 * @return the cos
		 */
		public double getCos() {
			return cos;
		}
		
	}
	
}



