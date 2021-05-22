package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemris.java.servlets.GlasanjeRezultatiServlet.Vote;

/**
 * Class {@code GlasanjeGrafikaServlet} represents a servlet 
 * action of creating a pie chart from current voting values.
 * 
 * @author stipe
 *
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Vote> voteValues = GlasanjeRezultatiServlet.getVoteValues(req);
		
		resp.setContentType("image/png");
		
		ChartUtilities.writeChartAsPNG(
				resp.getOutputStream(), 
				createChart(createDataset(voteValues), "Band contest"), 
				500, 
				500
			);
	}

	/**
     * Creates a sample dataset
     */
    private  PieDataset createDataset(List<Vote> voteValues) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (GlasanjeRezultatiServlet.Vote vote : voteValues) {
            dataset.setValue(vote.getName(), vote.getVotes());
        }
        
        return dataset;
    }

    /**
     * Creates a chart
     */
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(
            title,                  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
    
        return chart;
    }
	
}
