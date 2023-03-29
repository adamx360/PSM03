import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class EnergyFrame extends JFrame {
    private static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3;
    private static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
    private final XYSeries potentialEnergySeries;
    private final XYSeries kineticEnergySeries;
    private final XYSeries totalEnergySeries;

    public EnergyFrame(String name) {
        setTitle("Wykres Energii");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        potentialEnergySeries = new XYSeries("Energia Potencjalna");
        kineticEnergySeries = new XYSeries("Energia Kinetyczna");
        totalEnergySeries = new XYSeries("Energia Calkowita");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(potentialEnergySeries);
        dataset.addSeries(kineticEnergySeries);
        dataset.addSeries(totalEnergySeries);

        JFreeChart chart = ChartFactory.createXYLineChart(name, "Czas", "Energia", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setContentPane(chartPanel);
    }

    public void addDataPoint(double time, double potentialEnergy, double kineticEnergy, double totalEnergy) {
        potentialEnergySeries.add(time, potentialEnergy);
        kineticEnergySeries.add(time, kineticEnergy);
        totalEnergySeries.add(time, totalEnergy);
    }
}
