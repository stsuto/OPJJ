package hr.fer.zemris.java.hw14.servlets;

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

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Class {@code GlasanjeGrafikaServlet} represents a servlet 
 * action of creating a pie chart from current voting values.
 * 
 * @author stipe
 *
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		try {
			long pollID = Long.parseLong(req.getParameter("pollID"));
			List<PollOption> voteValues = DAOProvider.getDao().getPollOptions(pollID);
			
			ChartUtilities.writeChartAsPNG(
				resp.getOutputStream(), 
				createChart(createDataset(voteValues), "Band contest"), 
				500, 
				500
			);
		} catch (Exception ex) {}
	}

	/**
     * Creates a sample dataset
     */
    private  PieDataset createDataset(List<PollOption> voteValues) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (PollOption vote : voteValues) {
            dataset.setValue(vote.getOptionTitle(), vote.getVotesCount());
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
