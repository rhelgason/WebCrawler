package src;

import java.util.List;

/**
 * NOTE: THIS IS NOT MY CODE.
 * Most of this line graph logic sampled from https://www.javatips.net/blog/create-line-chart-using-jfreechart
 */

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChart extends JFrame {
    private static final long serialVersionUID = 1L;

    public LineChart(String applicationTitle, String chartTitle, String xTitle, String yTitle, List<XYSeries> allSeries) {
        super(applicationTitle);

        // based on the dataset we create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xTitle, yTitle, createDataset(allSeries), PlotOrientation.VERTICAL, true, true, false);

        // Adding chart into a chart panel
        ChartPanel chartPanel = new ChartPanel(chart);

        // settind default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        // add to contentPane
        setContentPane(chartPanel);
    }

    private XYDataset createDataset(List<XYSeries> allSeries) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries series : allSeries) {
            dataset.addSeries(series);
        }
        return dataset;
    }
}