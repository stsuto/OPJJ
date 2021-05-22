package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Class {@code PowersServlet} represents a servlet which creates
 * an xml using the given parameters.
 * 
 * @author stipe
 *
 */
@WebServlet("/powers")
public class PowersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a = -101;
		int b = -101;
		int n = 0;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));		
		} catch (NumberFormatException ignorable) {
		}

		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch (NumberFormatException ignorable) {
		}

		try {
			n = Integer.parseInt(req.getParameter("n"));
		} catch (NumberFormatException ignorable) {
		}
		
		String msg = getErrorMessage(a,b,n);
		if (msg != null) {			
			req.setAttribute("errorMsg", msg);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		
		HSSFWorkbook hwb = new HSSFWorkbook();

		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet = hwb.createSheet("Sheet #" + i);
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("x");
			rowhead.createCell((short) 1).setCellValue("x^" + i);
			
			for (int j = a, k = 1; j <= b; j++, k++) {
				HSSFRow row = sheet.createRow((short) k);
				row.createCell((short) 0).setCellValue(j);
				row.createCell((short) 1).setCellValue(Math.pow(j, i));
			}
		}

		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * Creates an error message.
	 * 
	 * @param a a parameter
	 * @param b b parameter
	 * @param n n parameter
	 * @return  error message
	 */
	private String getErrorMessage(int a, int b, int n) {
		StringBuilder msg = new StringBuilder();

		if (a < -100 || a > 100) {
			msg.append("Parameter \"a\" must be an integer in [-100,100] interval.\n");
		} 
		if (b < -100 || b > 100) {
			msg.append("Parameter \"b\" must be an integer in [-100,100] interval.\n");
		} 
		if (n < 1 || n > 5) {
			msg.append("Parameter \"n\" must be an integer in [1,5] interval.");
		}
		
		return msg.length() == 0 ? null : msg.toString();
	}
	
}
