package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.servlets.GlasanjeRezultatiServlet.Vote;

/**
 * Class {@code GlasanjeXlsServlet} represents a server which
 * creates an xml file with voting information.
 * 
 * @author stipe
 *
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Vote> voteValues = GlasanjeRezultatiServlet.getVoteValues(req);
		
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		
		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("New sheet");
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell((short) 0).setCellValue("Bend");
		rowhead.createCell((short) 1).setCellValue("Broj glasova");

		int len = voteValues.size();
		for (int i = 0; i < len; i++) {
			Vote vote = voteValues.get(i);
			HSSFRow row = sheet.createRow((short) i + 1);
			row.createCell((short) 0).setCellValue(vote.getName());
			row.createCell((short) 1).setCellValue(vote.getVotes());
		}

		hwb.write(resp.getOutputStream());
		hwb.close();
	}
	
}
